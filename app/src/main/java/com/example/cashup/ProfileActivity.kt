
//--------------------------------- START OF FILE --------------------------//
package com.example.cashup


import android.os.Bundle // STORES DATA

import android.widget.ImageButton // ALLOWS FOR IMAGES TO BE USED AS CLICKABLE BUTTONS

import androidx.appcompat.app.AppCompatActivity

class ProfileActivity : AppCompatActivity() {

    /*
    * BELOW IS A BASIC OVERIDE FUNCTION TO PREVENT CRASHES
    *
    * FURTHER CODE AND LOGIC WILL BE IMPLEMENTED IN PART 3
    *
    *
    * */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.add_income_section)

        // Find the back button by its ID
        val backButton: ImageButton = findViewById(R.id.back_button_poe)

        // Set an OnClickListener for the back button
        backButton.setOnClickListener {
            // Finish the current activity and go back to the previous one
            finish()
        }
    }
}
//---------------------------------- END OF FILE -------------------------------//