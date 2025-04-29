
//-------------------------- START OF FILE -------------------------------------//
package com.example.cashup

import android.os.Bundle // STORES DATA

import android.widget.ImageButton // ALLOWS FOR IMAGES TO BE USED AS BUTTONS

import androidx.appcompat.app.AppCompatActivity

class GamifyActivity : AppCompatActivity() {
/*
* BELOW IS AN OVERRIDE FUNCTION WHICH FOR NOW IS USED TO PREVENT THE APPLICATION FROM CRASHING
*
* FURTHER CODE AND LOGIC WILL BE USED IN PART 3
*
* */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Set the content view to the XML layout you created
        setContentView(R.layout.gamifyactivity)

        // Find the back button by its ID
        val backButton: ImageButton = findViewById(R.id.back_button_poe)

        // Set an OnClickListener for the back button
        backButton.setOnClickListener {
            // Finish the current activity and go back to the previous one
            finish()
        }
    }
}
//--------------------------- END OF FILE ----------------------------------//
