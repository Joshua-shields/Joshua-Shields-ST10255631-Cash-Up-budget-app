
//---------------------------------- START OF FILE ------------------------------//
package com.example.cashup.com.example.cashup



//************************ START OF IMPORTS**********************//

import android.os.Bundle // STORES DATA

import android.widget.Button // ALLOWS FOR CLICKABLE BUTTONS

import android.widget.EditText // ALLOWS USERS TO EDIT TEXT THROUGH NEW INPUT

import android.widget.ImageButton // ALLOWS FOR IMAGES TO BE USED AS BUTTONS

import android.widget.Toast // ALLOWS MESSAGES TO DISPLAY TO USERS SUCH AS NOTIFICATIONS, CONFIRMATIONS OR ERRORS

import androidx.appcompat.app.AppCompatActivity

import com.example.cashup.R // GRANTS ACCESS TO THE APP DATABASE RESOURCES

//*************************** END OF IMPORTS  *************************//

class ForgotPasswordActivity : AppCompatActivity() {

    //******************** START OF GLOBAL VARIABLES **********************//

    private lateinit var usernameInput: EditText

    private lateinit var newPasswordInput: EditText

    private lateinit var confirmPasswordInput: EditText

    private lateinit var resetButton: Button

    private lateinit var backButton: ImageButton

    //******************** END OF GLOBAL VARIABLES *******************************//


    // OVERRIDE FUNCTION
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        initializeViews() // SET UP OF VIEWS

        setupClickListeners() // SET UP OF LISTENERS
    }


    /*
    * THE BELOW CODE IS SETTING UP THE VIEWS FOR:
    * USERNAME
    * CONFIRM PASSWORD
    * BACK BUTTON
    * ------------------------ REQUIRED ---------------
    * PASSWORD
    *
    * */
    private fun initializeViews() {

        usernameInput = findViewById(R.id.username_input)

        confirmPasswordInput = findViewById(R.id.confirm_password_input)

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

/*
*
* BELOW IS THE ERROR HANDLING AS USER INPUT IS A SOURCE OF CODDE BREAKING
*
* ----------------- CHECKING FOR -------------------
* USERNAEM
* USERNAME LENGTH
* EMPTY USERNAME
*
* EMPTY PASSWORD
* NEW PASSWORD NOT EQUAL TO PRIOR PASSWORD
*
* */
    private fun validateInputs(username: String, newPassword: String, confirmPassword: String): Boolean {
        if (username.isEmpty()) {
            showMessage("Please enter your username") // checking for an empty username with added error message
            usernameInput.requestFocus()
            return false
        }

        if (username.length < 2) {
            showMessage("Username is too short") // checking for valid username length with added error message
            usernameInput.requestFocus()
            return false
        }

        if (newPassword.isEmpty()) {
            showMessage("Please enter a new password") // CHECKING FOR NEW PASSWORD WITH ERROR MESSAGE
            newPasswordInput.requestFocus()
            return false
        }

        if (newPassword.length < 6) {
            showMessage("Password must be at least 6 characters long") // CHECKING FOR VALID PASSWORD LENGTH
            newPasswordInput.requestFocus()
            return false
        }

        if (confirmPassword.isEmpty()) {
            showMessage("Please confirm your new password") // CHECKING FOR NEW PASSWORD
            confirmPasswordInput.requestFocus()
            return false
        }

        if (newPassword != confirmPassword) {
            showMessage("Passwords do not match") // MAKING SURE THE PASSWORD IS NOT EXISTING IN THE DATABASE
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
//----------------------------------- END OF FILE ---------------------------------------//