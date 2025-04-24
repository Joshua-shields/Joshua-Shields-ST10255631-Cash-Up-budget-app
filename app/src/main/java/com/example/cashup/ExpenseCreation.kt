package com.example.cashup

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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.expense_creation)

        // 1. Find your views
        groceriesInput = findViewById(R.id.groceries_input)
        amountInput    = findViewById(R.id.amount_input)
        dateInput      = findViewById(R.id.date_input)
        noteInput      = findViewById(R.id.note_input)
        createButton   = findViewById(R.id.create_button)

        // 2. Set up the dropdown for expense types
        val expenseTypes = listOf(
            "Groceries and Market",
            "Home and Maintenance",
            "Transport and Travel",
            "Gifts"
        )
        val adapter = ArrayAdapter(
            this,
            android.R.layout.simple_dropdown_item_1line,
            expenseTypes
        )
        groceriesInput.setAdapter(adapter)
        groceriesInput.threshold = 1  // start showing after 1 char

        // 3. Ensure amount allows decimal entry
        amountInput.inputType =
            InputType.TYPE_CLASS_NUMBER or InputType.TYPE_NUMBER_FLAG_DECIMAL

        // 4. Pre-fill date with today, hook up DatePicker
        val today = Calendar.getInstance()
        updateDateInView(today)
        dateInput.setOnClickListener {
            DatePickerDialog(
                this,
                { _, year, month, dayOfMonth ->
                    Calendar.getInstance().apply {
                        set(year, month, dayOfMonth)
                        updateDateInView(this)
                    }
                },
                today.get(Calendar.YEAR),
                today.get(Calendar.MONTH),
                today.get(Calendar.DAY_OF_MONTH)
            ).show()
        }

        // 5. noteInput is already editable and optional—no extra code needed

        // 6. (Optional) Handle your “Create” button
        createButton.setOnClickListener {
            // 1. Capture & trim
            val typeText = groceriesInput.text.toString().trim()
            val amountText = amountInput.text.toString().trim()
            val dateText = dateInput.text.toString().trim()
            val noteText = noteInput.text.toString().trim()

            // 2. Validate expense type
            if (typeText.isEmpty()) {
                groceriesInput.error = "Please select an expense type"
                groceriesInput.requestFocus()
                return@setOnClickListener
            }

            // 3. Validate amount (non-empty, numeric, > 0)
            if (amountText.isEmpty()) {
                amountInput.error = "Enter an amount"
                amountInput.requestFocus()
                return@setOnClickListener
            }
            val amountValue = amountText.toDoubleOrNull()
            if (amountValue == null || amountValue <= 0.0) {
                amountInput.error = "Enter a valid amount (> 0)"
                amountInput.requestFocus()
                return@setOnClickListener
            }

            // 4. Validate date (non-empty + correct format dd/MM/yyyy)
            if (dateText.isEmpty()) {
                dateInput.error = "Please pick a date"
                dateInput.requestFocus()
                return@setOnClickListener
            }
            val sdf = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).apply {
                isLenient = false
            }
            try {
                sdf.parse(dateText)
            } catch (e: ParseException) {
                dateInput.error = "Invalid date format"
                dateInput.requestFocus()
                return@setOnClickListener
            }

            // 5. Note is optional – just keep empty string or null
            val noteValue: String? = noteText.takeIf { it.isNotEmpty() }

            // 6. All inputs are valid—do your submission logic here
            //    e.g. send to ViewModel, database, REST API, etc.

        }
            submitExpense(
                type   = typeText,
                amount = amountValue,
                date   = dateText,
                note   = noteValue
            )


    }

    private fun updateDateInView(cal: Calendar) {
        val sdf = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
        dateInput.setText(sdf.format(cal.time))
    }
}
