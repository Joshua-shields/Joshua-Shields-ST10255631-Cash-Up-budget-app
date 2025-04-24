package com.example.cashup

import android.app.DatePickerDialog
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.cashup.databinding.ActivityIncomeEntryBinding
import java.util.Calendar

class IncomeEntryActivity : AppCompatActivity() {

    private lateinit var binding: ActivityIncomeEntryBinding
    private lateinit var viewModel: IncomeViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Inflate the layout using ViewBinding
        binding = ActivityIncomeEntryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Initialize ViewModel
        viewModel = ViewModelProvider(this)[IncomeViewModel::class.java]

        // Set up date picker for date field
        binding.dateView.setOnClickListener {
            showDatePicker()
        }

        // Set up Save button click listener
        binding.SaveButton.setOnClickListener {
            saveIncome()
        }

        // Optional: Set up click listener for document attachment (placeholder)
        binding.attachDocView.setOnClickListener {
            // Implement file picker logic here
            Toast.makeText(this, "Document picker not implemented", Toast.LENGTH_SHORT).show()
        }

        // Optional: Set up click listener for profile image (placeholder)
        binding.imageView4.setOnClickListener {
            // Implement image picker logic here
            Toast.makeText(this, "Image picker not implemented", Toast.LENGTH_SHORT).show()
        }
    }

    private fun showDatePicker() {
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        DatePickerDialog(this, { _, selectedYear, selectedMonth, selectedDay ->
            // Format date as YYYY-MM-DD
            val formattedDate = String.format("%d-%02d-%02d", selectedYear, selectedMonth + 1, selectedDay)
            binding.dateView.setText(formattedDate)
        }, year, month, day).show()
    }

    private fun saveIncome() {
        // Collect form inputs
        val salary = binding.incomeField.text.toString().trim()
        val amount = binding.amountView.text.toString().trim()
        val date = binding.dateView.text.toString().trim()
        val description = binding.descriptionView.text.toString().trim()
        val documentPath = binding.attachDocView.text.toString().takeIf {
            it != "Attach Supporting Document"
        } // Store null if default text
        val profileImagePath: String? = null // Replace with actual image path if implemented

        // Basic validation
        if (salary.isBlank() || amount.isBlank() || date.isBlank()) {
            Toast.makeText(this, "Please fill in salary, amount, and date", Toast.LENGTH_SHORT).show()
            return
        }

        // Validate amount is a number
        if (!amount.matches("\\d+(\\.\\d{1,2})?".toRegex())) {
            Toast.makeText(this, "Please enter a valid amount (e.g., 5000 or 5000.00)", Toast.LENGTH_SHORT).show()
            return
        }

        // Save to database via ViewModel
        viewModel.saveIncome(
            salary = salary,
            amount = amount,
            date = date,
            description = description,
            documentPath = documentPath,
            profileImagePath = profileImagePath
        )

        // Show success message and clear form
        Toast.makeText(this, "Income saved successfully", Toast.LENGTH_SHORT).show()
        clearForm()
    }

    private fun clearForm() {
        binding.incomeField.text.clear()
        binding.amountView.text.clear()
        binding.dateView.text.clear()
        binding.descriptionView.text.clear()
        binding.attachDocView.setText("Attach Supporting Document")
        // Reset profile image if applicable
    }
}