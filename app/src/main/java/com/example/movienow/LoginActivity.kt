package com.example.movienow

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.movienow.databinding.ActivityLoginBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var firestore: FirebaseFirestore
    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        firebaseAuth = FirebaseAuth.getInstance()
        firestore = FirebaseFirestore.getInstance()
        sharedPreferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)

        binding.loginbutton.setOnClickListener {
            val email = binding.email.text.toString()
            val password = binding.password.text.toString()

            if (email.isNotEmpty() && password.isNotEmpty()) {
                // Cek apakah pengguna adalah admin
                checkAdminLogin(email, password)


            } else {
                Toast.makeText(this, "Data Tidak Boleh Kosong", Toast.LENGTH_SHORT).show()
            }
        }

        binding.textregis.setOnClickListener {
            val regisIntent = Intent(this, SignupActivity::class.java)
            startActivity(regisIntent)
        }
    }

    private fun checkAdminLogin(email: String, password: String) {
        val adminCollectionRef = firestore.collection("admin")

        // Query untuk mencari admin dengan email yang sesuai
        val query = adminCollectionRef.whereEqualTo("email", email)

        query.get().addOnCompleteListener { task ->
            if (task.isSuccessful) {
                val adminDocs = task.result?.documents

                // Jika ditemukan admin dengan email yang sesuai
                if (!adminDocs.isNullOrEmpty()) {
                    val adminData = adminDocs[0].data

                    // Periksa apakah password cocok
                    if (adminData?.get("password") == password) {
                        // Jika cocok, izinkan akses ke aplikasi sebagai admin
                        saveLoginStatus(true)
                        Toast.makeText(this, "Login berhasil sebagai admin", Toast.LENGTH_SHORT).show()
                        val intent = Intent(this, AdminActivity::class.java)
                        startActivity(intent)

                        // clear fields
                        binding.email.text.clear()
                        binding.password.text.clear()
                    } else {
                        Toast.makeText(this, "Data Tidak Sesuai", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    // Jika tidak ditemukan admin, lakukan login sebagai pengguna biasa
                    saveLoginStatus(false)
                    loginUser(email, password)
                }
            } else {
                Toast.makeText(this, "Gagal melakukan login", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun loginUser(email: String, password: String) {
        firebaseAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                // Jika login berhasil, izinkan akses ke aplikasi
                saveLoginStatus(false)
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)

                // clear fields
                binding.email.text.clear()
                binding.password.text.clear()
            } else {
                Toast.makeText(this, "Data Tidak Sesuai", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun saveLoginStatus(isAdmin: Boolean) {
        val editor = sharedPreferences.edit()
        editor.putBoolean("isAdmin", isAdmin)
        editor.apply()
    }
}
