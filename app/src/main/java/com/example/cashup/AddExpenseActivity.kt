

package com.example.cashup




//---------------------------------- START OF IMPORTS ---------------------------------------//

import android.app.DatePickerDialog

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

import com.example.cashup.Database.Expense // DATABASE

import com.example.cashup.Database.ExpenseDatabase // ""

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




    // Launcher for "Pick a PNG file"
    private val pickPngLauncher = registerForActivityResult(GetContent()) { uri: Uri? ->
        uri?.let {
            val mime = contentResolver.getType(it)
            if (mime == "image/png") {
                receiptUri = it
                Toast.makeText(this, "PNG receipt attached", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "Only PNG files are allowed", Toast.LENGTH_SHORT).show()
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

        noteInput = findViewById(R.id.note_input)

        attachReceiptButton = findViewById(R.id.attach_file_button)

        createExpenseButton = findViewById(R.id.create_expense_button)


     //////////////////////////////////////////////////////////////////////////////////////////////////////////



        backButton.setOnClickListener { finish() }


        amountInput.inputType = InputType.TYPE_CLASS_NUMBER or InputType.TYPE_NUMBER_FLAG_DECIMAL


        val today = Calendar.getInstance()
        updateDateInView(today)
        dateInput.setOnClickListener { showDatePicker(today) }


        categoriesButton.setOnClickListener {
            /*
            *
            *
            *
            *
            *
            * CARLOS IMPLEMENT LOGIC HERE
            *
            *
            *
            *
            *
            *
            * */
        }

        // Attach receipt button handling
        attachReceiptButton.setOnClickListener {
            pickPngLauncher.launch("image/png")
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

    //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    /*
    *
    *
    *
    *
    * WE NEED LOGIC FOR THE BELOW FUNCTION
    *
    * WE NEED TO INCREMENT USERS
    *
    * CURRENT USER IS SET TO 1 SO THEY ALL SHOW UP INSTEAD OF RELEVANT TO USER
    *
    *
    *
    *
    *
    *
    *
    *
    * */


    private fun saveExpense() {

        val userId = getCurrentUserId() // CURRENT USER IS OUR PROBLEM







////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////



        val expenseDate = parseDate(dateInput.text.toString())

        // database expense object
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

                Toast.makeText(this@AddExpenseActivity, "Expense saved", Toast.LENGTH_SHORT).show()

                finish()
            } catch (e: Exception) {

                Log.e("AddExpenseActivity", "Error saving expense", e)

                withContext(Dispatchers.Main) {

                    Toast.makeText(this@AddExpenseActivity, "Error: ${e.message}", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////


    /*
    *
    *
    *
    *
    *
    * WE NEED LOGIC FOR THIS PART
    *
    * BELOW IS THE PROBLEM
    *
    *
    *
    *
    *
    *
    *
    * */
    private fun getCurrentUserId(): Int {

        return 1 // SET TO 1. WE NEED INDEX
    }

  ////////////////////////////////////////////////////////////////////////////////////////////////////////////
}
//***************************************************** START OF CODE ***********************************************************//