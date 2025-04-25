// START OF FILE: AddExpenseActivity.kt
package com.example.cashup

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

class AddExpenseActivity : AppCompatActivity() {

    // --- View references ---
    private lateinit var backButton: ImageButton
    private lateinit var expenseTypeInput: EditText
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

    // ------------------------------
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Inflate layout
        setContentView(R.layout.activity_add_expense)

        //  Bind views
        backButton = findViewById(R.id.back_button)
        expenseTypeInput = findViewById(R.id.expense_type_input)
        amountInput = findViewById(R.id.amount_input)
        categoriesButton = findViewById(R.id.categories_button)
        dateInput = findViewById(R.id.date_input)
        noteInput = findViewById(R.id.note_input)
        attachReceiptButton = findViewById(R.id.attach_file_button)
        createExpenseButton = findViewById(R.id.create_expense_button)

        // Back button closes activity
        backButton.setOnClickListener { finish() }

        // Amount field: numbers + decimal
        amountInput.inputType = InputType.TYPE_CLASS_NUMBER or InputType.TYPE_NUMBER_FLAG_DECIMAL

        // Date picker setup
        val today = Calendar.getInstance()
        updateDateInView(today)
        dateInput.setOnClickListener { showDatePicker(today) }

        // Category navigation stub
        categoriesButton.setOnClickListener {
            // TODO: launch your category screen
        }

        // Attach receipt button handling
        attachReceiptButton.setOnClickListener {
            pickPngLauncher.launch("image/png")
        }

        // Create expense: validation + log
        createExpenseButton.setOnClickListener {
            if (validateInputs()) {
                val expense = Expense(
                    type = expenseTypeInput.text.toString(),
                    amount = amountInput.text.toString().toDouble(),
                    date = dateInput.text.toString(),
                    note = noteInput.text.toString().takeIf { it.isNotBlank() },
                    receiptUri = receiptUri
                )
                Log.d("AddExpenseActivity", "Saving expense: $expense")
                finish()
            }
        }
    }

    // ------------------------------
    /** Validates form the users inputs before saving */
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

    /** Show a date picker dialog */
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

    /** Update date field in dd/MM/yyyy format */
    private fun updateDateInView(cal: Calendar) {
        val sdf = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
        dateInput.setText(sdf.format(cal.time))
    }


    /** Data class representing an Expense */
    data class Expense(
        val type: String,
        val amount: Double,
        val date: String,
        val note: String?,
        val receiptUri: Uri?
    )

} // --------------------END OF CLASS-------------------------------


