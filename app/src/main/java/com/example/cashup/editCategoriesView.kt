package com.example.cashup

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.cashup.Database.Category
import com.example.cashup.Database.ExpenseDatabase
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.Dispatchers

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
        newCategoryEditText = findViewById(R.id.newCategoryEditText)

        setupCategoryButton(groceriesButton)
        setupCategoryButton(homeButton)
        setupCategoryButton(transportButton)
        setupCategoryButton(giftsButton)
        setupCategoryButton(workButton)
        setupCategoryButton(foodButton)
        setupCategoryButton(entertainmentButton)


        backButton.setOnClickListener {
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
//unknown if working check if works, if not delete
    private fun loadCategories() {
        GlobalScope.launch(Dispatchers.IO) {
            try {
                expenseDatabase.categoryDao().getAllCategories()

            } catch (e: Exception) {
                runOnUiThread {
                    Toast.makeText(
                        this@EditCategoriesView,
                        "Error loading categories: ${e.message}",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }
    }

    private fun setupCategoryButton(button: Button) {
        button.setOnClickListener {
            val categoryName = button.text.toString()

            GlobalScope.launch(Dispatchers.IO) {
                try {
                    val exists = expenseDatabase.categoryDao().categoryExists(categoryName)

                    if (exists <= 0) {
                        expenseDatabase.categoryDao().insertCategory(
                            Category(
                                name = categoryName,
                                isDefault = true
                            )
                        )
                    }

                    runOnUiThread {
                        val resultIntent = Intent()
                        resultIntent.putExtra("SELECTED_CATEGORY", categoryName)
                        setResult(RESULT_OK, resultIntent)
                        finish()
                    }
                } catch (e: Exception) {
                    runOnUiThread {
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
        GlobalScope.launch(Dispatchers.IO) {
            try {
                val exists = expenseDatabase.categoryDao().categoryExists(categoryName)

                if (exists > 0) {
                    runOnUiThread {
                        Toast.makeText(
                            this@EditCategoriesView,
                            "Category '$categoryName' already exists",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                } else {
                    expenseDatabase.categoryDao().insertCategory(
                        Category(
                            name = categoryName,
                            isDefault = false
                        )
                    )

                    runOnUiThread {
                        val resultIntent = Intent()
                        resultIntent.putExtra("SELECTED_CATEGORY", categoryName)
                        setResult(RESULT_OK, resultIntent)

                        Toast.makeText(
                            this@EditCategoriesView,
                            "Category '$categoryName' added successfully",
                            Toast.LENGTH_SHORT
                        ).show()
                        finish()
                    }
                }
            } catch (e: Exception) {
                runOnUiThread {
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
//------------------------------------------END OF FILE-------------------------------//