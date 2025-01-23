package com.example.golmokstar.auth

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import com.example.golmokstar.R

class SplashActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        // 3초 후 LoginActivity로 이동
        window.decorView.postDelayed({
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }, 3000)
    }
}
