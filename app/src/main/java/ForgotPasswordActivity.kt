package com.example.cashup



//************************ start of imports **********************//
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

//*************************** end of imports *************************//

class ForgotPasswordActivity : AppCompatActivity() {

    private lateinit var usernameInput: EditText

    private lateinit var newPasswordInput: EditText

    private lateinit var confirmPasswordInput: EditText

    private lateinit var resetButton: Button

    private lateinit var backButton: ImageButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_forgot_password)

        initializeViews()

        setupClickListeners()
    }

    private fun initializeViews() {
        usernameInput = findViewById(R.id.username_input)

        newPasswordInput = findViewById(R.id.new_password_input1)

        confirmPasswordInput = findViewById(R.id.confirm_password_input)

        resetButton = findViewById(R.id.reset_button1)

        backButton = findViewById(R.id.back_button)
    }

    private fun setupClickListeners() {
        resetButton.setOnClickListener {
            val username = usernameInput.text.toString().trim()

            val newPassword = newPasswordInput.text.toString().trim()

            val confirmPassword = confirmPasswordInput.text.toString().trim()

            if (validateInputs(username, newPassword, confirmPassword)) {
                resetPassword(username, newPassword)
            }
        }

        backButton.setOnClickListener {
            finish() // Go back to previous screen
        }
    }

    private fun validateInputs(username: String, newPassword: String, confirmPassword: String): Boolean {
        if (username.isEmpty()) {
            showMessage("Please enter your username")
            usernameInput.requestFocus()
            return false
        }

        if (username.length < 2) {
            showMessage("Username is too short")
            usernameInput.requestFocus()
            return false
        }

        if (newPassword.isEmpty()) {
            showMessage("Please enter a new password")
            newPasswordInput.requestFocus()
            return false
        }

        if (newPassword.length < 6) {
            showMessage("Password must be at least 6 characters long")
            newPasswordInput.requestFocus()
            return false
        }

        if (confirmPassword.isEmpty()) {
            showMessage("Please confirm your new password")
            confirmPasswordInput.requestFocus()
            return false
        }

        if (newPassword != confirmPassword) {
            showMessage("Passwords do not match")
            confirmPasswordInput.requestFocus()
            return false
        }

        return true
    }

    private fun resetPassword(username: String, newPassword: String) {
       /*
       *
       *
       *
       * we need to link this information to the database
       *
       * hardcoded for now
       *
       *
       *
       * */
        if (username == "testuser") {
            showMessage("Password reset successful!")


            // Return to login screen after successful reset
            finish()
        } else {
            showMessage("Username not found")
        }
    }

    private fun showMessage(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}