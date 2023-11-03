package com.example.firebaseauthtest

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.firebaseauthtest.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    lateinit var binding : ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.register.setOnClickListener{
            startActivity(Intent(this@MainActivity , SignUp::class.java))
        }

        binding.login.setOnClickListener{
            startActivity(Intent(this@MainActivity , Login::class.java))
        }


    }
}