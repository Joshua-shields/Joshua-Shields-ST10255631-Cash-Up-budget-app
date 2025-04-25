package com.example.cashup

import android.os.Bundle
import android.widget.ImageButton
import androidx.appcompat.app.AppCompatActivity

class GamifyActivity : AppCompatActivity() {

    // Declare view variable for the back button
    private lateinit var backButton: ImageButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        setContentView(R.layout.gamify_streak_leaderboard)

        // Initialize the back button using its ID from the XML
        backButton = findViewById(R.id.back_button)

        // --- Setup Click Listener for the Back Button ---
        backButton.setOnClickListener {

            finish()
        }

        // You can add listeners for tabs or other elements here later if needed
    }
}
