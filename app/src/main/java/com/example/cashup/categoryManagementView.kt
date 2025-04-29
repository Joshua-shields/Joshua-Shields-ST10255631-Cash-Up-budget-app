
//------------------------------- START OF FILE ----------------------------//
package com.example.cashup


import android.os.Bundle // STORES DATA

import android.widget.ImageButton // ALLOWS FOR IMAGES TO BE USED AS CLICKABLE BUTTONS

import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

//////////////////////////////// START OF CODE /////////////////////////////
class categoryManagementView : AppCompatActivity() {

    private lateinit var crownButton: ImageButton
    private lateinit var statsButton: ImageButton


    //**************** START OF OVERRIDE FUNCTION ***************//

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_category_management_view)



        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    //*********************** END OF OVERRIDE FUNCTION ********************//
}

////////////////////////////////// END OF CODE ///////////////////////////////////////

//------------------------------ END OF FILE ------------------------------//