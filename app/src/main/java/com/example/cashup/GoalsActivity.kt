package com.example.cashup // Correct package name

import android.content.Intent
import android.os.Bundle
import android.widget.ImageButton
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import android.util.Log // Optional: for logging errors



class GoalsActivity : AppCompatActivity() {

    // Declare view variables
    private lateinit var backButton: ImageButton
    private lateinit var leaderboardButton: ImageButton
    private lateinit var crownButton: ImageButton
    private lateinit var addGoalButton: ImageButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        setContentView(R.layout.goals)

        // Initialize views using findViewById and the IDs from your XML
        backButton = findViewById(R.id.backButton)
        crownButton = findViewById(R.id.crownButton)
        addGoalButton = findViewById(R.id.add_goal_button)


        // Back Button: Finish this activity and return to the previous one (Homepage)
        backButton.setOnClickListener {
            finish()
        }

        // Leaderboard Button: Navigate to GamifyActivity (or a specific leaderboard screen)
        leaderboardButton.setOnClickListener {

            try {
                val intent = Intent(this, GamifyActivity::class.java)
                startActivity(intent)
            } catch (e: Exception) { // Catch ActivityNotFoundException specifically if needed
                showToast("Leaderboard screen not found!")
                Log.e("GoalsActivity", "Error starting GamifyActivity for leaderboard", e)
            }

        }

        // Crown Button: Navigate to GamifyActivity (or a specific premium/streak screen)
        crownButton.setOnClickListener {

            try {
                val intent = Intent(this, GamifyActivity::class.java)
                startActivity(intent)
            } catch (e: Exception) { // Catch ActivityNotFoundException specifically if needed
                showToast("Gamification screen not found!")
                Log.e("GoalsActivity", "Error starting GamifyActivity for crown", e)
            }
            // showToast("Crown button clicked") // Placeholder
        }

        // Add Goal Button: Navigate to a screen for creating/editing goals
        addGoalButton.setOnClickListener {

            showToast("Add Goal button clicked") // Placeholder
        }

    }

    // Helper function for showing Toast messages
    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}
