package com.example.cashup

//***************** Start of imports *****************************//
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.example.cashup.Database.AppDatabase
import com.example.cashup.Database.Goal
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import android.widget.ImageButton
//******************* End of imports ****************************//

class CreateGoalActivity : AppCompatActivity() {

    // UI
    private lateinit var backButton: ImageButton
    private lateinit var nameInput: EditText
    private lateinit var categoryInput: EditText
    private lateinit var minSpendInput: EditText
    private lateinit var maxSpendInput: EditText
    private lateinit var durationSpinner: Spinner
    private lateinit var notesInput: EditText
    private lateinit var createButton: Button

    // Room database
    private lateinit var database: AppDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_goal)

        // 1) init Room
        database = AppDatabase.getDatabase(this)

        // 2) bind UI
        backButton       = findViewById(R.id.back_button)
        nameInput        = findViewById(R.id.goal_name_input)
        categoryInput    = findViewById(R.id.goal_category_input)
        minSpendInput    = findViewById(R.id.min_spend_input)
        maxSpendInput    = findViewById(R.id.max_spend_input)
        durationSpinner  = findViewById(R.id.goal_duration_spinner)
        notesInput       = findViewById(R.id.notes_input)
        createButton     = findViewById(R.id.create_goal_button)

        // 3) wiring
        backButton.setOnClickListener { finish() }
        createButton.setOnClickListener { saveGoal() }
    }

    private fun saveGoal() {
        // 4) read + validate
        val title    = nameInput.text.toString().trim()
        val category = categoryInput.text.toString().trim()
        val minSpend = minSpendInput.text.toString().toDoubleOrNull()
        val maxSpend = maxSpendInput.text.toString().toDoubleOrNull()  // optional
        val notes    = notesInput.text.toString().trim()
        val duration = durationSpinner.selectedItem?.toString()?.uppercase() ?: ""

        if (title.isEmpty() || minSpend == null || duration.isEmpty()) {
            Toast.makeText(this, "Please fill in Name, Min Spend & Duration", Toast.LENGTH_SHORT).show()
            return
        }

        // 5) compute start/end
        val now = System.currentTimeMillis()
        val endTs = java.util.Calendar.getInstance().apply {
            timeInMillis = now
            when (duration) {
                "WEEK"  -> add(java.util.Calendar.WEEK_OF_YEAR, 1)
                "MONTH" -> add(java.util.Calendar.MONTH, 1)
                "YEAR"  -> add(java.util.Calendar.YEAR, 1)
                else    -> add(java.util.Calendar.MONTH, 1)
            }
        }.timeInMillis

        // 6) build Goal entity
        val goal = Goal(
            userId       = 1,                     // replace with real user ID logic
            title        = title,
            description  = if (notes.isNotEmpty()) notes else category,
            targetAmount = minSpend,
            currentAmount = 0.0,
            goalType     = duration,
            startDate    = now,
            endDate      = endTs,
            isCompleted  = false
        )

        // 7) insert via coroutine
        CoroutineScope(Dispatchers.Main).launch {
            withContext(Dispatchers.IO) {
                database.goalDao().insertGoal(goal)
            }
            Toast.makeText(this@CreateGoalActivity, "Goal saved!", Toast.LENGTH_SHORT).show()
            finish()
        }
    }
}
