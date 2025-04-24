package com.example.cashup

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity

class IncomeEntryActivity : AppCompatActivity() {

    private lateinit var incomeField: EditText
    private lateinit var amountView: EditText
    private lateinit var dateView: EditText
    private lateinit var descriptionView: EditText
    private lateinit var attachDocView: EditText
    private lateinit var saveButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

      //  setContentView(R.layout.activity_income_entry)

        // Initialize views
        incomeField = findViewById(R.id.income_field)
        amountView = findViewById(R.id.amount_view)
        dateView = findViewById(R.id.date_view)
        descriptionView = findViewById(R.id.description_view)
        attachDocView = findViewById(R.id.attachDoc_view)
        saveButton = findViewById(R.id.Save_button)

        // Save button click just reads inputs
        saveButton.setOnClickListener {
            val incomeType = incomeField.text.toString()
            val amount = amountView.text.toString()
            val date = dateView.text.toString()
            val description = descriptionView.text.toString()
            val attachment = attachDocView.text.toString()
        }
    }
}
