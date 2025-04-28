package com.example.cashup

//---------------------------------- START OF IMPORTS ---------------------------------------//

import android.app.DatePickerDialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.text.InputType
import android.util.Log
import android.widget.*
import androidx.activity.result.contract.ActivityResultContracts.GetContent
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

//***************************************************** START OF CODE ***********************************************************//

class AddExpenseActivity : AppCompatActivity() {

    //--------------------------- START OF GLOBAL VARIABLES -------------------------//

    ////////////////////// UI VARIABLES ////////////////////////

    private lateinit var backButton: ImageButton
    private lateinit var expenseTypeInput: EditText
    private lateinit var amountInput: EditText
    private lateinit var categoriesButton: Button
    private lateinit var dateInput: EditText
    private lateinit var noteInput: EditText
    private lateinit var attachReceiptButton: Button
    private lateinit var createExpenseButton: Button

    ////////////////////////////////////////////////////////

    private var receiptUri: Uri? = null // IMAGE VARIABLE

    // **************************** DATABASE VARIABLES *********************************//

    private lateinit var expenseDatabase: ExpenseDatabase

    //--------------------------- END OF GLOBAL VARIABLES -------------------------//

    // Improved image picker launcher with better error handling
    private val pickImageLauncher = registerForActivityResult(GetContent()) { uri: Uri? ->
        uri?.let {
            try {
                Log.d("AddExpenseActivity", "Selected image URI: $it")

                // Check if we already have permission for this URI
                val persistedUriPermissions = contentResolver.persistedUriPermissions
                val alreadyHasPermission = persistedUriPermissions.any { perm -> perm.uri == it }

                if (!alreadyHasPermission) {
                    // Take persistent permission for future access
                    contentResolver.takePersistableUriPermission(
                        it,
                        Intent.FLAG_GRANT_READ_URI_PERMISSION
                    )
                    Log.d("AddExpenseActivity", "Successfully took persistent permission")
                } else {
                    Log.d("AddExpenseActivity", "Already had permission for this URI")
                }

                receiptUri = it
                Toast.makeText(this, "Receipt attached successfully", Toast.LENGTH_SHORT).show()

                // Verify we can access the content (good for debugging)
                contentResolver.openInputStream(it)?.use { stream ->
                    Log.d("AddExpenseActivity", "Successfully verified content access")
                }

            } catch (e: SecurityException) {
                Log.e("AddExpenseActivity", "Permission error: ${e.message}", e)
                Toast.makeText(this, "Error securing image access: ${e.message}", Toast.LENGTH_SHORT).show()
                // Still save the URI, but warn the user
                receiptUri = it
                Toast.makeText(this, "Image might not be accessible later", Toast.LENGTH_SHORT).show()
            } catch (e: Exception) {
                Log.e("AddExpenseActivity", "Error handling image: ${e.message}", e)
                Toast.makeText(this, "Error handling image: ${e.message}", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_add_expense)

        //  SETUP
        expenseDatabase = ExpenseDatabase.getDatabase(this)

        ///////////////////////////////////////////////// BINDING /////////////////////////////////////////

        backButton = findViewById(R.id.back_button)
        expenseTypeInput = findViewById(R.id.expense_type_input)
        amountInput = findViewById(R.id.amount_input)
        categoriesButton = findViewById(R.id.categories_button)
        dateInput = findViewById(R.id.date_input)
        attachReceiptButton = findViewById(R.id.attach_file_button)
        createExpenseButton = findViewById(R.id.create_expense_button)

        //////////////////////////////////////////////////////////////////////////////////////////////////////////

        backButton.setOnClickListener { finish() }

        amountInput.inputType = InputType.TYPE_CLASS_NUMBER or InputType.TYPE_NUMBER_FLAG_DECIMAL

        val today = Calendar.getInstance()
        updateDateInView(today)
        dateInput.setOnClickListener { showDatePicker(today) }

        categoriesButton.setOnClickListener {
            // Navigate to categories screen
            val intent = Intent(this, EditCategoriesView::class.java)
            startActivity(intent)
        }

        // Improved image picker handling
        attachReceiptButton.setOnClickListener {
            try {
                pickImageLauncher.launch("image/*")
            } catch (e: Exception) {
                Log.e("AddExpenseActivity", "Error launching image picker: ${e.message}", e)
                Toast.makeText(this, "Error launching image picker", Toast.LENGTH_SHORT).show()
            }
        }

        // Create expense: validation + log
        createExpenseButton.setOnClickListener {
            if (validateInputs()) {
                saveExpense()
            }
        }
    }

    private fun validateInputs(): Boolean {
        var ok = true
        if (expenseTypeInput.text.isBlank()) {
            expenseTypeInput.error = "Enter an expense type"
            ok = false
        }
        val amt = amountInput.text.toString().toDoubleOrNull()
        if (amt == null || amt <= 0.0) {
            amountInput.error = "Enter a valid amount"
            ok = false
        }
        if (dateInput.text.isBlank()) {
            dateInput.error = "Pick a date"
            ok = false
        }
        return ok
    }

    private fun showDatePicker(calendar: Calendar) {
        DatePickerDialog(
            this,
            { _, year, month, day ->
                calendar.set(year, month, day)
                updateDateInView(calendar)
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
        val sdf = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
        return sdf.parse(dateString) ?: Date()
    }

    private fun saveExpense() {
        val userId = getCurrentUserId()
        val expenseDate = parseDate(dateInput.text.toString())

        // Improved URI permission handling
        receiptUri?.let { uri ->
            try {
                // Check if we already have permission for this URI
                val persistedUriPermissions = contentResolver.persistedUriPermissions
                val alreadyHasPermission = persistedUriPermissions.any { it.uri == uri }

                if (!alreadyHasPermission) {
                    // Take persistent permission for future access
                    contentResolver.takePersistableUriPermission(
                        uri,
                        Intent.FLAG_GRANT_READ_URI_PERMISSION
                    )
                    Log.d("AddExpenseActivity", "Successfully took persistent permission in saveExpense")
                }

                // Verify we can still access the content
                contentResolver.openInputStream(uri)?.use {
                    Log.d("AddExpenseActivity", "Verified URI is accessible before saving")
                }

            } catch (e: Exception) {
                Log.e("AddExpenseActivity", "Error with URI permission before saving: ${e.message}", e)
                Toast.makeText(this, "Warning: Receipt may not be viewable later", Toast.LENGTH_SHORT).show()
                // Continue with saving - the URI might still work
            }
        }

        // Database expense object
        val expense = Expense(
            userId = userId,
            type = expenseTypeInput.text.toString(),
            amount = amountInput.text.toString().toDouble(),
            notes = noteInput.text.toString().takeIf { it.isNotBlank() },
            receiptUri = receiptUri?.toString(),
            startDate = expenseDate,
            endDate = expenseDate, // For single-day expenses, start and end are the same
            createdAt = Date() // Current timestamp
        )

        // Save to database using coroutine
        lifecycleScope.launch {
            try {
                withContext(Dispatchers.IO) {
                    expenseDatabase.expenseDao().insertExpense(expense)
                }
                Log.d("AddExpenseActivity", "Saved expense to database: $expense")
                Toast.makeText(this@AddExpenseActivity, "Expense saved successfully", Toast.LENGTH_SHORT).show()
                finish()
            } catch (e: Exception) {
                Log.e("AddExpenseActivity", "Error saving expense", e)
                withContext(Dispatchers.Main) {
                    Toast.makeText(
                        this@AddExpenseActivity,
                        "Error saving expense: ${e.message}",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }
    }

    private fun getCurrentUserId(): Int {
        // TODO: Implement proper user ID retrieval
        return 1 // Currently hardcoded to user ID 1
    }
}
//***************************************************** END OF CODE ***********************************************************//
