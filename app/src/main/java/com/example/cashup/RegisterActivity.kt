package com.example.cashup

//***************** Start of imports *****************************//
import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.cashup.Database.AppDatabase
import com.example.cashup.Database.User
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

//******************* End of imports ****************************//

class RegisterActivity : AppCompatActivity() {

    //********* local variables *************//
    private lateinit var nameEditText: EditText

    private lateinit var emailEditText: EditText

    private lateinit var passwordEditText: EditText

    private lateinit var confirmPasswordEditText: EditText

    private lateinit var registerButton: Button

    private lateinit var database: AppDatabase

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
        nameEditText = findViewById(R.id.streaknum) /// name

        emailEditText = findViewById(R.id.email_input) // email

        passwordEditText = findViewById(R.id.password_input) // password

        confirmPasswordEditText = findViewById(R.id.confirm_password_input) // conformation

        registerButton = findViewById(R.id.register_button) // register clickable button
    }


    // register new user
    private fun registerUser() {

        val name = nameEditText.text.toString().trim() // input name + remove leading and trailing white space

        val email = emailEditText.text.toString().trim() // input email + remove leading and trailing white space

        val password = passwordEditText.text.toString().trim() // input password + remove leading and trailing white space

        val confirmPassword = confirmPasswordEditText.text.toString().trim() // confirmation  + remove leading and trailing white space

        // Input validation
        if (validateInputs(name, email, password, confirmPassword)) {
            // Split name into first and last name (assuming space-separated input)
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

                // Create and save user
                val user = User(
                    firstName = firstName,
                    lastName = lastName,
                    email = email,
                    password = password
                )

                withContext(Dispatchers.IO) {
                    database.userDao().insertUser(user)
                }

                // Display success message
                Toast.makeText(this@RegisterActivity, "Registration successful!", Toast.LENGTH_SHORT).show()

                // Navigate to login screen
                navigateToLogin()
            }
        }
    }

    private fun validateInputs(name: String, email: String, password: String, confirmPassword: String): Boolean {
        // Validate name
        if (TextUtils.isEmpty(name)) {
            nameEditText.error = "Name is required"
            nameEditText.requestFocus()
            return false




        }


        // Validate email
        if (TextUtils.isEmpty(email)) {
            emailEditText.error = "Email is required"
            emailEditText.requestFocus()
            return false
        }

        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            emailEditText.error = "Please enter a valid email"
            emailEditText.requestFocus()
            return false
        }

        // Validate password
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

        // Validate password confirmation
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
        // Create intent to navigate to LoginActivity
        val intent = Intent(this, LoginActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK
        startActivity(intent)
        finish()
    }
}