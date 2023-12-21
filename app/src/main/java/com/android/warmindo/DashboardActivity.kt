package com.android.warmindo

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import android.widget.Toast

class DashboardActivity : AppCompatActivity() {

    private lateinit var welcomeText: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dashboard)

        welcomeText = findViewById(R.id.welcome_text)

        // Ambil data dari Intent yang dikirim dari aktivitas sebelumnya (MainActivity)
        val namaPengguna = intent.getStringExtra("namapengguna")
        val idRole = intent.getIntExtra("idrole", 0)
        val mulaiShiftButton: Button = findViewById(R.id.mulai_shift_button)
        val logoutButton: Button = findViewById(R.id.logout_button)

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
                val intent = Intent(applicationContext, TransactionActivity::class.java)
                startActivity(intent)
                // Menutup aktivitas DashboardActivity
                finish()
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
            val intent = Intent(applicationContext, MainActivity::class.java)
            startActivity(intent)
            // Menutup aktivitas DashboardActivity
            finish()
            Toast.makeText(applicationContext, "Logout Berhasil", Toast.LENGTH_SHORT).show()
        }
    }
}
