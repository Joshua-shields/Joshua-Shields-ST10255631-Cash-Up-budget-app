package com.example.cashup.com.example.cashup

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.widget.CalendarView
import android.widget.Toast
import com.example.cashup.R

class CalendarActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_calendar)

        // Initialize CalendarView
        val calendarView: CalendarView = findViewById(R.id.calendarView)

        // Optional: Set a listener for date selection
        calendarView.setOnDateChangeListener { _, year, month, dayOfMonth ->
            // Month is 0-based, so add 1 for display
            val selectedDate = "$dayOfMonth/${month + 1}/$year"
            Toast.makeText(this, "Selected: $selectedDate", Toast.LENGTH_SHORT).show()
        }
    }
}