package com.example.golmokstar

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class SplashActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_splash)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        // 2초 후 다음 화면으로 이동
        Handler(Looper.getMainLooper()).postDelayed({
            /*
            val sharedPref = getSharedPreferences("AppPreferences", MODE_PRIVATE)
            val isOnboardingCompleted = sharedPref.getBoolean("OnboardingCompleted", false)

            if (isOnboardingCompleted) {
                // 온보딩 완료 → MainActivity로 이동
                startActivity(Intent(this, MainActivity::class.java))
            } else {
                // 온보딩 미완료 → OnbdActivity로 이동
                startActivity(Intent(this, OnbdActivity::class.java))
            }
             */
            startActivity(Intent(this, OnbdActivity::class.java))
            finish() // SplashActivity 종료
        }, 2000) // 2초 대기
    }
}