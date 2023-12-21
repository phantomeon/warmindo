package com.android.warmindo

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import java.text.DateFormat
import java.util.Date
import java.util.Locale

class MainActivity : AppCompatActivity() {

    private lateinit var usernameEditText: EditText
    private lateinit var passwordEditText: EditText
    private lateinit var loginButton: Button
    private lateinit var database: DatabaseReference
    private lateinit var databaseAct: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        usernameEditText = findViewById(R.id.email)
        passwordEditText = findViewById(R.id.password)
        loginButton = findViewById(R.id.button_login)

        loginButton.setOnClickListener {
            val username: String = usernameEditText.text.toString()
            val password: String = passwordEditText.text.toString()
            database = FirebaseDatabase.getInstance().getReference("pengguna")
            databaseAct = FirebaseDatabase.getInstance().getReference("aktivitas_pengguna")
            if (username.isEmpty() || password.isEmpty()) {
                Toast.makeText(applicationContext, "Username Atau Password Belum Terisi", Toast.LENGTH_SHORT).show()
            } else {
                database.orderByChild("username").equalTo(username).addListenerForSingleValueEvent(object : ValueEventListener {
                        override fun onDataChange(snapshot: DataSnapshot) {
                            if (snapshot.exists()) {
                                // User dengan username tersebut ditemukan
                                for (userSnapshot in snapshot.children) {

                                    // Variabels untuk menampung atribut dari data dengan username terkait
                                    val idPengguna = userSnapshot.child("idpengguna").getValue(String::class.java)
                                    val storedPassword = userSnapshot.child("password").getValue(String::class.java)
                                    val namaPengguna = userSnapshot.child("namapengguna").getValue(String::class.java)
                                    val idRole = userSnapshot.child("idrole").getValue(Int::class.java)
                                    val status = userSnapshot.child("status").getValue(String::class.java)

                                    // Pengecekan password, status, dan idrole
                                    if (storedPassword == password && status == "AKTIF" && (idRole == 2 || idRole == 3 || idRole == 4)) {
                                        Toast.makeText(applicationContext, "Sedang Menghubungkan Pengguna...", Toast.LENGTH_SHORT).show()

                                        val idaktivitas = databaseAct.push().key // Menggunakan push().key untuk mendapatkan ID baru secara otomatis
                                        if (idaktivitas != null) {
                                            // Dapatkan tanggal dan waktu saat ini
                                            val currentDateTime = Date()
                                            // Dapatkan formatter untuk tanggal dan waktu dengan format lokal
                                            val dateFormatter = DateFormat.getDateInstance(DateFormat.LONG, Locale.getDefault())
                                            val timeFormatter = DateFormat.getTimeInstance(DateFormat.SHORT, Locale.getDefault())
                                            // Format tanggal dan waktu
                                            val formattedDate = dateFormatter.format(currentDateTime)
                                            val formattedTime = timeFormatter.format(currentDateTime)

                                            // Intent data ke page setelahnya
                                            val intent = Intent(applicationContext, DashboardActivity::class.java)
                                            intent.putExtra("namapengguna", namaPengguna)
                                            intent.putExtra("idrole", idRole)
                                            intent.putExtra("idpengguna", idPengguna)
                                            startActivity(intent)

                                            // Menambahkan aktivitas Login untuk pengguna terkait ke aktivitas_pengguna
                                            databaseAct.child(idaktivitas).child("aktivitas").setValue("LOGIN")
                                            databaseAct.child(idaktivitas).child("idpengguna").setValue(idPengguna)
                                            databaseAct.child(idaktivitas).child("idaktivitas").setValue(idaktivitas)
                                            databaseAct.child(idaktivitas).child("tanggal").setValue(formattedDate)
                                            databaseAct.child(idaktivitas).child("waktu").setValue(formattedTime)
                                        } else {
                                            Toast.makeText(applicationContext, "Gagal Menambahkan Log Aktivitas, Harap Hubungi Pemilik", Toast.LENGTH_SHORT).show()
                                        }

                                        Toast.makeText(applicationContext, "Login Berhasil", Toast.LENGTH_SHORT).show()
                                    } else {
                                        if (status != "AKTIF") {
                                            Toast.makeText(applicationContext, "Akun Tidak Aktif", Toast.LENGTH_SHORT).show()
                                        } else if (idRole == 1) {
                                            Toast.makeText(applicationContext, "Mengarahkan Pengguna ke Halaman Dashboard Pemilik...", Toast.LENGTH_SHORT).show()
                                        } else if (idRole != 1) {
                                            Toast.makeText(applicationContext, "Akun Tidak Memiliki Hak Akses", Toast.LENGTH_SHORT).show()
                                        }else {
                                            Toast.makeText(applicationContext, "Password Salah", Toast.LENGTH_SHORT).show()
                                        }
                                    }
                                }
                            } else {
                                Toast.makeText(applicationContext, "Email atau Username Salah", Toast.LENGTH_SHORT).show()
                            }
                        }

                        override fun onCancelled(error: DatabaseError) {
                            // Handle error
                        }
                    })
            }
        }
    }
}