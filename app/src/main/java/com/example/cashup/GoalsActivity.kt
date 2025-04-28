// Start of file: GoalsActivity.kt
package com.example.cashup

import android.content.Intent
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.ImageButton
import android.widget.Spinner
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.cashup.Database.AppDatabase
import com.example.cashup.Database.GoalDao
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

/**
 * Displays the user’s goals in three dropdowns (weekly, monthly, yearly)
 * and provides a button to add a new goal.
 */
class GoalsActivity : AppCompatActivity() {

    // UI elements
    private lateinit var backButton: ImageButton
    private lateinit var addGoalButton: ImageButton
    private lateinit var weeklySpinner: Spinner
    private lateinit var monthlySpinner: Spinner
    private lateinit var yearlySpinner: Spinner

    // Data access
    private lateinit var goalDao: GoalDao

    // TODO: Replace with real authenticated user ID
    private val currentUserId: Int = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.goals)

        // Bind views
        backButton = findViewById(R.id.backButton)
        addGoalButton = findViewById(R.id.add_goal_button)
        weeklySpinner = findViewById(R.id.spinner_weekly)
        monthlySpinner = findViewById(R.id.spinner_monthly)
        yearlySpinner = findViewById(R.id.spinner_yearly)

        // Initialize DAO
        goalDao = AppDatabase.getDatabase(this).goalDao()

        // Back navigation
        backButton.setOnClickListener { finish() }

        // Open CreateGoalActivity when + button clicked
        addGoalButton.setOnClickListener {
            startActivity(Intent(this, CreateGoalActivity::class.java))
        }
    }

    override fun onResume() {
        super.onResume()
        // Refresh dropdowns whenever the screen comes back into view
        loadWeeklyGoals()
        loadMonthlyGoals()
        loadYearlyGoals()
    }

    /**
     * Loads weekly goals from the database and populates the spinner.
     */
    private fun loadWeeklyGoals() {
        lifecycleScope.launch(Dispatchers.IO) {
            val goals = goalDao.getWeeklyGoals(currentUserId)
            val items = goals.map {
                "${it.title} — Min: R${"%,.2f".format(it.currentAmount)}  Max: R${"%,.2f".format(it.targetAmount)}"
            }
            withContext(Dispatchers.Main) {
                weeklySpinner.adapter = ArrayAdapter(
                    this@GoalsActivity,
                    R.layout.spinner_item_white,               // closed: white text
                    items
                ).apply {
                    setDropDownViewResource(R.layout.spinner_dropdown_item_black) // open: black text
                }
                if (items.isEmpty()) showToast("No weekly goals found")
            }
        }
    }

    /**
     * Loads monthly goals from the database and populates the spinner.
     */
    private fun loadMonthlyGoals() {
        lifecycleScope.launch(Dispatchers.IO) {
            val goals = goalDao.getMonthlyGoals(currentUserId)
            val items = goals.map {
                "${it.title} — Min: R${"%,.2f".format(it.currentAmount)}  Max: R${"%,.2f".format(it.targetAmount)}"
            }
            withContext(Dispatchers.Main) {
                monthlySpinner.adapter = ArrayAdapter(
                    this@GoalsActivity,
                    R.layout.spinner_item_white,
                    items
                ).apply {
                    setDropDownViewResource(R.layout.spinner_dropdown_item_black)
                }
                if (items.isEmpty()) showToast("No monthly goals found")
            }
        }
    }

    /**
     * Loads yearly goals from the database and populates the spinner.
     */
    private fun loadYearlyGoals() {
        lifecycleScope.launch(Dispatchers.IO) {
            val goals = goalDao.getYearlyGoals(currentUserId)
            val items = goals.map {
                "${it.title} — Min: R${"%,.2f".format(it.currentAmount)}  Max: R${"%,.2f".format(it.targetAmount)}"
            }
            withContext(Dispatchers.Main) {
                yearlySpinner.adapter = ArrayAdapter(
                    this@GoalsActivity,
                    R.layout.spinner_item_white,
                    items
                ).apply {
                    setDropDownViewResource(R.layout.spinner_dropdown_item_black)
                }
                if (items.isEmpty()) showToast("No yearly goals found")
            }
        }
    }

    /**
     * Helper to show toast messages.
     */
    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}
//***************************************************** END OF CODE ***********************************************************//
