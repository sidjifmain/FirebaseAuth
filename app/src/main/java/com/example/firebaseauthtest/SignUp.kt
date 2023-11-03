package com.example.firebaseauthtest

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import com.example.firebaseauthtest.databinding.ActivitySignUpBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class SignUp : AppCompatActivity() {
    lateinit var binding: ActivitySignUpBinding
    private lateinit var firebaseAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignUpBinding.inflate(layoutInflater)
        setContentView(binding.root)

        firebaseAuth = FirebaseAuth.getInstance()
        val db = FirebaseFirestore.getInstance()

        binding.button.setOnClickListener {
            val email = binding.emailEt.text.toString()
            val pass = binding.passET.text.toString()
            val confirmPass = binding.confirmPassEt.text.toString()
            val name = binding.name.text.toString()
            val surname = binding.surname.text.toString()
            val age = binding.age.text.toString()

            if (email.isNotEmpty() && pass.isNotEmpty() && confirmPass.isNotEmpty()) {
                if (pass == confirmPass) {
                    // Kayıt işlemi başlamadan önce ProgressBar'ü görünür hale getirin
                    binding.progressBar.visibility = View.VISIBLE
                    binding.button.isEnabled = false // Butonu devre dışı bırak

                    firebaseAuth.createUserWithEmailAndPassword(email, pass).addOnCompleteListener { it ->
                        // Kayıt işlemi tamamlandığında ProgressBar'ü gizle ve butonu etkinleştir
                        binding.progressBar.visibility = View.GONE
                        binding.button.isEnabled = true

                        if (it.isSuccessful) {
                            val user = hashMapOf(
                                "email" to email,
                                "name" to name,
                                "last_name" to surname,
                                "age" to age
                            )

                            db.collection("users").document(it.result.user!!.uid)
                                .set(user)
                                .addOnSuccessListener {
                                    val intent = Intent(this, AllUsers::class.java)
                                    startActivity(intent)
                                    finish()
                                }
                                .addOnFailureListener { exception ->
                                    Log.e("Firestore Hatası", "Hata: $exception")
                                    Toast.makeText(this, "Firestore hatası: $exception", Toast.LENGTH_SHORT).show()
                                }
                        } else {
                            Toast.makeText(this, it.exception.toString(), Toast.LENGTH_SHORT).show()
                        }
                    }
                } else {
                    Toast.makeText(this, "Password is not matching", Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(this, "Empty Fields Are not Allowed !!", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
