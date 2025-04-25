package com.example.cashup

import android.app.DatePickerDialog
import android.net.Uri
import android.os.Bundle
import android.text.InputType
import android.util.Log
import android.view.View
import android.widget.*
import androidx.activity.result.contract.ActivityResultContracts.GetContent
import androidx.appcompat.app.AppCompatActivity
import java.text.SimpleDateFormat
import java.util.*

class AddExpenseActivity : AppCompatActivity() {

    private lateinit var backButton: ImageButton
    private lateinit var expenseTypeSpinner: Spinner
    private lateinit var amountInput: EditText
    private lateinit var categoriesButton: Button
    private lateinit var dateInput: EditText
    private lateinit var noteInput: EditText
    private lateinit var attachReceiptButton: Button
    private lateinit var createExpenseButton: Button

    //  chosen receipt (PNG only)
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
        // The XML layout
        setContentView(R.layout.activity_add_expense)

        // Find every view by its ID
        backButton          = findViewById(R.id.back_button)
        expenseTypeSpinner  = findViewById(R.id.expense_type_spinner)
        amountInput         = findViewById(R.id.amount_input)
        categoriesButton    = findViewById(R.id.categories_button)
        dateInput           = findViewById(R.id.date_input)
        noteInput           = findViewById(R.id.note_input)
        attachReceiptButton = findViewById(R.id.attach_file_button)
        createExpenseButton = findViewById(R.id.create_expense_button)

        //  Configures the Spinner with adapter for custom white‐text layouts.
        val expenseOptions = resources.getStringArray(R.array.expense_type_options)
        val spinnerAdapter = ArrayAdapter(
            this,
            R.layout.spinner_item,               // closed (selected) view
            expenseOptions
        ).also {
            it.setDropDownViewResource(R.layout.spinner_dropdown_item)  // dropdown items
        }
        expenseTypeSpinner.adapter = spinnerAdapter
        expenseTypeSpinner.setSelection(0)  // show “Choose expense” initially

        // Optional react whenever the user picks an option
        expenseTypeSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View, pos: Int, id: Long) {
                // val selectedType = parent.getItemAtPosition(pos).toString()
            }
            override fun onNothingSelected(parent: AdapterView<*>) { /* no-op */ }
        }

        //  Back button: close this Activity
        backButton.setOnClickListener { finish() }

        //  Amount field: numbers + decimal only
        amountInput.inputType = InputType.TYPE_CLASS_NUMBER or InputType.TYPE_NUMBER_FLAG_DECIMAL

        // Date picker defaulting to today
        val today = Calendar.getInstance()
        updateDateInView(today)
        dateInput.setOnClickListener { showDatePicker(today) }

        // Choose Category navigation
        categoriesButton.setOnClickListener {
            // TODO: launch your CategoryManagementActivity
        }

        // Attach receipt (PNG only)
        attachReceiptButton.setOnClickListener {
            pickPngLauncher.launch("image/png")
        }

        // Creates expense: validate & log
        createExpenseButton.setOnClickListener {
            if (validateInputs()) {
                val expense = Expense(
                    type       = expenseTypeSpinner.selectedItem.toString(),
                    amount     = amountInput.text.toString().toDouble(),
                    date       = dateInput.text.toString(),
                    note       = noteInput.text.toString().takeIf { it.isNotBlank() },
                    receiptUri = receiptUri
                )
                Log.d("AddExpenseActivity", "Saving expense: $expense")
                finish()
            }
        }
    }

    /** Checks the Amount and Date are fields filled out correctly. */
    private fun validateInputs(): Boolean {
        var ok = true
        val amtText = amountInput.text.toString()
        if (amtText.isBlank() || amtText.toDoubleOrNull()?.let { it <= 0.0 } == true) {
            amountInput.error = "Enter a valid amount"
            ok = false
        }
        if (dateInput.text.isBlank()) {
            dateInput.error = "Pick a date"
            ok = false
        }
        return ok
    }

    /** Show a date picker and update the date field. */
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

    /** Format the Calendar as dd/MM/yyyy into dateInput. */
    private fun updateDateInView(cal: Calendar) {
        val sdf = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
        dateInput.setText(sdf.format(cal.time))
    }

    /** Simple data class for your expense object. */
    data class Expense(
        val type: String,
        val amount: Double,
        val date: String,
        val note: String?,
        val receiptUri: Uri?
    )
}
//------------------------------------------END OF FILE-------------------------------//
