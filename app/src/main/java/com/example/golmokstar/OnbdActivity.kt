package com.example.golmokstar

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class OnbdActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_onbd)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // 온보딩 완료 버튼 클릭 시 MainActivity로 이동
        findViewById<Button>(R.id.btn_start).setOnClickListener {
            // 온보딩 완료 상태 저장
            val sharedPref = getSharedPreferences("AppPreferences", MODE_PRIVATE)
            with(sharedPref.edit()) {
                putBoolean("OnboardingCompleted", true)
                apply()
            }

            // MainActivity로 이동
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }
    }
}