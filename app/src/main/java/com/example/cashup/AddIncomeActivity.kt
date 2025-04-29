
//-------------------------------- START OF FILE -------------------------------//
package com.example.cashup

//******************** START OF IMPORTS *****************//

import android.os.Bundle // STORES DATA

import android.widget.ImageButton // ALLOWS FOR IMAGES TO BE USED AS A BUTTON AS PER TEXT

import androidx.appcompat.app.AppCompatActivity

//********************* END OF IMPORTS ****************//


/*
*
*
* THIS FILE AND COD WILL BE IMPLEMENTED IN PART 3
*
* BASIC OVERRIDE FUNCTION FOR NOW TO PREVENT CRASHES IN THE CODE
*
*
*
* */
class AddIncomeActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Set the content view to the XML layout you created
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
//------------------------------- END OF FILE ---------------------------------------//