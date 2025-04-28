package com.example.cashup

//************************* Start of imports ************************//

import android.content.Intent

import android.os.Bundle

import android.widget.Button // added to allow button actions

import android.widget.EditText // added to edit text

import android.widget.Toast // added to display pop up messages

import androidx.appcompat.app.AppCompatActivity //

import android.text.method.PasswordTransformationMethod // added to allow for switching between encrypted and view ( for password )

import android.widget.ImageButton // added to allow for "hidden eye" icon to be used


///////////////////////////////////////////////////////////////////////


import com.example.cashup.Database.AppDatabase

import kotlinx.coroutines.CoroutineScope

import kotlinx.coroutines.Dispatchers

import kotlinx.coroutines.launch

import kotlinx.coroutines.withContext

//////////////////////////////////////////////////////////////////////


//************************* End of imports ***************************//


//------------------------- Start of Code ----------------------------//

//**************************************************** START OF CLASS *********************************//
class LoginActivity : AppCompatActivity() {

//--------------- GLOBAL VARIABLES DECLARATION START ---------//

    private lateinit var usernameInput: EditText // USERNAME VARIABLE

    private lateinit var passwordInput: EditText // PASSWORD VARIABLE

    private lateinit var loginButton: Button // LOGIN BUTTON VARIABLE

    private lateinit var forgotPasswordButton: Button // FORGOT PASSWORD BUTTON VARIABLE

    private lateinit var registerLinkButton: Button // REGISTER VARIABLE

    private lateinit var passwordToggle: ImageButton // TOGGLE VARIABLE ( for (non)/encrypted viewing

    private var passwordVisible = false


    /////////////////////////////////////////////////

    private lateinit var database: AppDatabase

    /////////////////////////////////////////////////

    //--------------- GLOBAL VARIABLES DECLARATION END ---------//



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        initializeViews() // SET UP VIEW ON CREATE

        setupClickListeners() // SET UP LISTENER


        /////////////////////////////////////

        database = AppDatabase.getDatabase(this)

        /////////////////////////////////////
    }

    private fun initializeViews() {

        //--------------- DECLARATION --------------//

        usernameInput = findViewById(R.id.username_input)

        passwordInput = findViewById(R.id.password_input)

        loginButton = findViewById(R.id.login_button)

        forgotPasswordButton = findViewById(R.id.forgotpassword)

        registerLinkButton = findViewById(R.id.register_link_button)

        passwordToggle = findViewById(R.id.password_toggle) // ADDED TO CHOOSE BETWEEN VISIBLE AND NOT VISIBLE PASSWORD INPUT
    }

    private fun setupClickListeners() {

        loginButton.setOnClickListener {

            val username = usernameInput.text.toString().trim() // CONVERTED USER INPUT TO A STRING AND REMOVED LEADING AND TRAILING WHITE SPACE WITH trim()

            val password = passwordInput.text.toString().trim()

            if (validateInputs(username, password)) {

                performLogin(username, password)
            }
        }

        passwordToggle.setOnClickListener {

            passwordVisible = !passwordVisible
        // If the password visibility is clicked the visible icon will appear and the user will be able to view the password
            // this is due ti transformation being set to null as indicated on line 96
            if (passwordVisible) {


                passwordInput.transformationMethod = null

                passwordToggle.setImageResource(R.drawable.ic_visibility)
            } else
            {
        // if the password visibility is not clicked, the password will be hidden due to transformation being set
                passwordInput.transformationMethod = PasswordTransformationMethod.getInstance()

                passwordToggle.setImageResource(R.drawable.ic_visibility_off)
            }


            passwordInput.setSelection(passwordInput.text.length)
        }

        forgotPasswordButton.setOnClickListener {
            showMessage("Forgot Password Clicked (Implement Navigation)") // message to be displayed when button is clicked
        }

        registerLinkButton.setOnClickListener {
            val intent = Intent(this, RegisterActivity::class.java)

            startActivity(intent)
        }
    }

    private fun validateInputs(username: String, password: String): Boolean {

        // isEmpty() is used to verify that a user has actually entered a password

        if (username.isEmpty()) {
            showMessage("You Need to enter Your Username") // error message

            usernameInput.requestFocus()
            return false
        }
        // username needs to be longer than 2 characters
        if (username.length < 2) {
            showMessage("Username too Short, Enter A longer one") // error message if username is to short
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

        CoroutineScope(Dispatchers.Main).launch {

            val user = withContext(Dispatchers.IO) {

                database.userDao().getUserByEmail(username)
            }

            if (user != null && user.password == password) {
                // Successful login
                showMessage("Login successful")

                val homeIntent = Intent(this@LoginActivity, HomepageActivity::class.java)

                startActivity(homeIntent)

                finish()

            }
            else {
                showMessage("Invalid email or password")
            }
        }
    }

    private fun authenticate(username: String, password: String): Boolean {
        var isValid = false


        CoroutineScope(Dispatchers.Main).launch {
            val user = withContext(Dispatchers.IO) {


                database.userDao().getUserByEmail(username)  // this shall be changed if we want to use email or username for the login
            }

            if (user != null && user.password == password) {
                // User found and password matches
                showMessage("Login successful")
                val homeIntent = Intent(this@LoginActivity, HomepageActivity::class.java)
                startActivity(homeIntent)
                finish()
            } else {
                showMessage("Invalid email or password")
            }
        }

        return isValid
    }

    private fun showMessage(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}
//***************************************************** END OF CODE ***********************************************************//
