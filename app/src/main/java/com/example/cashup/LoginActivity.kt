
//***************************** start of login backend code **************************************//

package com.example.cashup

import android.os.Bundle
import android.widget.Button // allows for clickable buttons to be coded
import android.widget.EditText // allows for text to be edited (input fields)
import android.widget.ImageView // Import ImageView if you need to reference it
import android.widget.TextView // Import TextView if you need to reference it
import android.widget.Toast // needed for pop up messages
import androidx.appcompat.app.AppCompatActivity

class LoginActivity : AppCompatActivity() { // Needs to inherit from AppCompatActivity

    // Declare views
    private lateinit var imageView: ImageView
    private lateinit var textViewTitle: TextView
    private lateinit var usernameInput: EditText
    private lateinit var passwordInput: EditText
    private lateinit var forgotPasswordButton: Button
    private lateinit var loginButton: Button


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Set the layout file for this activity
        // Make sure R.layout.activity_login matches your XML file name
        setContentView(R.layout.activity_login)

    }
}




//********************************** end of login backend code *****************************//