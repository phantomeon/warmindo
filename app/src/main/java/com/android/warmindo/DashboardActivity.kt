package com.android.warmindo

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import com.google.firebase.database.FirebaseDatabase
import java.text.DateFormat
import java.util.Date
import java.util.Locale

class DashboardActivity : AppCompatActivity() {

    private lateinit var welcomeText: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dashboard)

        // Ambil data dari Intent yang dikirim dari aktivitas sebelumnya (MainActivity)
        val mulaiShiftButton: Button = findViewById(R.id.mulai_shift_button)
        val logoutButton: Button = findViewById(R.id.logout_button)
        val namaPengguna = intent.getStringExtra("namapengguna")
        val idPengguna = intent.getStringExtra("idpengguna")
        val idRole = intent.getIntExtra("idrole", 0)
        // Variabels untuk menambahkan aktivitas pengguna
        val databaseAct = FirebaseDatabase.getInstance().getReference("aktivitas_pengguna")
        val idaktivitas = databaseAct.push().key // Menghasilkan kunci unik

        welcomeText = findViewById(R.id.welcome_text)
        // Periksa apakah namaPengguna tidak null
        if (namaPengguna != null) {
            Log.d("DashboardActivity", "Masuk ke blok if")
            // Set teks pada welcomeText
            welcomeText.text = when (idRole) {
                2 -> "Selamat Datang, Petugas Kasir $namaPengguna"
                3 -> "Selamat Datang, Petugas Pengantar Pesanan $namaPengguna"
                4 -> "Selamat Datang, Petugas Dapur $namaPengguna"
                else -> "Selamat Datang, $namaPengguna"
            }
        }

        // Implementasi onClickListener untuk tombol Mulai Shift
        mulaiShiftButton.setOnClickListener {
            if (idRole == 4) {
                Toast.makeText(applicationContext, "Selamat Bekerja, Petugas Dapur $namaPengguna", Toast.LENGTH_SHORT).show()
                if (idaktivitas != null) {
                    // Dapatkan tanggal dan waktu saat ini
                    val currentDateTime = Date()
                    // Dapatkan formatter untuk tanggal dan waktu dengan format lokal
                    val dateFormatter = DateFormat.getDateInstance(DateFormat.LONG, Locale.getDefault())
                    val timeFormatter = DateFormat.getTimeInstance(DateFormat.SHORT, Locale.getDefault())
                    // Format tanggal dan waktu
                    val formattedDate = dateFormatter.format(currentDateTime)
                    val formattedTime = timeFormatter.format(currentDateTime)

                    // Intent ke TransactionActivity
                    val intent = Intent(applicationContext, TransactionActivity::class.java)
                    startActivity(intent)

                    //Menambahkan data ke tabel aktivitas_pengguna
                    databaseAct.child(idaktivitas).child("aktivitas").setValue("AKSES SHIFT")
                    databaseAct.child(idaktivitas).child("idpengguna").setValue(idPengguna)
                    databaseAct.child(idaktivitas).child("idaktivitas").setValue(idaktivitas)
                    databaseAct.child(idaktivitas).child("tanggal").setValue(formattedDate)
                    databaseAct.child(idaktivitas).child("waktu").setValue(formattedTime)

                    // Menutup aktivitas DashboardActivity
                    finish()
                }
            } else if (idRole == 2) {
                Toast.makeText(applicationContext, "Selamat Bekerja, Petugas Kasir $namaPengguna", Toast.LENGTH_SHORT).show()
            } else if (idRole==3) {
                Toast.makeText(applicationContext, "Selamat Bekerja, Petugas Pengantar Pesanan $namaPengguna", Toast.LENGTH_SHORT).show()
            }
        }

        // Implementasi onClickListener untuk tombol Logout
        logoutButton.setOnClickListener {
            // Kode logout di sini, misalnya menghapus data sesi atau membuka halaman login
            Toast.makeText(applicationContext, "Sedang Dalam Proses Logout..", Toast.LENGTH_SHORT).show()
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
            Toast.makeText(applicationContext, "Logout Berhasil", Toast.LENGTH_SHORT).show()
        }
    }
}
