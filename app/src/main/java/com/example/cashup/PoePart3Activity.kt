package com.example.cashup

import android.os.Bundle
import android.widget.ImageButton
import androidx.appcompat.app.AppCompatActivity

class PoePart3Activity : AppCompatActivity() {

    private lateinit var backButton: ImageButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_poe_part3_stats)

        // Initialize back button
        backButton = findViewById(R.id.back_button_poe)

        // Set up back button click listener
        backButton.setOnClickListener {
            finish() // Close this activity and return to previous screen
        }
    }
}
//***************************************************** END OF CODE ***********************************************************//