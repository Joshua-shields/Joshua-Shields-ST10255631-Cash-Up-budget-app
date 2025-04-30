
//------------------------------------- START OF FILE ----------------------------------//
package com.example.cashup

//***************** Start of imports *****************************//

import android.content.Intent

import android.os.Bundle // ALLOWS FOR DATA STORAGE

import android.text.TextUtils

import android.widget.Button // ALLOWS FOR CLICKABLE BUTTONS

import android.widget.EditText // ALLOWS FOR EDITING OF TEXT

import android.widget.Toast // ALLOWS FOR ERROR MESSAGES TO BE DISPLAYED

import androidx.appcompat.app.AppCompatActivity

import com.example.cashup.Database.AppDatabase

import com.example.cashup.Database.User // ACCESS TO THE DATABASE

import kotlinx.coroutines.CoroutineScope

import kotlinx.coroutines.Dispatchers

import kotlinx.coroutines.launch

import kotlinx.coroutines.withContext

import android.widget.ImageButton

import android.text.method.PasswordTransformationMethod // Added for password toggle
//******************* End of imports ****************************//

class RegisterActivity : AppCompatActivity() {

    //********* local variables *************//
    private lateinit var nameEditText: EditText
    private lateinit var emailEditText: EditText
    private lateinit var passwordEditText: EditText
    private lateinit var confirmPasswordEditText: EditText
    private lateinit var registerButton: Button
    private lateinit var database: AppDatabase
    private lateinit var passwordToggle: ImageButton
    private lateinit var confirmPasswordToggle: ImageButton
    private var passwordVisible = false
    private var confirmPasswordVisible = false
    //********* end of local variables ********//

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        // Initialize Room database
        database = AppDatabase.getDatabase(this)

        // Initialize UI elements
        initUI()

        // Set click listener for register button
        registerButton.setOnClickListener {
            registerUser()
        }
    }

    // initialization of user ui
    private fun initUI() {
        nameEditText = findViewById(R.id.name_input)
        emailEditText = findViewById(R.id.email_input)
        passwordEditText = findViewById(R.id.password_input)
        confirmPasswordEditText = findViewById(R.id.confirm_password_input)
        registerButton = findViewById(R.id.register_button)
        passwordToggle = findViewById(R.id.password_toggle)
        confirmPasswordToggle = findViewById(R.id.confirm_password_toggle)

        val backButton: ImageButton = findViewById(R.id.back_button)
        backButton.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            finish()
        }

        // Set up password toggle
        passwordToggle.setOnClickListener {
            passwordVisible = !passwordVisible
            if (passwordVisible) {
                passwordEditText.transformationMethod = null
                passwordToggle.setImageResource(R.drawable.ic_visibility)
            } else {
                passwordEditText.transformationMethod = PasswordTransformationMethod.getInstance()
                passwordToggle.setImageResource(R.drawable.ic_visibility_off)
            }
            passwordEditText.setSelection(passwordEditText.text.length)
        }

        // Set up confirm password toggle
        confirmPasswordToggle.setOnClickListener {
            confirmPasswordVisible = !confirmPasswordVisible
            if (confirmPasswordVisible) {
                confirmPasswordEditText.transformationMethod = null
                confirmPasswordToggle.setImageResource(R.drawable.ic_visibility)
            } else {
                confirmPasswordEditText.transformationMethod = PasswordTransformationMethod.getInstance()
                confirmPasswordToggle.setImageResource(R.drawable.ic_visibility_off)
            }
            confirmPasswordEditText.setSelection(confirmPasswordEditText.text.length)
        }
    }

    // register new user
    private fun registerUser() {
        val name = nameEditText.text.toString().trim()
        val email = emailEditText.text.toString().trim()
        val password = passwordEditText.text.toString().trim()
        val confirmPassword = confirmPasswordEditText.text.toString().trim()

        // Input validation
        if (validateInputs(name, email, password, confirmPassword)) {
            val nameParts = name.split(" ")
            val firstName = nameParts.getOrElse(0) { "" }
            val lastName = nameParts.getOrElse(1) { "" }

            CoroutineScope(Dispatchers.Main).launch {
                val existingUser = withContext(Dispatchers.IO) {
                    database.userDao().getUserByEmail(email)
                }

                if (existingUser != null) {
                    emailEditText.error = "Email already registered"
                    emailEditText.requestFocus()
                    return@launch
                }

                val user = User(
                    firstName = firstName,
                    lastName = lastName,
                    email = email,
                    password = password
                )

                withContext(Dispatchers.IO) {
                    database.userDao().insertUser(user)
                }

                Toast.makeText(
                    this@RegisterActivity,
                    "Registration successful!",
                    Toast.LENGTH_SHORT
                ).show()

                navigateToLogin()
            }
        }
    }

    private fun validateInputs(
        name: String,
        email: String,
        password: String,
        confirmPassword: String
    ): Boolean {
        if (TextUtils.isEmpty(name)) {
            nameEditText.error = "Name is required"
            nameEditText.requestFocus()
            return false
        }

        if (TextUtils.isEmpty(email)) {
            emailEditText.error = "Email is required"
            emailEditText.requestFocus()
            return false
        }

        /* if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            emailEditText.error = "Please enter a valid email"
            emailEditText.requestFocus()
            return false
        } */

        if (TextUtils.isEmpty(password)) {
            passwordEditText.error = "Password is required"
            passwordEditText.requestFocus()
            return false
        }

        if (password.length < 6) {
            passwordEditText.error = "Password must be at least 6 characters"
            passwordEditText.requestFocus()
            return false
        }

        if (TextUtils.isEmpty(confirmPassword)) {
            confirmPasswordEditText.error = "Please confirm your password"
            confirmPasswordEditText.requestFocus()
            return false
        }

        if (password != confirmPassword) {
            confirmPasswordEditText.error = "Passwords do not match"
            confirmPasswordEditText.requestFocus()
            return false
        }

        return true
    }

    private fun navigateToLogin() {
        val intent = Intent(this, LoginActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK
        startActivity(intent)
        finish()
    }
}
//------------------------------------------- END OF FILE --------------------------------------//