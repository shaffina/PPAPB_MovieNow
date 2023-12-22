package com.example.movienow

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.movienow.databinding.ActivitySignupBinding
import com.google.firebase.auth.FirebaseAuth

class RegisterFragment : AppCompatActivity() {

    private lateinit var binding: ActivitySignupBinding
    private lateinit var firebaseAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignupBinding.inflate(layoutInflater)
        setContentView(binding.root)

        firebaseAuth = FirebaseAuth.getInstance()

        binding.regisbutton.setOnClickListener {
            val email = binding.textemail.text.toString()
            val password = binding.textpassword.text.toString()
            val confirm = binding.confirmpassword.text.toString()

            if (email.isNotEmpty() && password.isNotEmpty() && confirm.isNotEmpty()){
                if(password==confirm){

                    firebaseAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener{
                        if(it.isSuccessful){
                            val intent = Intent(this,LoginActivity::class.java)
                            startActivity(intent)
                        }
                        else{
                            Toast.makeText(this, "Data Tidak Sesuai", Toast.LENGTH_SHORT).show()
                        }
                    }
                }
                else{
                    Toast.makeText(this, "Password Tidak Cocok", Toast.LENGTH_SHORT).show()
                }
            } else{
                Toast.makeText(this, "Data Tidak Boleh kosong", Toast.LENGTH_SHORT).show()
            }
        }

        binding.textlogin.setOnClickListener {
            val loginIntent = Intent(this,LoginFragment::class.java)
            startActivity(loginIntent)
        }
    }
}