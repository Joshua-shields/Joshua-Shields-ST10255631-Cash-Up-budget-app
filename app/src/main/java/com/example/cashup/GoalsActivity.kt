package com.example.cashup

import android.content.Intent
import android.os.Bundle
import android.util.Log // <-- Make sure this import is present
import android.widget.ImageButton
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import android.widget.Spinner

class GoalsActivity : AppCompatActivity() {

    // ... (other variables if you have them)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.goals)

        val backButton: ImageButton = findViewById(R.id.backButton)
        val weeklySpinner: Spinner = findViewById(R.id.spinner_weekly)
        val monthlySpinner: Spinner = findViewById(R.id.spinner_monthly)
        val yearlySpinner: Spinner = findViewById(R.id.spinner_yearly)
        val addGoalButton: ImageButton = findViewById(R.id.add_goal_button)

        Log.d("GoalsActivity", "Activity created. Setting up listeners.") // Log activity start

        backButton.setOnClickListener {
            Log.d("GoalsActivity", "Back button clicked.") // Log back button click
            finish()
        }

        addGoalButton.setOnClickListener {
            Log.d("GoalsActivity", "Add Goal Button Clicked!") // check if button works

            try {
                val targetClass = CreateGoalActivity::class.java
                Log.d("GoalsActivity", "CreateGoalActivity class found: ${targetClass.name}") // check class reference
            } catch (e: NoClassDefFoundError) {
                Log.e("GoalsActivity", "ERROR: CreateGoalActivity class NOT found!", e)
                showToast("Error: CreateGoalActivity class missing")
                return@setOnClickListener // Stop here if class is missing
            } catch (e: Exception) {
                Log.e("GoalsActivity", "ERROR: Could not reference CreateGoalActivity class", e)
                showToast("Error referencing CreateGoalActivity")
                return@setOnClickListener
            }

            val intent = Intent(this, CreateGoalActivity::class.java)
            Log.d("GoalsActivity", "Intent created for CreateGoalActivity")

            try {
                startActivity(intent)
                Log.d("GoalsActivity", "startActivity(intent) called successfully.")
            } catch (e: Exception) {
                // Log any exception during startActivity
                Log.e("GoalsActivity", "ERROR starting CreateGoalActivity!", e) // Check for errors
                showToast("Error opening Create Goal screen: ${e.message}")
            }

        }

    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show()
    }
}
