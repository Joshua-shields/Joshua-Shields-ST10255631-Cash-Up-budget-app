package com.example.cashup

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.cashup.Database.Category
import com.example.cashup.Database.ExpenseDatabase
import kotlinx.coroutines.launch
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext


//-----------------------------------------START OF FILE--------------------------------//
class EditCategoriesView : AppCompatActivity() {
    //declarations
    //UI Components
    private lateinit var newCategoryEditText: EditText
    private lateinit var saveButton: Button
    private lateinit var backButton: ImageButton

    //category buttons
    private lateinit var groceriesButton: Button
    private lateinit var homeButton: Button
    private lateinit var transportButton: Button
    private lateinit var giftsButton: Button
    private lateinit var workButton: Button
    private lateinit var foodButton: Button
    private lateinit var entertainmentButton: Button

    private lateinit var expenseDatabase: ExpenseDatabase

    // Companion object for the result key
    companion object {
        const val EXTRA_SELECTED_CATEGORY = "SELECTED_CATEGORY"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_categories_view)

        expenseDatabase = ExpenseDatabase.getDatabase(this)

        //Initialisation of the different views
        newCategoryEditText = findViewById(R.id.newCategoryEditText)
        saveButton = findViewById(R.id.saveButton)
        backButton = findViewById(R.id.backButton)


        //locate views via ID
        groceriesButton = findViewById(R.id.groceriesButton)
        homeButton = findViewById(R.id.homeButton)
        transportButton = findViewById(R.id.transportButton)
        giftsButton = findViewById(R.id.giftsButton)
        workButton = findViewById(R.id.workButton)
        foodButton = findViewById(R.id.foodButton)
        entertainmentButton = findViewById(R.id.entertainmentButton)

        setupCategoryButton(groceriesButton)
        setupCategoryButton(homeButton)
        setupCategoryButton(transportButton)
        setupCategoryButton(giftsButton)
        setupCategoryButton(workButton)
        setupCategoryButton(foodButton)
        setupCategoryButton(entertainmentButton)


        backButton.setOnClickListener {
            setResult(RESULT_CANCELED)
            finish()
        }

        // save button
        saveButton.setOnClickListener {
            val newCategoryName = newCategoryEditText.text.toString().trim()
            if (newCategoryName.isNotEmpty()) {
                addNewCategory(newCategoryName)
            } else {
                Toast.makeText(this, "Please enter a category name", Toast.LENGTH_SHORT).show()
            }
        }
    }
    /*
    private fun loadCategories() {
        lifecycleScope.launch(Dispatchers.IO) { // Use lifecycleScope
            try {
                expenseDatabase.categoryDao().getAllCategories()
                // If you need to update UI with categories, switch back to Main thread
            } catch (e: Exception) {
                 withContext(Dispatchers.Main) { // Use withContext for single switch
                    Toast.makeText(
                        this@EditCategoriesView,
                        "Error loading categories: ${e.message}",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }
    }
    */

    private fun setupCategoryButton(button: Button) {
        button.setOnClickListener {
            val categoryName = button.text.toString()

            lifecycleScope.launch {
                try {
                    val exists = withContext(Dispatchers.IO) { // Switch to IO for DB access
                        expenseDatabase.categoryDao().categoryExists(categoryName)
                    }

                    if (exists <= 0) {
                        // Add the default category if it doesn't exist
                        withContext(Dispatchers.IO) {
                            expenseDatabase.categoryDao().insertCategory(
                                Category(
                                    name = categoryName,
                                    isDefault = true
                                )
                            )
                        }
                    }

                    // Return the selected category name to the calling activity
                    val resultIntent = Intent()
                    // Use the constant key
                    resultIntent.putExtra(EXTRA_SELECTED_CATEGORY, categoryName)
                    setResult(RESULT_OK, resultIntent)
                    finish() // Close this activity

                } catch (e: Exception) {
                    // Show error on the main thread
                    withContext(Dispatchers.Main) {
                        Toast.makeText(
                            this@EditCategoriesView,
                            "Error selecting category: ${e.message}",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            }
        }
    }

    private fun addNewCategory(categoryName: String) {
        // Use lifecycleScope
        lifecycleScope.launch {
            try {
                val exists = withContext(Dispatchers.IO) { // Switch to IO for DB check
                    expenseDatabase.categoryDao().categoryExists(categoryName)
                }

                if (exists > 0) {
                    // Show message on Main thread
                    withContext(Dispatchers.Main) {
                        Toast.makeText(
                            this@EditCategoriesView,
                            "Category '$categoryName' already exists",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                } else {
                    // Insert on IO thread
                    withContext(Dispatchers.IO) {
                        expenseDatabase.categoryDao().insertCategory(
                            Category(
                                name = categoryName,
                                isDefault = false // Newly added are not default
                            )
                        )
                    }

                    // Return result and show success message on Main thread
                    withContext(Dispatchers.Main) {
                        val resultIntent = Intent()
                        // Use the constant key
                        resultIntent.putExtra(EXTRA_SELECTED_CATEGORY, categoryName)
                        setResult(RESULT_OK, resultIntent)

                        Toast.makeText(
                            this@EditCategoriesView,
                            "Category '$categoryName' added successfully",
                            Toast.LENGTH_SHORT
                        ).show()
                        finish() // Close this activity
                    }
                }
            } catch (e: Exception) {
                // Show error on Main thread
                withContext(Dispatchers.Main) {
                    Toast.makeText(
                        this@EditCategoriesView,
                        "Error adding category: ${e.message}",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }
    }
}
//***************************************************** END OF CODE ***********************************************************//
