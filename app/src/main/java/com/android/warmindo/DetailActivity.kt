package com.android.warmindo

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class DetailActivity : AppCompatActivity() {

    private lateinit var dbref : DatabaseReference
    private lateinit var userRecyclerview : RecyclerView
    private lateinit var userArrayList : ArrayList<DetailModel>

    companion object {
        const val  KEY_NAMA_PELANGGAN = "key_nama"
        const val KEY_KODE_MEJA = "key_meja"
        const val KEY_TANGGAL = "key_tgl"
        const val KEY_TRANSACTION_ID = "key_id"
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)

        // Temukan tombol back berdasarkan ID
        val backButton = findViewById<ImageView>(R.id.backButton)
        backButton.setOnClickListener {
            // Buat Intent untuk berpindah ke TransactionActivity
            val intent = Intent(this, TransactionActivity::class.java)
            startActivity(intent)
        }

        val detailPesananTitle = findViewById<TextView>(R.id.detailPesananTitle)
        val descriptionTextView = findViewById<TextView>(R.id.descriptionTextView)
        val transactionIdTextView = findViewById<TextView>(R.id.transactionIdTextView)

        // Mendapatkan data dari intent
        val namapelanggan = intent.getStringExtra(KEY_NAMA_PELANGGAN)
        val kodemeja = intent.getStringExtra(KEY_KODE_MEJA)
        val tanggal = intent.getStringExtra(KEY_TANGGAL)
        val idtransaksi = intent.getStringExtra(KEY_TRANSACTION_ID)

        // Menampilkan data di layout
        detailPesananTitle.text = "Detail Transaksi $namapelanggan"
        descriptionTextView.text = "Kode Meja: $kodemeja\nTanggal: $tanggal"
        transactionIdTextView.text = "ID Transaksi: $idtransaksi"

        val tandaiSelesaiButton = findViewById<Button>(R.id.mulai_shift_button)
        tandaiSelesaiButton.setOnClickListener {
            // Mendapatkan ID transaksi dari intent
            val idtransaksi = intent.getStringExtra(KEY_TRANSACTION_ID)

            // Perbarui status transaksi menjadi "DIPROSES" di Firebase Database
            updateTransactionStatus(idtransaksi)
        }

        userRecyclerview = findViewById(R.id.recyclerViewOrderItems)
        userRecyclerview.layoutManager = LinearLayoutManager(this)
        userRecyclerview.setHasFixedSize(true)

        userArrayList = arrayListOf<DetailModel>()

        if (idtransaksi != null) {
            getUserData(idtransaksi)
        }
    }

    private fun getUserData(idTransaksi: String) {
        dbref = FirebaseDatabase.getInstance().getReference("detail_transaksi")

        // Gunakan method orderByChild untuk menyusun data berdasarkan field "idtransaksi"
        dbref.orderByChild("idtransaksi").equalTo(idTransaksi)
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.exists()) {
                        userArrayList.clear() // Bersihkan list sebelum menambahkan data baru

                        for (userSnapshot in snapshot.children) {
                            // Ambil ID transaksi dari data detail_transaksi
                            val idTransaksiDetail = userSnapshot.child("idtransaksi").getValue(String::class.java)

                            // Cek apakah ID transaksi sama dengan yang dipilih di TransactionActivity
                            if (idTransaksi == idTransaksiDetail) {
                                val user = userSnapshot.getValue(DetailModel::class.java)
                                userArrayList.add(user!!)
                            }
                        }
                        userRecyclerview.adapter = DetailAdapter(userArrayList)
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    // Handle error
                }
            })
    }


    private fun updateTransactionStatus(idtransaksi: String?) {
        if (idtransaksi != null) {
            // Dapatkan referensi ke Firebase Database
            val dbref = FirebaseDatabase.getInstance().getReference("transaksi")

            // Perbarui status transaksi menjadi "DIPROSES"
            dbref.child(idtransaksi).child("status").setValue("DIPROSES")

            // Tampilkan pesan atau lakukan tindakan lain jika diperlukan
            Toast.makeText(this, "Transaksi telah ditandai sebagai selesai diproses", Toast.LENGTH_SHORT).show()

            // Intent ke TransactionActivity
            val intent = Intent(this, TransactionActivity::class.java)
            startActivity(intent)
        }
    }
}