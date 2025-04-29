package com.example.cashup

//---------------------------------- START OF IMPORTS ---------------------------------------//

import android.app.Activity
import android.app.DatePickerDialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.text.InputType
import android.util.Log
import android.widget.*
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import java.text.SimpleDateFormat
import java.util.*
import androidx.lifecycle.lifecycleScope
import com.example.cashup.Database.Expense
import com.example.cashup.Database.ExpenseDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

//---------------------------------- END OF IMPORTS ---------------------------------------//


/**Decleration for AI usage
 * Nature : How does contentResolver.takePersistableUriPermission() work in Android?
 * AI used: ChatGPT
 * Link for chat: https://chatgpt.com/share/6811430a-9314-800e-a4db-573f9e7c79a8
 * */


//***************************************************** START OF CODE ***********************************************************//

class AddExpenseActivity : AppCompatActivity() {

    //--------------------------- START OF GLOBAL VARIABLES -------------------------//

    ////////////////////// UI VARIABLES ////////////////////////

    private lateinit var backButton: ImageButton
    private lateinit var expenseTypeInput: EditText
    private lateinit var amountInput: EditText
    private lateinit var categoriesButton: Button // This button displays the chosen category
    private lateinit var dateInput: EditText
    private lateinit var noteInput: EditText
    private lateinit var attachReceiptButton: Button
    private lateinit var createExpenseButton: Button

    ////////////////////////////////////////////////////////

    private var receiptUri: Uri? = null // IMAGE VARIABLE
    private var selectedCategoryName: String? = null // Variable to store the chosen category

    // **************************** DATABASE VARIABLES *********************************//

    private lateinit var expenseDatabase: ExpenseDatabase

    // --- Activity Result Launcher for Category Selection ---
    private lateinit var categoryLauncher: ActivityResultLauncher<Intent>

    //--------------------------- END OF GLOBAL VARIABLES -------------------------//

    // Image picker launcher
    private val pickImageLauncher = registerForActivityResult(
        ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        uri?.let {
            try {
                Log.d("AddExpenseActivity", "Selected image URI: $it")
                val takeFlags: Int = Intent.FLAG_GRANT_READ_URI_PERMISSION
                contentResolver.takePersistableUriPermission(it, takeFlags)
                Log.d(
                    "AddExpenseActivity",
                    "Successfully took persistent permission"
                )
                receiptUri = it
                Toast.makeText(
                    this,
                    "Receipt attached successfully",
                    Toast.LENGTH_SHORT
                ).show()
                contentResolver.openInputStream(it)?.use { stream ->
                    Log.d(
                        "AddExpenseActivity",
                        "Successfully verified content access"
                    )
                }
            } catch (e: SecurityException) {
                Log.e(
                    "AddExpenseActivity",
                    "Permission error: ${e.message}",
                    e
                )
                Toast.makeText(
                    this,
                    "Error securing image access: ${e.message}",
                    Toast.LENGTH_SHORT
                ).show()
                receiptUri = it // Still save URI
            } catch (e: Exception) {
                Log.e(
                    "AddExpenseActivity",
                    "Error handling image: ${e.message}",
                    e
                )
                Toast.makeText(
                    this,
                    "Error handling image: ${e.message}",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }
////////////////////////////////////////////////////////////////////////////////////////////////////

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_expense) // Ensure this layout name is correct

        expenseDatabase = ExpenseDatabase.getDatabase(this) // Initialisation of the database

        // --- Initialize Activity Result Launcher ---
        categoryLauncher = registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                // Get the selected category name back from EditCategoriesView
                val data: Intent? = result.data
                val category =
                    data?.getStringExtra(EditCategoriesView.EXTRA_SELECTED_CATEGORY)
                if (category != null) {
                    selectedCategoryName = category
                    categoriesButton.text = selectedCategoryName
                    categoriesButton.error = null // Clear potential error on button
                } else {
                    Log.w(
                        "AddExpenseActivity",
                        "Received RESULT_OK but no category data"
                    )
                }
            } else {
                Log.d(
                    "AddExpenseActivity",
                    "Category selection cancelled or failed"
                )
            }
        }
        // --- End of Launcher Initialization ---

        ///////////////////////////////////////////////// BINDING /////////////////////////////////////////
        backButton = findViewById(R.id.back_button)
        expenseTypeInput = findViewById(R.id.expense_type_input)
        amountInput = findViewById(R.id.amount_input)
        categoriesButton = findViewById(R.id.categories_button) // This button triggers selection
        dateInput = findViewById(R.id.date_input)
        noteInput = findViewById(R.id.note_input)
        attachReceiptButton = findViewById(R.id.attach_file_button)
        createExpenseButton = findViewById(R.id.create_expense_button)
        //////////////////////////////////////////////////////////////////////////////////////////////////////////

        backButton.setOnClickListener { finish() }

        amountInput.inputType =
            InputType.TYPE_CLASS_NUMBER or InputType.TYPE_NUMBER_FLAG_DECIMAL

        val today = Calendar.getInstance() // sets default date to current date
        updateDateInView(today)
        dateInput.isFocusable = false // Prevent keyboard, allow click
        dateInput.isClickable = true
        dateInput.setOnClickListener { showDatePicker(today) }

        // Launch EditCategoriesView for result
        categoriesButton.setOnClickListener {
            val intent = Intent(this, EditCategoriesView::class.java)
            categoryLauncher.launch(intent) // Use the launcher
        }

