package com.example.cashup.com.example.cashup

import android.content.Intent
import android.os.Bundle
import android.widget.ImageButton
import androidx.appcompat.app.AppCompatActivity
import com.example.cashup.HomepageActivity
import com.example.cashup.R

class ProfileActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)


        val backButton = findViewById<ImageButton>(R.id.back_button_profile)


        backButton.setOnClickListener {

            val intent = Intent(this, HomepageActivity::class.java)
            startActivity(intent)

            finish()
        }
    }
}