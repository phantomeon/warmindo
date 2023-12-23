package com.android.warmindo

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.res.ColorStateList
import android.graphics.Paint
import android.graphics.Typeface
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.SpannableString
import android.text.Spanned
import android.text.style.StyleSpan
import android.widget.TextView
import androidx.core.content.res.ResourcesCompat
import android.text.TextPaint
import android.text.style.TypefaceSpan
import android.view.View
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale


class TransactionActivity : AppCompatActivity() {

    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var dbref : DatabaseReference
    private lateinit var userRecyclerview : RecyclerView
    private lateinit var userArrayList : ArrayList<TransactionModel>

    class CustomTypefaceSpan(private val newType: Typeface) : TypefaceSpan("") {
        override fun updateDrawState(ds: TextPaint) {
            applyCustomTypeFace(ds, newType)
        }

        override fun updateMeasureState(paint: TextPaint) {
            applyCustomTypeFace(paint, newType)
        }

        private fun applyCustomTypeFace(paint: Paint, tf: Typeface) {
            paint.typeface = tf
        }
    }
    private fun updateUIWithUserName(userName: String) {
        val welcomeTextView = findViewById<TextView>(R.id.welcome_text)
        val fullText = "Selamat bekerja,\n$userName"
        val spannable = SpannableString(fullText)

        // Dapatkan font dari resources
        val customFont: Typeface? = ResourcesCompat.getFont(this, R.font.sequel_sans_bold_head)

        // Terapkan font pada nama pengguna
        customFont?.let {
            val customTypefaceSpan = CustomTypefaceSpan(it)
            val userNameStart = fullText.indexOf(userName)
            spannable.setSpan(
                customTypefaceSpan,
                userNameStart,
                userNameStart + userName.length,
                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
            )
        }

        // Set teks ke TextView
        welcomeTextView.text = spannable
    }

    companion object {
        // Deklarasikan variabel statis untuk menyimpan nilai
        var namapengguna: String? = null
        var currDateTime: String? = null
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_transaction)

        // Inisialisasi SharedPreferences
        sharedPreferences = getPreferences(Context.MODE_PRIVATE)
        
        //inisialisasi nama username yg dipanggil dari fungsi pada DashboardActivity
        val userName = intent.getStringExtra("namapengguna")
        val idPengguna = intent.getStringExtra("idpengguna")

        //update UI dengan username
        if (userName != null) {
            updateUIWithUserName(userName)
        }

        // Ambil nilai currdatetime dari intent
        val currDateTime = intent.getStringExtra("currdatetime")

        // Ambil nilai currentDateTime dari intent
        val currentDateTime = intent.getSerializableExtra("currdatetime") as? Date

        // Simpan nilai namapengguna dan currDateTime ke SharedPreferences
        saveToSharedPreferences(userName, currDateTime)

        // Ambil nilai dari SharedPreferences
        val savedUserName = getFromSharedPreferences("namapengguna", "")
        val savedCurrDateTime = getFromSharedPreferences("currdatetime", "")

        // Gunakan nilai yang disimpan
        if (savedUserName.isNotEmpty()) {
            updateUIWithUserName(savedUserName)
        }

        if (savedCurrDateTime.isNotEmpty()) {
            // Konversi Date ke format string
            val sdf = SimpleDateFormat("dd MMMM yyyy 'pada' HH:mm", Locale.getDefault())
            val formattedDateTime = sdf.format(Date(savedCurrDateTime.toLong()))

            // Update teks pada TextView dengan ID text2
            val text2TextView = findViewById<TextView>(R.id.text2)
            text2TextView.text = formattedDateTime
        }
        // Periksa apakah currentDateTime tidak null
        if (currentDateTime != null) {
            // Konversi Date ke format string
            val sdf = SimpleDateFormat("dd MMMM yyyy 'pada' HH:mm", Locale.getDefault())
            val formattedDateTime = sdf.format(currentDateTime)

            // Update teks pada TextView dengan ID text2
            val text2TextView = findViewById<TextView>(R.id.text2)
            text2TextView.text = formattedDateTime
        }

        // Tambahkan click listener pada logout_text
        val logoutText = findViewById<TextView>(R.id.logout_text)
        logoutText.setOnClickListener {
            // Mulai Intent ke MainActivity
            val databaseAct = FirebaseDatabase.getInstance().getReference("aktivitas_pengguna")
            val idaktivitas = databaseAct.push().key // Menghasilkan kunci unik

            if (idaktivitas != null) {
                // Dapatkan tanggal dan waktu saat ini
                val currentDateTime = Date()
                // Dapatkan formatter untuk tanggal dan waktu dengan format lokal
                val dateFormatter = DateFormat.getDateInstance(DateFormat.LONG, Locale.getDefault())
                val timeFormatter = DateFormat.getTimeInstance(DateFormat.SHORT, Locale.getDefault())
                // Format tanggal dan waktu
                val formattedDate = dateFormatter.format(currentDateTime)
                val formattedTime = timeFormatter.format(currentDateTime)

                // Intent ke MainActivity
                val intent = Intent(applicationContext, MainActivity::class.java)
                startActivity(intent)

                //Menambahkan data ke tabel aktivitas_pengguna
                databaseAct.child(idaktivitas).child("aktivitas").setValue("LOGOUT")
                databaseAct.child(idaktivitas).child("idpengguna").setValue(idPengguna)
                databaseAct.child(idaktivitas).child("idaktivitas").setValue(idaktivitas)
                databaseAct.child(idaktivitas).child("tanggal").setValue(formattedDate)
                databaseAct.child(idaktivitas).child("waktu").setValue(formattedTime)

                // Menutup aktivitas DashboardActivity
                finish()
            }
        }

        userRecyclerview = findViewById(R.id.recyclerView_cooking)
        userRecyclerview.layoutManager = LinearLayoutManager(this)
        userRecyclerview.setHasFixedSize(true)

        userArrayList = arrayListOf<TransactionModel>()
        getUserData()
    }

    private fun saveToSharedPreferences(userName: String?, currDateTime: String?) {
        with(sharedPreferences.edit()) {
            putString("namapengguna", userName)
            putString("currdatetime", currDateTime)
            apply()
        }
    }

    private fun getFromSharedPreferences(key: String, defaultValue: String): String {
        return sharedPreferences.getString(key, defaultValue) ?: defaultValue
    }

    private fun getUserData() {
        dbref = FirebaseDatabase.getInstance().getReference("transaksi")
        dbref.addValueEventListener(object : ValueEventListener {

            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    userArrayList.clear() // Bersihkan list sebelum menambahkan data baru
                    for (userSnapshot in snapshot.children) {
                        val user = userSnapshot.getValue(TransactionModel::class.java)
                        // Cek apakah status transaksi adalah "BARU"
                        if (user != null && user.status == "BARU") {
                            userArrayList.add(user)
                        }
                    }
                    userRecyclerview.adapter = TransactionAdapter(userArrayList)
                }
            }

            override fun onCancelled(error: DatabaseError) {
                // Handle error
            }
        })
    }
}