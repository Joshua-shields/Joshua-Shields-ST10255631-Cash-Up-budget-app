package com.example.cashup


import android.os.Bundle
import android.widget.ImageButton
import androidx.appcompat.app.AppCompatActivity

class CreateGoalActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_goal)

        val backButton: ImageButton = findViewById(R.id.back_button)

        backButton.setOnClickListener {
            finish() // Close this activity and go back to GoalsActivity
        }


    }
}
