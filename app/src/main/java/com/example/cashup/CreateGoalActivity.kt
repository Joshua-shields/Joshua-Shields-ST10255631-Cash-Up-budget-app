// Start of file: CreateGoalActivity.kt
package com.example.cashup

import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.Spinner
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.cashup.Database.AppDatabase
import com.example.cashup.Database.Goal
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.Calendar

/**
 * Captures title, category/description, min/max spend, duration, and notes.
 * Inserts the new Goal into the Room database.
 */
class CreateGoalActivity : AppCompatActivity() {

    // UI elements
    private lateinit var backButton: ImageButton
    private lateinit var nameInput: EditText
    private lateinit var categoryInput: EditText
    private lateinit var minSpendInput: EditText
    private lateinit var maxSpendInput: EditText
    private lateinit var durationSpinner: Spinner
    private lateinit var notesInput: EditText
    private lateinit var createButton: Button

    // Database instance
    private lateinit var database: AppDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_goal)

        // Initialize Room database
        database = AppDatabase.getDatabase(this)

        // Bind views
        backButton = findViewById(R.id.back_button)
        nameInput = findViewById(R.id.goal_name_input)
        categoryInput = findViewById(R.id.goal_category_input)
        minSpendInput = findViewById(R.id.min_spend_input)
        maxSpendInput = findViewById(R.id.max_spend_input)
        durationSpinner = findViewById(R.id.goal_duration_spinner)
        notesInput = findViewById(R.id.notes_input)
        createButton = findViewById(R.id.create_goal_button)

        // Configure the duration spinner:
        val spinnerAdapter = ArrayAdapter.createFromResource(
            this,
            R.array.duration_options,
            R.layout.spinner_item_white          // closed: white text
        ).apply {
            setDropDownViewResource(R.layout.spinner_dropdown_item) // open: black text
        }
        durationSpinner.adapter = spinnerAdapter

        // Close screen when back button clicked
        backButton.setOnClickListener { finish() }

        // Save goal when create button clicked
        createButton.setOnClickListener { saveGoal() }
    }

    /**
     * Reads inputs, validates them, maps the duration to the DB goalType,
     */
    private fun saveGoal() {
        // Read and trim inputs
        val title = nameInput.text.toString().trim()
        val category = categoryInput.text.toString().trim()
        val minSpend = minSpendInput.text.toString().toDoubleOrNull()
        val maxSpend = maxSpendInput.text.toString().toDoubleOrNull()
        val notes = notesInput.text.toString().trim()

        // Validate required fields
        if (title.isEmpty() || minSpend == null) {
            Toast.makeText(this, "Please fill in Name and Minimum Spend", Toast.LENGTH_SHORT).show()
            return
        }

        // Map spinner raw value ("WEEK","MONTH","YEAR") to DAO-expected ("WEEKLY", etc.)
        val raw = durationSpinner.selectedItem?.toString()?.uppercase() ?: ""
        val goalType = when (raw) {
            "WEEK" -> "WEEKLY"
            "MONTH" -> "MONTHLY"
            "YEAR" -> "YEARLY"
            else -> "MONTHLY"
        }

        // Calculate start and end timestamps
        val now = System.currentTimeMillis()
        val endTs = Calendar.getInstance().apply {
            timeInMillis = now
            when (goalType) {
                "WEEKLY" -> add(Calendar.WEEK_OF_YEAR, 1)
                "MONTHLY" -> add(Calendar.MONTH, 1)
                "YEARLY" -> add(Calendar.YEAR, 1)
            }
        }.timeInMillis

        // Build Goal entity
        val goal = Goal(
            userId = 1,
            title = title,
            description = if (notes.isNotEmpty()) notes else category,
            targetAmount = maxSpend ?: minSpend,          // use max if provided, else min
            currentAmount = minSpend,                      // store the user’s minimum here
            goalType = goalType,
            startDate = now,
            endDate = endTs,
            isCompleted = false
        )

        // Insert into DB off the main thread, then close
        CoroutineScope(Dispatchers.Main).launch {
            withContext(Dispatchers.IO) {
                database.goalDao().insertGoal(goal)
            }
            Toast.makeText(this@CreateGoalActivity, "Goal saved!", Toast.LENGTH_SHORT).show()
            finish()
        }
    }
}
//***************************************************** END OF CODE ***********************************************************//
