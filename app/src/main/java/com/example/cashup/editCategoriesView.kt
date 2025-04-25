package com.example.cashup

import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity

//-----------------------------------------START OF FILE--------------------------------//
class EditCategoriesView : AppCompatActivity() {

    //declaration of variables
    private lateinit var btnSave: Button
    private lateinit var btnColourNew: Button
    private val categoryColours = mutableMapOf<String, Int>()
    private val colourButtons = mutableListOf<Button>()
    private lateinit var newCatName: EditText

    //-------------------------oncreate method-----------------------//
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_categories_view)

        //initialisation of UI components, change the colour button so that it is the image button
        btnSave = findViewById(R.id.btn_save)
        btnColourNew = findViewById(R.id.btn_color_new)
        newCatName = findViewById(R.id.new_category_name)

        //set up colour buttons for existing categories
        setupColourButtons()

        //set up onclick listeners
        btnSave.setOnClickListener {
            saveCategories()
        }

        //set up back button, change so that it redirects to the add expense view
        findViewById<View>(R.id.btn_back).setOnClickListener { onBackPressed() }

        //setup colour selector for new category
        btnColourNew.setOnClickListener {
            showColourPicker(btnColourNew)
        }
    }
//-----------------------setup of colour buttons---------------------------//
    private fun setupColourButtons() {
        //add all category colour buttons to a list
        colourButtons.add(findViewById(R.id.btn_colour_groceries))
        colourButtons.add(findViewById(R.id.btn_colour_home))
        colourButtons.add(findViewById(R.id.btn_colour_transport))
        colourButtons.add(findViewById(R.id.btn_colour_gifts))
        colourButtons.add(findViewById(R.id.btn_colour_work))
        colourButtons.add(findViewById(R.id.btn_colour_fast_food))
        colourButtons.add(findViewById(R.id.btn_colour_entertainment))
        colourButtons.add(findViewById(R.id.btn_colour_extra))

        //set onclick listeners for all colour buttons
        for (button in colourButtons) {
            button.setOnClickListener {
                showColourPicker(button)
            }
        }

        //initialise the category colours map with current colors
        categoryColours["groceries"] = Color.RED
        categoryColours["home"] = Color.GREEN
        categoryColours["transport"] = Color.YELLOW
        categoryColours["gifts"] = Color.MAGENTA
        categoryColours["work"] = Color.BLUE
        categoryColours["fast_food"] = Color.LTGRAY
        categoryColours["entertainment"] = Color.CYAN
        categoryColours["extra"] = Color.MAGENTA
    }
//-----------------------------colour selection method---------------------//
    private fun showColourPicker(button: Button) {
        //define available colors available to choose from
        val colors = arrayOf(
            Color.RED,
            Color.GREEN,
            Color.BLUE,
            Color.YELLOW,
            Color.CYAN,
            Color.MAGENTA,
            Color.WHITE,
            Color.YELLOW,
            Color.LTGRAY
        )

        //create ColourView items for the dialog
        val colours = Array(colors.size) { i ->
            View(this).apply {
                layoutParams = ViewGroup.LayoutParams(48, 48)
                setBackgroundColor(colors[i])
            }
        }

        // create an alert message for the colour options
        AlertDialog.Builder(this)
            .setTitle("Select Colour")
            .setItems(Array(colors.size) { "" }) { dialog, which ->
                button.setBackgroundColor(colors[which])

                //update the colour map if it's one of the existing category buttons, should change this to improve usability
                when (button.id) {
                    R.id.btn_colour_groceries -> categoryColours["groceries"] = colors[which]
                    R.id.btn_colour_home -> categoryColours["home"] = colors[which]
                    R.id.btn_colour_transport -> categoryColours["transport"] = colors[which]
                    R.id.btn_colour_gifts -> categoryColours["gifts"] = colors[which]
                    R.id.btn_colour_work -> categoryColours["work"] = colors[which]
                    R.id.btn_colour_fast_food -> categoryColours["fast_food"] = colors[which]
                    R.id.btn_colour_entertainment -> categoryColours["entertainment"] = colors[which]
                    R.id.btn_colour_extra -> categoryColours["extra"] = colors[which]
                }
            }

            .setNegativeButton("Cancel") { dialog, _ -> dialog.dismiss() }
            .show().apply {
                //set views for each item to display coloured squares

            }
    }
//-----------------------------addNewCatName method---------------------------//
    private fun saveCategories() {
        //declare the new category name
        val newCategoryName: String = newCatName.text.toString().trim()

        //checker if user is trying to add a new category
        if (newCategoryName.isNotEmpty()) {
            //validation check for the new category name
            if (categoryColours.containsKey(newCategoryName.lowercase())) {
                Toast.makeText(this, "Category already exists!", Toast.LENGTH_SHORT).show()
                return
            }

            //get the colour for the new category
            val newCategoryColour = btnColourNew.solidColor
            // Add the new category, must consider another way of doing this
            categoryColours[newCategoryName.lowercase()] = newCategoryColour

            //must create a database to hold the category name and call from the database to display the new name

            Toast.makeText(this, "New category '$newCategoryName' added", Toast.LENGTH_SHORT).show()
        }

        Toast.makeText(this, "Categories updated", Toast.LENGTH_SHORT).show()

        //send to next screen (find out where it must go)
        finish()
    }
}

//------------------------------------------END OF FILE-------------------------------//