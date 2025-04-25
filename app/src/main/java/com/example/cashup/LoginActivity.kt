//***************************** start of login backend code **************************************//

package com.example.cashup

import android.content.Intent
import android.os.Bundle
import android.widget.Button // allows for clickable buttons to be coded
import android.widget.EditText // allows for text to be edited  (input fields )
import android.widget.Toast // needed for pop up messages
import androidx.appcompat.app.AppCompatActivity


import android.widget.TextView

class LoginActivity : AppCompatActivity() {

    private lateinit var usernameInput: EditText
    private lateinit var passwordInput: EditText
    private lateinit var loginButton: Button
    private lateinit var forgotPasswordButton: Button


    private lateinit var registerLinkButton: Button // button variable to be used to redirect "register now " feature

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login) // Make sure this matches your login XML filename

        val forgotPasswordText: TextView = findViewById(R.id.forgotpassword)


        initializeViews()
        setupClickListeners()
    }

    private fun initializeViews() {
        usernameInput = findViewById(R.id.username_input)
        passwordInput = findViewById(R.id.password_input)
        loginButton = findViewById(R.id.login_button)
        forgotPasswordButton = findViewById(R.id.forgotpassword)

        registerLinkButton = findViewById(R.id.register_link_button) // initialization of new button "register now"
    }

    private fun setupClickListeners() {

        loginButton.setOnClickListener {
            val username = usernameInput.text.toString().trim()
            val password = passwordInput.text.toString().trim()

            if (validateInputs(username, password)) {
                performLogin(username, password)
            }
        }

        forgotPasswordButton.setOnClickListener {  // requires logic

            showMessage("Forgot Password Clicked (Implement Navigation)")
        }

        registerLinkButton.setOnClickListener {
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
        }
    }

    private fun validateInputs(username: String, password: String): Boolean {
        if (username.isEmpty()) {
            showMessage("You Need to enter Your Username ")
            usernameInput.requestFocus()
            return false
        }
        if (username.length < 2) {
            showMessage("Username too Short, Enter A longer one")
            usernameInput.requestFocus()
            return false
        }
        if (password.isEmpty()) {
            showMessage("Enter a Password")
            passwordInput.requestFocus()
            return false
        }

        if (password.length < 4) {
            showMessage("Password must be at least 4 characters long")
            passwordInput.requestFocus()
            return false
        }
        return true
    }

    private fun performLogin(username: String, password: String) {
        if (authenticate(username, password)) {
            // Successful login
            showMessage("Login successful")

            // --- NAVIGATE TO HOMEPAGE ---
            val homeIntent = Intent(this, HomepageActivity::class.java)
            startActivity(homeIntent) // Start HomepageActivity
            // --- END NAVIGATION ---

            finish() // Close LoginActivity so the user can't go back to it with the back button
        } else {
            showMessage("Invalid username or password")
        }
    }

    private fun authenticate(username: String, password: String): Boolean {

        if (username == "testuser" && password == "pass1234") {
            return true
        }

        return false
    }

    private fun showMessage(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}

//********************************** end of login backend code *****************************//
