package com.example.cashup // Correct package name

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.cashup.Data.IncomeItem // Import your data class
import com.example.cashup.Data.IncomeRepository // Import your repository

class AddIncomeActivity : AppCompatActivity() {

    // Declare view variables
    private lateinit var incomeTypeEditText: EditText
    private lateinit var amountEditText: EditText
    private lateinit var dateEditText: EditText
    private lateinit var descriptionEditText: EditText
    private lateinit var saveButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        setContentView(R.layout.add_income_section)

        // Initialize views using findViewById and the IDs from your XML
        incomeTypeEditText = findViewById(R.id.income_field)
        amountEditText = findViewById(R.id.amount_view)
        dateEditText = findViewById(R.id.date_view)
        descriptionEditText = findViewById(R.id.description_view)
        saveButton = findViewById(R.id.Save_button) // Corrected ID

        // Set click listener for the save button
        saveButton.setOnClickListener {
            saveIncomeData()
        }

    }

    private fun saveIncomeData() {
        // Get text from EditText fields, trim whitespace
        val incomeType = incomeTypeEditText.text.toString().trim()
        val amount = amountEditText.text.toString().trim()
        val date = dateEditText.text.toString().trim()
        val description = descriptionEditText.text.toString().trim()

        // Basic validation: Check if required fields are empty
        if (incomeType.isEmpty() || amount.isEmpty() || date.isEmpty()) {
            showToast("Please fill in Income Type, Amount, and Date.")
            return // Stop the function if validation fails
        }

        // Create an IncomeItem object
        val newIncome = IncomeItem(
            incomeType = incomeType,
            amount = amount, // Storing as String as per IncomeItem definition
            date = date,
            description = description // Description can be empty if needed
        )

        // Add the income item to the repository
        IncomeRepository.addIncome(newIncome)

        // Show success message
        showToast("Income saved successfully!")

        // Finish the activity and return to the previous screen (Homepage)
        finish()
    }

    // Helper function for showing Toast messages
    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}
