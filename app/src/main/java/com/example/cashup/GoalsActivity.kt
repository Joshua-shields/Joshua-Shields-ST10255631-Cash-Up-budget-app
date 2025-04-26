package com.example.cashup

import android.content.Intent
import android.os.Bundle
import android.widget.ImageButton
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import android.util.Log
import android.widget.Spinner

class GoalsActivity : AppCompatActivity() {

    private lateinit var backButton: ImageButton
    private lateinit var weeklySpinner: Spinner
    private lateinit var monthlySpinner: Spinner
    private lateinit var yearlySpinner: Spinner
    private lateinit var addGoalButton: ImageButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Set the content view to your goals XML layout
        setContentView(R.layout.goals)

        // Initialize views
        backButton = findViewById(R.id.backButton) // Use the ID from your XML
        weeklySpinner = findViewById(R.id.spinner_weekly)
        monthlySpinner = findViewById(R.id.spinner_monthly)
        yearlySpinner = findViewById(R.id.spinner_yearly)
        addGoalButton = findViewById(R.id.add_goal_button)

        backButton.setOnClickListener {
            finish() // Go back to the previous activity (Homepage)
        }

        // Set click listener for the add button
        addGoalButton.setOnClickListener {
            showToast("Add Goal button clicked")
        }
    }

    // Helper function for showing Toast messages
    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}
