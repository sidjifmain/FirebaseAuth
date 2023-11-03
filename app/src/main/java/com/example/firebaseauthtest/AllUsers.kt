package com.example.firebaseauthtest

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.example.firebaseauthtest.databinding.ActivityAllUsersBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class AllUsers : AppCompatActivity() {
    lateinit var binding : ActivityAllUsersBinding
    private lateinit var firebaseAuth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAllUsersBinding.inflate(layoutInflater)
        setContentView(binding.root)



        val db = FirebaseFirestore.getInstance()
        firebaseAuth = FirebaseAuth.getInstance()
        val usersRef = db.collection("users")

        usersRef.get()
            .addOnSuccessListener { documents ->
                val userList = ArrayList<UserModel>() // Firestore verilerini saklayacak liste

                for (document in documents) {
                    val name = document.getString("name")
                    val lastName = document.getString("last_name")
                    val age = document.getString("age")

                    val user = UserModel(name, lastName, age)
                    userList.add(user)
                }

                val userInformation = StringBuilder()
                for (user in userList) {
                    userInformation.append("Name: ${user.name}\n")
                    userInformation.append("Last Name: ${user.lastName}\n")
                    userInformation.append("Age: ${user.age}\n\n")
                }

                binding.allUsers.text = userInformation.toString()
            }
            .addOnFailureListener { exception ->
                Log.e("Firestore Hatası", "Veri çekme hatası: $exception")
            }

        binding.logout.setOnClickListener{
            firebaseAuth.signOut()
            startActivity(Intent(this@AllUsers , MainActivity::class.java))
            finish()
        }



    }
}