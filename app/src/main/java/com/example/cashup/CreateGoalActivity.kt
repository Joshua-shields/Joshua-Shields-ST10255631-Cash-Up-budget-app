package com.example.cashup

//***************** Start of imports *****************************//
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
import java.util.*
//******************* End of imports ****************************//

class CreateGoalActivity : AppCompatActivity() {

    //********* local variables *************//
    private lateinit var backButton: ImageButton
    private lateinit var nameInput: EditText
    private lateinit var categoryInput: EditText
    private lateinit var minSpendInput: EditText
    private lateinit var maxSpendInput: EditText
    private lateinit var durationSpinner: Spinner
    private lateinit var notesInput: EditText
    private lateinit var createButton: Button
    private lateinit var database: AppDatabase
    //********* end of local variables ********//

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_goal)

        // Initialize Room database
        database = AppDatabase.getDatabase(this)

        // Initialize UI elements
        backButton       = findViewById(R.id.back_button)           // back button
        nameInput        = findViewById(R.id.goal_name_input)       // goal name
        categoryInput    = findViewById(R.id.goal_category_input)   // category
        minSpendInput    = findViewById(R.id.min_spend_input)       // minimum spend
        maxSpendInput    = findViewById(R.id.max_spend_input)       // maximum spend (optional)
        durationSpinner  = findViewById(R.id.goal_duration_spinner) // duration selector
        notesInput       = findViewById(R.id.notes_input)           // additional notes
        createButton     = findViewById(R.id.create_goal_button)    // create goal button

        // Set up duration spinner adapter for white while its closed text and black for when dropdown items
        val spinnerAdapter = ArrayAdapter.createFromResource(
            this,
            R.array.duration_options,
            R.layout.spinner_item_white          // closed state shows white text
        )
        spinnerAdapter.setDropDownViewResource(
            R.layout.spinner_dropdown_item       // dropdown items show black text
        )
        durationSpinner.adapter = spinnerAdapter

        // Set click listeners
        backButton.setOnClickListener {
            finish() // close and return
        }
        createButton.setOnClickListener {
            saveGoal() // attempt to save goal
        }
    }

    // save new goal to database
    private fun saveGoal() {
        // Read and trim inputs
        val title    = nameInput.text.toString().trim()
        val category = categoryInput.text.toString().trim()
        val minSpend = minSpendInput.text.toString().toDoubleOrNull()
        val maxSpend = maxSpendInput.text.toString().toDoubleOrNull() // optional
        val notes    = notesInput.text.toString().trim()
        val duration = durationSpinner.selectedItem?.toString()?.uppercase() ?: ""

        // Input validation
        if (title.isEmpty() || minSpend == null || duration.isEmpty()) {
            Toast.makeText(
                this,
                "Please fill in Name, Min Spend & Duration",
                Toast.LENGTH_SHORT
            ).show()
            return
        }

        // Compute start and end timestamps
        val now = System.currentTimeMillis()
        val endTs = Calendar.getInstance().apply {
            timeInMillis = now
            when (duration) {
                "WEEK"  -> add(Calendar.WEEK_OF_YEAR, 1)
                "MONTH" -> add(Calendar.MONTH, 1)
                "YEAR"  -> add(Calendar.YEAR, 1)
                else    -> add(Calendar.MONTH, 1)
            }
        }.timeInMillis

        // Build Goal entity
        val goal = Goal(
            userId        = 1,                      // replace with real user ID
            title         = title,
            description   = if (notes.isNotEmpty()) notes else category,
            targetAmount  = minSpend,
            currentAmount = 0.0,
            goalType      = duration,
            startDate     = now,
            endDate       = endTs,
            isCompleted   = false
        )

        // Insert in Room on a background thread
        CoroutineScope(Dispatchers.Main).launch {
            withContext(Dispatchers.IO) {
                database.goalDao().insertGoal(goal)   // perform insert
            }
            // Notify user and close
            Toast.makeText(
                this@CreateGoalActivity,
                "Goal saved!",
                Toast.LENGTH_SHORT
            ).show()
            finish()
        }
    }
}
