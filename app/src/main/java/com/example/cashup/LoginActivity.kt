//***************************** start of login backend code **************************************//

package com.example.cashup

import android.os.Bundle

import androidx.appcompat.app.AppCompatActivity

import android.widget.Button // allows for clickable buttons to be coded

import android.widget.EditText // allows for text to be edited  (input fields )

import android.widget.Toast // needed for pop up messages

class LoginActivity : AppCompatActivity() { // Needs to inherit from AppCompatActivity



    // ************ Start of Declaration of UI Components *********** //



    private fun initializeViews() {

        usernameInput = findViewById(R.id.username_input)

        passwordInput = findViewById(R.id.password_input)

        loginButton = findViewById(R.id.login_button)

        forgotPasswordButton = findViewById(R.id.forgotpassword)
    }

    private fun setupClickListeners() {
        // trim is used to remove leading and trailing white spaces
        loginButton.setOnClickListener {
            val username = usernameInput.text.toString().trim() // login listener for username input

            val password = passwordInput.text.toString().trim() // login listener for password input

            if (validateInputs(username, password)) {
                performLogin(username, password)
            }
        }


        /////////////////////////////////////////////////////////////////////

     /*
     *
     * we need a forgot password option and logic to redirect to reset password
     *
     * */


        /////////////////////////////////////////////////////////////////
    }
/////////////////////// Username exception handling ///////////////////////////////////////////////////

    // This exception handling is added to prevent users from not adding a username
    private fun validateInputs(username: String, password: String): Boolean {
        if (username.isEmpty()) {
            showMessage("You Need to enter Your Username ")
            usernameInput.requestFocus()
            return false
        }
        // added to make sure the username exceeds 2 characters
        if (username.length < 2) {
            showMessage("Username to Short, Enter A longer one")
            usernameInput.requestFocus()
            return false
        }


//////////////////////// Password exception handling //////////////////////////////////////////////////
    // added to ensure password isnt empty
        if (password.isEmpty()) {
            showMessage("Enter a Password")
            passwordInput.requestFocus()
            return false
        }
    // added to ensure input password exceeds 4 characters
        if (password.length < 4 ) {
        showMessage("Password must be at least 8 characters long")
        passwordInput.requestFocus()
        return false
    }

        return true
    }



    //////////////////////// authentication //////////////////////////////////

    //**************************** requires logic ***********************************************//

    private fun performLogin(username: String, password: String) {
        if (authenticate(username, password)) {

            // Successful login
            showMessage("Login successful")

            startActivity(intent)
            finish() // Close login activity
        } else {

            showMessage("Invalid username or password")
        }
    }


    //******************** requires database  and logic ***************************//

    private fun authenticate(username: String, password: String): Boolean {




        return false
    }

    private fun showMessage(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
    private lateinit var usernameInput: EditText

    private lateinit var passwordInput: EditText

    private lateinit var loginButton: Button

    private lateinit var forgotPasswordButton: Button









    // override function
    override fun onCreate(savedInstanceState: Bundle?) {
            super.onCreate(savedInstanceState)
            setContentView(R.layout.activity_login)


            initializeViews() // intializing user interface component


            setupClickListeners() // ''                     button click action
        }

    }




//********************************** end of login backend code *****************************//