        attachReceiptButton.setOnClickListener {
            try {
                pickImageLauncher.launch("image/*")
            } catch (e: Exception) {
                Log.e(
                    "AddExpenseActivity",
                    "Error launching image picker: ${e.message}",
                    e
                )
                Toast.makeText(
                    this,
                    "Error launching image picker",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }

        createExpenseButton.setOnClickListener {  //Create expense on click if input is valid
            if (validateInputs()) {
                saveExpense()
            }
        }
    }

    // --- Validate category selection and type input separately ---
    private fun validateInputs(): Boolean {
        var ok = true

        // Validate category selection via the button
        if (selectedCategoryName.isNullOrBlank()) {
            categoriesButton.error = "Please choose a category"
            ok = false
        } else {
            categoriesButton.error = null // Clear error if selected
        }

        // Validate the separate expense type/description field
        if (expenseTypeInput.text.isBlank()) {
            expenseTypeInput.error = "Expense description missing" // Adjusted error message
            ok = false
        } else {
            expenseTypeInput.error = null // Clear error if text is entered
        }

        val amt = amountInput.text.toString().toDoubleOrNull()
        if (amt == null || amt <= 0.0) {
            amountInput.error = "Enter a valid amount"
            ok = false
        } else {
            amountInput.error = null
        }

        if (dateInput.text.isBlank()) {
            dateInput.error = "Pick a date"
            ok = false
        } else {
            dateInput.error = null
        }
        return ok
    }


    private fun showDatePicker(calendar: Calendar) {
        DatePickerDialog(
            this,
            { _, year, month, day ->
                calendar.set(year, month, day)
                updateDateInView(calendar)
                dateInput.error = null // Clear error after picking date
            },
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        ).show()
    }

    private fun updateDateInView(cal: Calendar) {
        val sdf = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
        dateInput.setText(sdf.format(cal.time))
    }

    private fun parseDate(dateString: String): Date {
        return try {
            val sdf = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
            // Set lenient to false to avoid parsing invalid dates like 31/02/2024
            sdf.isLenient = false
            sdf.parse(dateString) ?: Date() // Use current date as fallback
        } catch (e: Exception) {
            Log.e("AddExpenseActivity", "Error parsing date: $dateString", e)
            Date() // Return current date on parsing error
        }
    }

    // --- Uses selectedCategoryName for category and input text for type ---
    private fun saveExpense() {
        // Ensure category is selected (double check, though validateInputs should catch it)
        if (selectedCategoryName.isNullOrBlank()) {
            Toast.makeText(
                this,
                "Cannot save without a category",
                Toast.LENGTH_SHORT
            ).show()
            if (expenseTypeInput.text.isBlank()) {
                Toast.makeText(
                    this,
                    "Cannot save without expense description", // Adjusted message
                    Toast.LENGTH_SHORT
                ).show()
                return
            }
            return // Return if category is missing
        }
        // Ensure type field is not blank (double check)
        if (expenseTypeInput.text.isBlank()) {
            Toast.makeText(
                this,
                "Cannot save without expense description", // Adjusted message
                Toast.LENGTH_SHORT
            ).show()
            return
        }


        val userId = getCurrentUserId()
        val expenseDate = parseDate(dateInput.text.toString())
        val expenseAmountText = amountInput.text.toString()
        val expenseNotes = noteInput.text.toString().takeIf { it.isNotBlank() }

        // Ensure amount is valid before creating Expense object
        val expenseAmount = expenseAmountText.toDoubleOrNull()
        if (expenseAmount == null) {
            amountInput.error = "Invalid amount format"
            Toast.makeText(this, "Invalid amount format", Toast.LENGTH_SHORT)
                .show()
            return
        }

        receiptUri?.let { uri ->
            try {
                val persistedUriPermissions =
                    contentResolver.persistedUriPermissions
                val alreadyHasPermission =
                    persistedUriPermissions.any { it.uri == uri }
                if (!alreadyHasPermission) {
                    contentResolver.takePersistableUriPermission(
                        uri,
                        Intent.FLAG_GRANT_READ_URI_PERMISSION
                    )
                    Log.d(
                        "AddExpenseActivity",
                        "Successfully took persistent permission in saveExpense"
                    )
                }
                contentResolver.openInputStream(uri)?.use {
                    Log.d(
                        "AddExpenseActivity",
                        "Verified URI is accessible before saving"
                    )
                }
            } catch (e: Exception) {
                Log.e(
                    "AddExpenseActivity",
                    "Error with URI permission before saving: ${e.message}",
                    e
                )
                Toast.makeText(
                    this,
                    "Warning: Receipt may not be viewable later",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }

        val expense = Expense(
            userId = userId,
            // Use the text manually entered by the user for 'type'
            type = expenseTypeInput.text.toString(),
            amount = expenseAmount,
            notes = expenseNotes,
            receiptUri = receiptUri?.toString(),
            startDate = expenseDate,
            endDate = expenseDate,
            createdAt = Date(),
            // Use the category selected via the button for 'category'
            category = selectedCategoryName
        )

        // Save to database using coroutine
        lifecycleScope.launch {
            try {
                withContext(Dispatchers.IO) {
                    expenseDatabase.expenseDao().insertExpense(expense)
                }
                Log.d(
                    "AddExpenseActivity",
                    "Saved expense to database: $expense"
                )
                Toast.makeText(
                    this@AddExpenseActivity,
                    "Expense saved successfully",
                    Toast.LENGTH_SHORT
                ).show()
                finish() // Close activity after successful save
            } catch (e: Exception) {
                Log.e("AddExpenseActivity", "Error saving expense", e)
                withContext(Dispatchers.Main) {
                    Toast.makeText(
                        this@AddExpenseActivity,
                        "Error saving expense: ${e.message}",
                        Toast.LENGTH_LONG
                    ).show()
                }
            }
        }
    }

    private fun getCurrentUserId(): Int { //returns a userID
        return 1
    }
}
//***************************************************** END OF CODE ***********************************************************//
