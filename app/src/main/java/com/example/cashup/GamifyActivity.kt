package com.example.cashup

import android.os.Bundle
import android.widget.ImageButton
import androidx.appcompat.app.AppCompatActivity

class GamifyActivity : AppCompatActivity() {

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
//***************************************************** END OF CODE ***********************************************************//
