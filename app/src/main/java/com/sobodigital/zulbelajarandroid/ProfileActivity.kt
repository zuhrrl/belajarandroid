package com.sobodigital.zulbelajarandroid

import android.os.Bundle
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity

class ProfileActivity : AppCompatActivity() {
    private lateinit var btnHome: ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)

        btnHome = findViewById(R.id.btn_home)

        btnHome.setOnClickListener {
           this.onBackPressed()
        }

    }
}