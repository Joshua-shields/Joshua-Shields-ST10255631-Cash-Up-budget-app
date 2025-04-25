package com.example.cashup

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.graphics.toColorInt
import androidx.core.graphics.drawable.toDrawable

//-----------------------------------------START OF FILE--------------------------------//
class EditCategoriesView : AppCompatActivity() {
//declarations
    // UI Components
    private lateinit var btnSave: Button
    private lateinit var btnColourNew: Button
    private lateinit var newCatName: EditText
    private lateinit var assignColorField: EditText
    private lateinit var backArrow: ImageButton

    //--------------------------------------------------------
    // Data structures
    private val categoryColours = mutableMapOf<String, Int>()
    private val categoryEditTexts = mutableMapOf<String, EditText>()
    private val colourButtons = mutableListOf<Button>()

    //---------------------------------------------------------
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_categories_view)

        // Initialize UI components
        initializeUI()

        // Set up colour buttons for existing categories
        setupColourButtons()

        // Set up listeners
        setupListeners()

        // Load existing categories (could be from a database in the future)
        loadExistingCategories()
    }

    private fun initializeUI() {
        // Main UI components
        btnSave = findViewById(R.id.btn_save)
        btnColourNew = findViewById(R.id.btn_color_new)
        newCatName = findViewById(R.id.new_category_name)
        assignColorField = findViewById(R.id.assign_color)
        backArrow = findViewById(R.id.back_arrow)

        // Make assign color field non-editable - it's just for UI hint
        assignColorField.isFocusable = false
        assignColorField.isClickable = false
    }

    private fun setupListeners() {
        // Save button listener
        btnSave.setOnClickListener {
            saveCategories()
        }

        // Back button listener
        backArrow.setOnClickListener {
            Toast.makeText(this, "Button clicked", Toast.LENGTH_SHORT).show()
        }

        // Color selector for new category
        btnColourNew.setOnClickListener {
            showColourPicker(btnColourNew)
        }
    }

    private fun loadExistingCategories() {
        //this should be replaced with database loading in a production app

        categoryColours["groceries"] = "#FFCC00".toColorInt()
        categoryColours["home"] = "#00FF00".toColorInt()
        categoryColours["transport"] = "#00AAF".toColorInt()
        categoryColours["gifts"] = "#FF00FF".toColorInt()
        categoryColours["work"] = "#FFCC00".toColorInt()
        categoryColours["fast_food"] = "#FF6600".toColorInt()
        categoryColours["entertainment"] = "#00FFFF".toColorInt()
        categoryColours["extra"] = "#FF00FF".toColorInt()

        // Set button colors to match the loaded values
        updateColorButtonsFromData()
    }

    private fun updateColorButtonsFromData() {
        // Update each button with the color from the data structure
        for (button in colourButtons) {
            when (button.id) {
                R.id.btn_colour_groceries -> button.setBackgroundColor(
                    categoryColours["groceries"] ?: Color.RED
                )

                R.id.btn_colour_home -> button.setBackgroundColor(
                    categoryColours["home"] ?: Color.GREEN
                )

                R.id.btn_colour_transport -> button.setBackgroundColor(
                    categoryColours["transport"] ?: Color.YELLOW
                )

                R.id.btn_colour_gifts -> button.setBackgroundColor(
                    categoryColours["gifts"] ?: Color.MAGENTA
                )

                R.id.btn_colour_work -> button.setBackgroundColor(
                    categoryColours["work"] ?: Color.BLUE
                )

                R.id.btn_colour_fast_food -> button.setBackgroundColor(
                    categoryColours["fast_food"] ?: Color.LTGRAY
                )

                R.id.btn_colour_entertainment -> button.setBackgroundColor(
                    categoryColours["entertainment"] ?: Color.CYAN
                )

                R.id.btn_colour_extra -> button.setBackgroundColor(
                    categoryColours["extra"] ?: Color.MAGENTA
                )
            }
        }
    }

    private fun setupColourButtons() {
        // Add category edit texts to the map for easy reference
        categoryEditTexts["groceries"] = findViewById(R.id.groceries)
        categoryEditTexts["home"] = findViewById(R.id.et_home)
        categoryEditTexts["transport"] = findViewById(R.id.transport)
        categoryEditTexts["gifts"] = findViewById(R.id.gifts)
        categoryEditTexts["work"] = findViewById(R.id.work)
        categoryEditTexts["fast_food"] = findViewById(R.id.food)
        categoryEditTexts["entertainment"] = findViewById(R.id.entertainment)
        categoryEditTexts["extra"] = findViewById(R.id.extra)

        // Add all category colour buttons to the list
        colourButtons.add(findViewById(R.id.btn_colour_groceries))
        colourButtons.add(findViewById(R.id.btn_colour_home))
        colourButtons.add(findViewById(R.id.btn_colour_transport))
        colourButtons.add(findViewById(R.id.btn_colour_gifts))
        colourButtons.add(findViewById(R.id.btn_colour_work))
        colourButtons.add(findViewById(R.id.btn_colour_fast_food))
        colourButtons.add(findViewById(R.id.btn_colour_entertainment))
        colourButtons.add(findViewById(R.id.btn_colour_extra))

        // Set onclick listeners for all colour buttons
        for (button in colourButtons) {
            button.setOnClickListener {
                showColourPicker(button)
            }
        }
    }

    private fun showColourPicker(button: Button) {
        // Define available colors with better range
        val colors = arrayOf(
            "#FF0000".toColorInt(),  // Red
            "#00FF00".toColorInt(),  // Green
            "#0000FF".toColorInt(),  // Blue
            "#FFFF00".toColorInt(),  // Yellow
            "#00FFFF".toColorInt(),  // Cyan
            "#FF00FF".toColorInt(),  // Magenta
            "#FFFFFF".toColorInt(),  // White
            "#FF6600".toColorInt(),  // Orange
            "#00AAFF".toColorInt(),  // Light Blue
            "#AAAAAA".toColorInt()   // Gray
        )

        // Create color preview views for the dialog
        val colorViews = Array(colors.size) { i ->
            View(this).apply {
                layoutParams = ViewGroup.LayoutParams(60, 60)
                setBackgroundColor(colors[i])
            }
        }

        // Build the dialog with a grid of color options
        val dialog = AlertDialog.Builder(this)
            .setTitle("Select Colour")
            .setItems(Array(colors.size) { "" }) { _, which ->
                button.setBackgroundColor(colors[which])

                // Update the color map based on which button was clicked
                when (button.id) {
                    R.id.btn_colour_groceries -> categoryColours["groceries"] = colors[which]
                    R.id.btn_colour_home -> categoryColours["home"] = colors[which]
                    R.id.btn_colour_transport -> categoryColours["transport"] = colors[which]
                    R.id.btn_colour_gifts -> categoryColours["gifts"] = colors[which]
                    R.id.btn_colour_work -> categoryColours["work"] = colors[which]
                    R.id.btn_colour_fast_food -> categoryColours["fast_food"] = colors[which]
                    R.id.btn_colour_entertainment -> categoryColours["entertainment"] =
                        colors[which]

                    R.id.btn_colour_extra -> categoryColours["extra"] = colors[which]
                    R.id.btn_color_new -> {
                        // No category yet for the new button
                    }
                }
            }
            .setNegativeButton("Cancel") { dialog, _ -> dialog.dismiss() }
            .create()

        // Show the dialog
        dialog.show()

        // Make the dialog items show the color swatches
        val listView = dialog.listView
        for (i in colorViews.indices) {
            listView.getChildAt(i)?.background = colors[i].toDrawable()
        }
    }

    private fun saveCategories() {
        // Get the new category name
        val newCategoryName = newCatName.text.toString().trim()

        // First save any changes to existing category names
        saveExistingCategoryChanges()

        // Check if we have a new category to add
        if (newCategoryName.isNotEmpty()) {
            // Validation check for the new category name
            if (categoryColours.containsKey(newCategoryName.lowercase())) {
                Toast.makeText(this, "Category already exists!", Toast.LENGTH_SHORT).show()
                return
            }

            // Get the color for the new category
            val newCategoryColour =
                (btnColourNew.background as? ColorDrawable)?.color ?: Color.WHITE

            // Add the new category
            categoryColours[newCategoryName.lowercase()] = newCategoryColour

            // In a real app, you would save to database here
            // saveToDatabase(newCategoryName, newCategoryColour)

            Toast.makeText(this, "New category '$newCategoryName' added", Toast.LENGTH_SHORT).show()
        }

        Toast.makeText(this, "Categories updated", Toast.LENGTH_SHORT).show()

        // Return to previous screen
        finish()
    }

    private fun saveExistingCategoryChanges() {
        // Check for changes in each category name
        for ((key, editText) in categoryEditTexts) {
            val newText = editText.text.toString().trim()
            val currentKey = key

            // If text changed, update the map
            if (newText != getDisplayNameForCategory(currentKey)) {
                // Get color before removing
                val color = categoryColours[currentKey] ?: Color.WHITE

                // Remove old entry and add new one
                categoryColours.remove(currentKey)
                categoryColours[newText.lowercase()] = color

                // In a real app, update the database here
                // updateCategoryInDb(currentKey, newText, color)
            }
        }
    }

    // Helper to get display names for categories (could be from resources in a real app)
    private fun getDisplayNameForCategory(category: String): String {
        return when (category) {
            "groceries" -> "Groceries and Market"
            "home" -> "Home and Maintenance"
            "transport" -> "Transport"
            "gifts" -> "Gifts"
            "work" -> "Work"
            "fast_food" -> "Fast food"
            "entertainment" -> "Entertainment"
            "extra" -> "Extra"
            else -> category
        }
    }
}
//------------------------------------------END OF FILE-------------------------------//