package com.example.cashup // Correct package name

import android.content.Intent
import android.os.Bundle
import android.widget.ImageButton
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import android.util.Log // Optional: for logging errors
import android.widget.Spinner

class GoalsActivity : AppCompatActivity() {

    // Declare views if you need to interact with them later (like spinners)
    private lateinit var backButton: ImageButton
    private lateinit var weeklySpinner: Spinner
    private lateinit var monthlySpinner: Spinner
    private lateinit var yearlySpinner: Spinner
    private lateinit var addGoalButton: ImageButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Set the content view to your goals XML layout
        // RENAME your XML file to "activity_goals.xml" if it's not already named that
        setContentView(R.layout.goals) // Make sure layout file is named activity_goals.xml

        // Initialize views
        backButton = findViewById(R.id.backButton) // Use the ID from your XML
        weeklySpinner = findViewById(R.id.spinner_weekly)
        monthlySpinner = findViewById(R.id.spinner_monthly)
        yearlySpinner = findViewById(R.id.spinner_yearly)
        addGoalButton = findViewById(R.id.add_goal_button)

        // Set click listener for the back button (alternative to android:onClick)
        backButton.setOnClickListener {
            finish() // Go back to the previous activity (Homepage)
        }

        // Set click listener for the add button
        addGoalButton.setOnClickListener {
            // TODO: Implement logic to add a new goal (e.g., open a dialog or new activity)
            showToast("Add Goal button clicked")
        }

        // TODO: Add logic here to populate your Spinners (weekly, monthly, yearly)
        // You'll likely need ArrayAdapter and data sources (e.g., lists of goals)
    }

    // This function is needed if you use android:onClick="onBackButtonClick" in XML
    // If you set the listener in onCreate (as shown above), you don't need this
    // fun onBackButtonClick(view: View) {
    //     finish()
    // }

    // Helper function for showing Toast messages
    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}
