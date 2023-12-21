package com.android.warmindo

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener


class MainActivity : AppCompatActivity() {

    private lateinit var usernameEditText: EditText
    private lateinit var passwordEditText: EditText
    private lateinit var loginButton: Button
    private lateinit var database: DatabaseReference

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
            if (username.isEmpty() || password.isEmpty()) {
                Toast.makeText(applicationContext, "Username Atau Password Belum Terisi", Toast.LENGTH_SHORT).show()
            } else {
                database.orderByChild("username").equalTo(username).addListenerForSingleValueEvent(object : ValueEventListener {
                        override fun onDataChange(snapshot: DataSnapshot) {
                            if (snapshot.exists()) {
                                // User dengan username tersebut ditemukan, dilakukan pengecekan password
                                for (userSnapshot in snapshot.children) {

                                    val storedPassword = userSnapshot.child("password").getValue(String::class.java)
                                    val status = userSnapshot.child("status").getValue(String::class.java)
                                    val idRole = userSnapshot.child("idrole").getValue(Int::class.java)

                                    if (storedPassword == password && status == "AKTIF" && (idRole == 2 || idRole == 3 || idRole == 4)) {
                                        Toast.makeText(applicationContext, "Login Berhasil", Toast.LENGTH_SHORT).show()
                                        val intent = Intent(applicationContext, DashboardActivity::class.java)
                                        intent.putExtra("namapengguna", userSnapshot.child("namapengguna").getValue(String::class.java))
                                        intent.putExtra("idrole", idRole)
                                        startActivity(intent)
                                    } else {
                                        if (status != "AKTIF") {
                                            Toast.makeText(applicationContext, "Akun Tidak Aktif", Toast.LENGTH_SHORT).show()
                                        } else if (idRole == 1) {
                                            Toast.makeText(applicationContext, "Mengarahkan Pengguna ke Halaman Dashboard Pemilik..", Toast.LENGTH_SHORT).show()
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