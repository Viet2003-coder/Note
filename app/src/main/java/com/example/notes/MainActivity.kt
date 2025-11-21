package com.example.notes

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val username = intent.getStringExtra("usernamelogin") ?: "Người dùng"
        val viewPg2=findViewById<ViewPager2>(R.id.vpg2)
        val tabLayout = findViewById<TabLayout>(R.id.tabLayout)
        var adapter= ViewPager2Adapter(this,username)
        viewPg2.adapter=adapter
        TabLayoutMediator(tabLayout, viewPg2) { tab, position ->
            tab.text = when (position) {
                0 -> "Home"
                1 -> "Special Notes"
                else -> "Information"
            }
        }.attach()

    }
}