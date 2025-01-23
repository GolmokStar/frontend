package com.example.golmokstar.auth

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import com.example.golmokstar.R
import com.example.golmokstar.databinding.ActivityLoginBinding

class LoginActivity : AppCompatActivity() {

    private lateinit var binding : ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_login)

        binding.nextBTN.setOnClickListener{
            val intent = Intent(this, OnboardingActivity::class.java)
            startActivity(intent)
        }
    }
}