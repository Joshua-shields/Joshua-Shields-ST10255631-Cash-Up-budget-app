package com.example.cashup

import android.app.DatePickerDialog
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import java.util.Calendar

class IncomeEntryActivity : AppCompatActivity() {

    private lateinit var incomeField: EditText
    private lateinit var amountView: EditText
    private lateinit var dateView: EditText
    private lateinit var descriptionView: EditText
    private lateinit var attachDocView: EditText
    private lateinit var imageView4: ImageView
    private lateinit var saveButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


       setContentView(R.layout.activity_income_entry)

        // Initialize views using findViewById
        incomeField = findViewById(R.id.income_field)
        amountView = findViewById(R.id.amount_view)
        dateView = findViewById(R.id.date_view)
        descriptionView = findViewById(R.id.description_view)
        attachDocView = findViewById(R.id.attachDoc_view)
        imageView4 = findViewById(R.id.imageView4)
        saveButton = findViewById(R.id.Save_button)

        // Set up date picker for date field
        dateView.setOnClickListener {
            showDatePicker()
        }

        // Set up Save button click listener to collect input
        saveButton.setOnClickListener {
            // Collect form inputs but do nothing with them
            val salary = incomeField.text.toString().trim()
            val amount = amountView.text.toString().trim()
            val date = dateView.text.toString().trim()
            val description = descriptionView.text.toString().trim()
            val documentPath = attachDocView.text.toString().takeIf {
                it != "Attach Supporting Document"
            }
            val profileImagePath: String? = null // Placeholder, as no image picker is implemented
        }

        // Set up click listeners for document and image (placeholders, no action)
        attachDocView.setOnClickListener {
            // No action, just allow input
        }

        imageView4.setOnClickListener {
            // No action, just allow interaction
        }
    }

    /*private fun showDatePicker() {
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        DatePickerDialog(this, { _, selectedYear, selectedMonth, selectedDay ->
            // Format date as YYYY-MM-DD and set it in the date field
            val formattedDate = String.format("%d-%02d-%02d", selectedYear, selectedMonth + 1, selectedDay)
            dateView.setText(formattedDate)
        }, year, month, day).show()
    }*/
}