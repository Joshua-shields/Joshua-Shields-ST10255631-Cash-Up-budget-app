package com.example.cashup // Make sure this matches your actual package

import android.app.DatePickerDialog
import android.os.Bundle
import android.text.InputType
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class ExpenseCreation : AppCompatActivity() {

    private lateinit var groceriesInput: AutoCompleteTextView
    private lateinit var amountInput: EditText
    private lateinit var dateInput: EditText
    private lateinit var noteInput: EditText
    private lateinit var createButton: Button

    // Define the date format consistently
    private val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.expense_creation) // Ensure this layout exists

        // Find your views
        groceriesInput = findViewById(R.id.groceries_input)
        amountInput    = findViewById(R.id.amount_input)
        dateInput      = findViewById(R.id.date_input)
        noteInput      = findViewById(R.id.note_input)
        createButton   = findViewById(R.id.create_button)

        // Set up the dropdown for expense types
        val expenseTypes = listOf(
            "Groceries and Market",
            "Home and Maintenance",
            "Transport and Travel",
            "Gifts"
            // Add more types as needed
        )
        val adapter = ArrayAdapter(
            this,
            android.R.layout.simple_dropdown_item_1line,
            expenseTypes
        )
        groceriesInput.setAdapter(adapter)
        groceriesInput.threshold = 1 // Start showing suggestions after 1 character

        // 3. Ensure amount allows decimal entry
        amountInput.inputType =
            InputType.TYPE_CLASS_NUMBER or InputType.TYPE_NUMBER_FLAG_DECIMAL

        // 4. Pre-fill date with today, hook up DatePicker
        val todayCalendar = Calendar.getInstance()
        updateDateInView(todayCalendar) // Set initial date

        // Make date field non-editable directly, only via picker
        dateInput.isFocusable = false
        dateInput.isClickable = true

        dateInput.setOnClickListener {
            val calendar = Calendar.getInstance()
            // Try parsing the current date text to set the picker's initial date
            try {
                val currentDate = dateFormat.parse(dateInput.text.toString())
                if (currentDate != null) {
                    calendar.time = currentDate
                }
            } catch (e: ParseException) {
                // Keep calendar as today if parsing fails
            }

            DatePickerDialog(
                this,
                { _, year, month, dayOfMonth ->
                    // Month is 0-based, Calendar handles it correctly
                    val selectedCalendar = Calendar.getInstance().apply {
                        set(year, month, dayOfMonth)
                    }
                    updateDateInView(selectedCalendar)
                },
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
            ).show()
        }

        //

        // 6.
        createButton.setOnClickListener {
            // --- Validation Start ---
            // 1. Capture & trim input
            val typeText = groceriesInput.text.toString().trim()
            val amountText = amountInput.text.toString().trim()
            val dateText = dateInput.text.toString().trim()
            val noteText = noteInput.text.toString().trim()

            // Clear previous errors
            groceriesInput.error = null
            amountInput.error = null
            dateInput.error = null

            // 2. Validate expense type
            if (typeText.isEmpty()) {
                groceriesInput.error = "Please select or enter an expense type"
                groceriesInput.requestFocus()
                return@setOnClickListener // Stop processing
            }

            // 3. Validate amount (non-empty, numeric, > 0)
            if (amountText.isEmpty()) {
                amountInput.error = "Enter an amount"
                amountInput.requestFocus()
                return@setOnClickListener
            }
            // Use toDoubleOrNull for safe conversion
            val amountValue = amountText.toDoubleOrNull()
            if (amountValue == null || amountValue <= 0.0) {
                amountInput.error = "Enter a valid positive amount"
                amountInput.requestFocus()
                return@setOnClickListener
            }

            // Validate date (non-empty + correct format dd/MM/yyyy)
            if (dateText.isEmpty()) {
                // This shouldn't happen if we pre-fill, but good practice
                dateInput.error = "Please pick a date"
                // Since it's not focusable, maybe focus amount instead or just show error
                amountInput.requestFocus()
                return@setOnClickListener
            }
            // Use the same date format instance
            dateFormat.isLenient = false // Strict parsing
            try {
                dateFormat.parse(dateText) // Just try parsing to validate
            } catch (e: ParseException) {
                dateInput.error = "Invalid date format (dd/MM/yyyy)"
                amountInput.requestFocus() // Focus previous field
                return@setOnClickListener
            }

            // get value or null if empty
            val noteValue: String? = noteText.takeIf { it.isNotEmpty() }

            // --- Validation End ---

            // All inputs are valid—Call the submission logic HERE
            submitExpense(
                type   = typeText,
                amount = amountValue, // Pass the validated Double
                date   = dateText,    // Pass the validated String date
                note   = noteValue    // Pass the optional String
            )

            clearInputFields()


        }
    } // End of onCreate

    /**
     * Updates the dateInput EditText with the formatted date.
     */
    private fun updateDateInView(cal: Calendar) {
        dateInput.setText(dateFormat.format(cal.time))
        // Clear error after updating date
        dateInput.error = null
    }

    /**
     * Placeholder function to handle the validated expense data.
     * Replace the Toast with your actual logic (save to DB, ViewModel, etc.)
     */
    private fun submitExpense(type: String, amount: Double, date: String, note: String?) {
        // TODO: Implement your actual saving logic here
        // For example, save to Room database, send to ViewModel, call API

        val noteDisplay = note ?: "No note" // Handle null note for display
        val message = "Expense Saved:\nType: $type\nAmount: $amount\nDate: $date\nNote: $noteDisplay"
        Toast.makeText(this, message, Toast.LENGTH_LONG).show()


    }

    /**
     * Helper function to clear all input fields and errors.
     */
    private fun clearInputFields() {
        groceriesInput.text.clear()
        amountInput.text.clear()
        updateDateInView(Calendar.getInstance())
        noteInput.text.clear()

        // Clear errors
        groceriesInput.error = null
        amountInput.error = null
        dateInput.error = null
        noteInput.error = null // Although note doesn't have validation here

        // Set focus back to the first field
        groceriesInput.requestFocus()
    }
}
