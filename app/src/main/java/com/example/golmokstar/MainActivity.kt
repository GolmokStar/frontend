package com.example.golmokstar

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.golmokstar.fragments.*

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val bottomNavigationView = findViewById<com.google.android.material.bottomnavigation.BottomNavigationView>(R.id.bottom_navigation)

        // 기본 프래그먼트를 HomeFragment로 설정
        if (savedInstanceState == null) {
            replaceFragment(HomeFragment())
        }

        // BottomNavigationView의 클릭 이벤트 설정
        bottomNavigationView.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_home -> replaceFragment(HomeFragment())
                R.id.nav_calendar -> replaceFragment(CalendarFragment())
                R.id.nav_map -> replaceFragment(MapFragment())
                R.id.nav_history -> replaceFragment(HistoryFragment())
                R.id.nav_mypage -> replaceFragment(MypageFragment())
            }
            true
        }
    }

    // 프래그먼트를 교체하는 함수
    private fun replaceFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, fragment)
            .commit()
    }
}
