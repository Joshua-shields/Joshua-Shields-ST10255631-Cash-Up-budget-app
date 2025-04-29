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

    //-----------------------declarations-----------------------------//
    private lateinit var newCategoryEditText: EditText //declares the newCategoryText variable as a EditText component
    private lateinit var saveButton: Button //declares the saveButton variable as a button component
    private lateinit var backButton: ImageButton //declares the backButton variable as a ImageButton component
    //category buttons
    private lateinit var groceriesButton: Button //declares the groceriesButton variable as a button component
    private lateinit var homeButton: Button //declares the homeButton variable as a button component
    private lateinit var transportButton: Button //declares the transportButton variable as a button component
    private lateinit var giftsButton: Button //declares the giftButton variable as a button component
    private lateinit var workButton: Button //declares the worksButton variable as a button component
    private lateinit var foodButton: Button //declares the foodButton variable as a button component
    private lateinit var entertainmentButton: Button //declares the entertainmentButton variable as a button component

    private lateinit var expenseDatabase: ExpenseDatabase
    //----------------------------------------------------------------//

    // Companion object for the result key
    companion object {
        const val EXTRA_SELECTED_CATEGORY = "SELECTED_CATEGORY"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_categories_view)

        expenseDatabase = ExpenseDatabase.getDatabase(this)

        //-------------------UI reference list------------------------//
        //reference to UI elements (in this case buttons) defined in the xml file
        newCategoryEditText = findViewById(R.id.newCategoryEditText)
        saveButton = findViewById(R.id.saveButton)
        backButton = findViewById(R.id.backButton)
        groceriesButton = findViewById(R.id.groceriesButton)
        homeButton = findViewById(R.id.homeButton)
        transportButton = findViewById(R.id.transportButton)
        giftsButton = findViewById(R.id.giftsButton)
        workButton = findViewById(R.id.workButton)
        foodButton = findViewById(R.id.foodButton)
        entertainmentButton = findViewById(R.id.entertainmentButton)

        //methods used to avoid repeating code for each button
        setupCategoryButton(groceriesButton)
        setupCategoryButton(homeButton)
        setupCategoryButton(transportButton)
        setupCategoryButton(giftsButton)
        setupCategoryButton(workButton)
        setupCategoryButton(foodButton)
        setupCategoryButton(entertainmentButton)
        //-------------------------------------------------------------------------//

//----------------------On click listener for back button---------------------------//
/*
*when the back button is clicked:
*set the result to RESULT_CANCELED, this informs the calling Activity that the user canceled the operation
*/
//on click listener method for the back button to return the user to the previous view when the user clicks the back button
        backButton.setOnClickListener {
            setResult(RESULT_CANCELED)
            finish()
        }
    //-------------------------------------------------//

        //---------------------on click listener for save button--------------//
        /*
             *when the save button is clicked:
             *get the new category name from the EditText
             *trim any leading/trailing whitespace
             *if the name is not empty, call addNewCategory() to insert it in the database
             *if the name is empty, show a Toast prompting the user to enter a name
             */
        //on click listener that saves the category name if it is not empty and adds it to the text box
        saveButton.setOnClickListener {
            val newCategoryName = newCategoryEditText.text.toString().trim()
            if (newCategoryName.isNotEmpty()) {
                addNewCategory(newCategoryName)
            } else { //conditional statement that prompts the user to enter a category name if they want to continue with the operation
                Toast.makeText(this, "Please enter a category name", Toast.LENGTH_SHORT).show()
            }
        }
    }
    //---------------------------------------------------------//

    //----------------------setupCategoryButton method-------------------------------//
    /*
         *this method is used to set up the behavior of each category button
         *when a category button is clicked, this method will:
         *get the category name from the button's text
         *check if the category exists in the database
         *if it doesn't exist, insert it as a default category
         *return the selected category name to the calling activity
         *handle any errors during the process
         */
    private fun setupCategoryButton(button: Button) {
        button.setOnClickListener {
            val categoryName = button.text.toString()
            lifecycleScope.launch { /*This launches a new coroutine within the activity's lifecycle scope.
                                   Coroutines are used here to perform database operations
                                   without blocking the main thread
                                   the lifecycle scope ensures that this coroutine is automatically cancelled when
                                   the Activity is destroyed, preventing potential memory leaks or crashes*/
                try {
                    val exists = withContext(Dispatchers.IO) {
                        expenseDatabase.categoryDao().categoryExists(categoryName)
                    }
                    if (exists <= 0) { //if the category does not exist in the database, it will be added to it

                        withContext(Dispatchers.IO) { /*inserts the new category into the database and
                                                               using "withContext(Dispatchers.IO)" ensures the
                                                                database write operation is also performed on
                                                                a background thread to prevent blocking the main thread.*/
                            expenseDatabase.categoryDao().insertCategory(
                                Category(
                                    name = categoryName, //uses the "name" in the button
                                    isDefault = true //states that this category is must be made a default category
                                )
                            )
                        }
                    }
                    val resultIntent = Intent() //returns the selected category name to the activity

                    resultIntent.putExtra(EXTRA_SELECTED_CATEGORY, categoryName)
                    setResult(RESULT_OK, resultIntent)
                    finish() //closes this activity and returns to the previous view

                } catch (e: Exception) {//error handling if there is an error retrieving from the database
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
//---------------------------------------------------------------------------------------------//

    //---------------------------addNewCategory method----------------------//
    /*
 *this method adds a new category to the database
 *it is called when the user clicks the save button
 *the method will:
 *check if the category already exists in the database
 *if it exists, show a Toast message
 *if it doesn't exist, add it to the database
 *return the new category to the calling Activity
 *handle any errors that occur during the process
 */
    private fun addNewCategory(categoryName: String) {
        lifecycleScope.launch {
            try {
                val exists = withContext(Dispatchers.IO) {
                    expenseDatabase.categoryDao().categoryExists(categoryName)
                }
                if (exists > 0) {
                    withContext(Dispatchers.Main) {
                        Toast.makeText(
                            this@EditCategoriesView,
                            "Category '$categoryName' already exists",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                } else {
                    withContext(Dispatchers.IO) {
                        expenseDatabase.categoryDao().insertCategory(
                            Category(
                                name = categoryName,
                                isDefault = false //newly added categories are not declared as default
                            )
                        )
                    }
                    // Return result and show success message on Main thread
                    withContext(Dispatchers.Main) {
                        val resultIntent = Intent()
                        resultIntent.putExtra(EXTRA_SELECTED_CATEGORY, categoryName)
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
//------------------------------END OF FILE-------------------------------------------------------//
