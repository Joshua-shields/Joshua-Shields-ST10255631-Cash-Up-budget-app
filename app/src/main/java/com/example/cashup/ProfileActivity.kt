package com.example.cashup


import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class ProfileActivity : AppCompatActivity() {

    // Declare view variables
    private lateinit var backButton: ImageButton
    private lateinit var usernameEditText: EditText
    private lateinit var streakEditText: EditText
    private lateinit var goalsEditText: EditText
    private lateinit var contactUsButton: Button
    // Add other views if needed

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // *** SET THE CORRECT LAYOUT ***
        // Use the name you saved the profile XML file as.
        setContentView(R.layout.activity_profile) // ASSUMING the XML is named activity_profile.xml

        // Initialize views using findViewById
        backButton = findViewById(R.id.back_button_profile) // Use the ID from your XML
        usernameEditText = findViewById(R.id.viewusername)
        streakEditText = findViewById(R.id.streaknum)
        goalsEditText = findViewById(R.id.goalsachived)
        contactUsButton = findViewById(R.id.contacttext) // Example

        // --- Setup Click Listeners ---

        // Back Button: Finish this activity and return to the previous one (Homepage)
        backButton.setOnClickListener {
            finish() // Closes the current activity
        }

        // TODO: Add listeners for contact buttons or other interactive elements
        contactUsButton.setOnClickListener {
            showToast("Contact Us clicked") // Placeholder
        }

        // TODO: Load actual user data into the EditText fields
        // For now, they will just show the hint text from the XML
        // Example:
        // usernameEditText.setText("ActualUsername")
        // streakEditText.setText("5 months")
        // goalsEditText.setText("12 Goals Achieved")

        // Make EditText fields non-editable if they are just for display
        usernameEditText.isFocusable = false
        usernameEditText.isClickable = false
        streakEditText.isFocusable = false
        streakEditText.isClickable = false
        goalsEditText.isFocusable = false
        goalsEditText.isClickable = false

    }

    // Helper function for showing Toast messages
    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}
