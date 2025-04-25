package com.example.cashup


import com.example.cashup.R
import android.app.DatePickerDialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.text.InputType
import android.util.Log
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts.GetContent
import androidx.appcompat.app.AppCompatActivity
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class AddExpenseActivity : AppCompatActivity() {

    private lateinit var backButton: ImageButton
    private lateinit var expenseTypeInput: AutoCompleteTextView
    private lateinit var amountInput: EditText
    private lateinit var categoriesButton: Button
    private lateinit var dateInput: EditText
    private lateinit var noteInput: EditText
    private lateinit var attachReceiptButton: Button
    private lateinit var createExpenseButton: Button

    // Holds the URI of the chosen receipt (PNG only)
    private var receiptUri: Uri? = null

    // Launcher for “Pick a PNG file”
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

        // 1) Find views
        backButton = findViewById(R.id.back_button)
        expenseTypeInput = findViewById(R.id.expense_type_input)
        amountInput = findViewById(R.id.amount_input)
        categoriesButton = findViewById(R.id.categories_button)
        dateInput = findViewById(R.id.date_input)
        noteInput = findViewById(R.id.note_input)
        attachReceiptButton = findViewById(R.id.attach_file_button)
        createExpenseButton = findViewById(R.id.create_expense_button)

        // Setup back button behavior
        setupBackButton()

        // 2) Dropdown for expense types
        val types = listOf(
            "Groceries and Market",
            "Home and Maintenance",
            "Transport and Travel",
            "Gifts"
        )
        val adapter = ArrayAdapter(
            this,
            android.R.layout.simple_dropdown_item_1line,
            types
        )
        expenseTypeInput.setAdapter(adapter)
        expenseTypeInput.threshold = 1

        // 3) Only allow numbers and cents
        amountInput.inputType =
            InputType.TYPE_CLASS_NUMBER or InputType.TYPE_NUMBER_FLAG_DECIMAL

        // 4) Date picker defaulting to today
        val today = Calendar.getInstance()
        updateDateInView(today)
        dateInput.setOnClickListener { showDatePicker(today) }

        // 5) Choosing Category will direct the user to CategoryManagementView
        categoriesButton.setOnClickListener {
            //startActivity(Intent(this, editCategoriesView::class.java))
            // val intent = Intent(this, editCategoriesView::class.java)
        }

        // 6) Attach receipt (PNG only)
        attachReceiptButton.setOnClickListener {
            pickPngLauncher.launch("image/png")
        }

        // 7) Create expense
        createExpenseButton.setOnClickListener {
            if (validateInputs()) {
                // Build an Expense object
                val expense = Expense(
                    type = expenseTypeInput.text.toString(),
                    amount = amountInput.text.toString().toDouble(),
                    date = dateInput.text.toString(),
                    note = noteInput.text.toString().takeIf { it.isNotBlank() },
                    receiptUri = receiptUri
                )

                /** will wait for the db**/
                Log.d("ExpenseCreation", "Saving expense: $expense")

                // Navigate back to the expense list

                //startActivity(Intent(this, HomepageActivity::class.java))
                finish()
            }
        }
    }

    /**
     * clicking the back button so clicking closes this screen and redirects the user to the expense list.
     */
    private fun setupBackButton() {
        backButton.setOnClickListener {

            finish()

            //  TODO: once ExpenseListActivity exists, will need to uncomment:
            /*
            val intent = Intent(this, MainExpenseListActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
            startActivity(intent)
            */
        }
    }

    /**
     * Validates all user inputs.
     * @return true if all fields pass.
     */
    private fun validateInputs(): Boolean {
        var ok = true

        if (expenseTypeInput.text.isBlank()) {
            expenseTypeInput.error = "Select an expense type"
            ok = false
        }
        val amtText = amountInput.text.toString()
        if (amtText.isBlank() ||
            amtText.toDoubleOrNull().let { it == null || it <= 0.0 }
        ) {
            amountInput.error = "Enter a valid amount"
            ok = false
        }
        if (dateInput.text.isBlank()) {
            dateInput.error = "Pick a date"
            ok = false
        }
        // receiptUri is optional
        return ok
    }

    /**
     * Displays the DatePicker dialog and updates the date field.
     */
    private fun showDatePicker(calendar: Calendar) {
        DatePickerDialog(
            this,
            { _, year, month, dayOfMonth ->
                calendar.set(year, month, dayOfMonth)
                updateDateInView(calendar)
            },
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        ).show()
    }

    /**
     * Formats the given Calendar as dd/MM/yyyy in dateInput.
     */
    private fun updateDateInView(cal: Calendar) {
        val sdf = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
        dateInput.setText(sdf.format(cal.time))
    }


    /**
     * Simple data class to represent an Expense.
     */
    data class Expense(
        val type: String,
        val amount: Double,
        val date: String,
        val note: String?,
        val receiptUri: Uri?
    )

}

//------------------------------------------END OF FILE-------------------------------//