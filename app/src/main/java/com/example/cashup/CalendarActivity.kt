package com.example.cashup.com.example.cashup

//---------------------------------------- START OF IMPORTS -------------------------------------//

import android.app.DatePickerDialog // ALLOWS USER TO SELECT DATE

import android.content.Intent // ALLOWS DATA TO BE SENT BETWEEN COMPONENTS

import android.os.Bundle // STORES DATA

import android.util.Log

import android.view.LayoutInflater

import android.view.View

import android.view.ViewGroup

import android.widget.Button

import android.widget.CalendarView

import android.widget.ImageButton

import android.widget.TextView

import android.widget.Toast

import androidx.appcompat.app.AlertDialog

import androidx.appcompat.app.AppCompatActivity

import androidx.lifecycle.lifecycleScope

import androidx.recyclerview.widget.LinearLayoutManager

import androidx.recyclerview.widget.RecyclerView

import com.example.cashup.Database.Expense

import com.example.cashup.R

import com.google.android.material.button.MaterialButton

import kotlinx.coroutines.Dispatchers

import kotlinx.coroutines.launch

import kotlinx.coroutines.withContext

import java.text.SimpleDateFormat

import java.util.Calendar

import java.util.Date

import java.util.Locale

import com.example.cashup.Database.ExpenseDatabase

import com.example.cashup.ImageViewerActivity

//---------------------------------------- END OF IMPORTS -------------------------------------//

class CalendarActivity : AppCompatActivity() {

    //------------------- START OF GLOBAL VARIABLES ----------------------//
    //***************** UI VARIABLES ***********//
    private lateinit var calendarView: CalendarView
    private lateinit var startDateButton: Button
    private lateinit var endDateButton: Button
    private lateinit var searchButton: MaterialButton
    private lateinit var expensesRecyclerView: RecyclerView
    private lateinit var noExpensesText: TextView
    private lateinit var backButton: ImageButton
    private lateinit var categorySearchButton: MaterialButton

    //////////////////////////// DATABASE VARIABLES ///////////////////////////
    private lateinit var database: ExpenseDatabase

    //---------------------- DATE & FILTER VARIABLES -------------------------//
    private var startDate: Date = Calendar.getInstance().time
    private var endDate: Date = Calendar.getInstance().time
    private val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
    private var selectedCategory: String? = null // Stores the selected category name

    // --- Hardcoded User ID (Replace with actual login mechanism later) --- //
    private val currentUserId = 1 // Make sure this is the correct way you get the user ID
    //------------------- END OF GLOBAL VARIABLES ----------------------//

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_calendar)

        database = ExpenseDatabase.getDatabase(this)

        initializeViews()
        setupListeners()
        setDefaultDateRange()
        updateDateButtonsText()
        loadExpensesForSelectedPeriod() // Load initial expenses
        // markDatesWithExpenses() // Consider removing/disabling this when filters are active
    }

    private fun initializeViews() {
        calendarView = findViewById(R.id.calendarView)
        startDateButton = findViewById(R.id.startDateButton)
        endDateButton = findViewById(R.id.endDateButton)
        searchButton = findViewById(R.id.searchButton)
        expensesRecyclerView = findViewById(R.id.expensesRecyclerView)
        noExpensesText = findViewById(R.id.noExpensesText)
        backButton = findViewById(R.id.back_button_poe)
        categorySearchButton = findViewById(R.id.categorySearchButton)

        expensesRecyclerView.layoutManager = LinearLayoutManager(this)
    }
    // Button click listener
    private fun setupListeners() {
        backButton.setOnClickListener {
            finish()
        }

        startDateButton.setOnClickListener {
            showDatePickerDialog(true)
        }

        endDateButton.setOnClickListener {
            showDatePickerDialog(false)
        }
    // Category button listener
        categorySearchButton.setOnClickListener {
            showCategorySelectionDialog()
        }

        searchButton.setOnClickListener {
            // Reload expenses based on current date range and category filter
            loadExpensesForSelectedPeriod()
        }

        calendarView.setOnDateChangeListener { _, year, month, dayOfMonth ->
            val calendar = Calendar.getInstance()
            // Set start date to the beginning of the selected day
            calendar.set(year, month, dayOfMonth, 0, 0, 0)
            calendar.set(Calendar.MILLISECOND, 0)
            startDate = calendar.time

            // Set end date to the end of the selected day
            calendar.set(year, month, dayOfMonth, 23, 59, 59)
            calendar.set(Calendar.MILLISECOND, 999)
            endDate = calendar.time

            updateDateButtonsText()
            loadExpensesForSelectedPeriod() // Reload expenses for the single selected day
        }
    }

    private fun setDefaultDateRange() {
        val calendar = Calendar.getInstance()

        // First day of current month (start of day)
        calendar.set(Calendar.DAY_OF_MONTH, 1)
        calendar.set(Calendar.HOUR_OF_DAY, 0)
        calendar.set(Calendar.MINUTE, 0)
        calendar.set(Calendar.SECOND, 0)
        calendar.set(Calendar.MILLISECOND, 0)
        startDate = calendar.time

        // Last day of current month (end of day)
        calendar.set(
            Calendar.DAY_OF_MONTH,
            calendar.getActualMaximum(Calendar.DAY_OF_MONTH)
        )
        calendar.set(Calendar.HOUR_OF_DAY, 23)
        calendar.set(Calendar.MINUTE, 59)
        calendar.set(Calendar.SECOND, 59)
        calendar.set(Calendar.MILLISECOND, 999)
        endDate = calendar.time
    }

    private fun updateDateButtonsText() {
        startDateButton.text = dateFormat.format(startDate)
        endDateButton.text = dateFormat.format(endDate)
    }

    private fun showCategorySelectionDialog() {
        lifecycleScope.launch {
            val categories = withContext(Dispatchers.IO) {
                try {
                    // Use the confirmed CategoryDao method
                    database.categoryDao().getAllCategories()
                } catch (e: Exception) {
                    Log.e("CalendarActivity", "Error fetching categories", e)
                    withContext(Dispatchers.Main) { // Show toast on main thread
                        Toast.makeText(
                            this@CalendarActivity,
                            "Error fetching categories",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                    listOf() // Return empty list on error
                }
            }

            // Ensure UI operations run on the Main thread
            withContext(Dispatchers.Main) {
                if (categories.isEmpty()) {
                    Toast.makeText(
                        this@CalendarActivity,
                        "No categories found.",
                        Toast.LENGTH_SHORT
                    ).show()
                    return@withContext // Exit if no categories
                }

                // Use the 'name' field from the Category entity
                val categoryNames = categories.map { it.name }.toTypedArray()
                val currentSelectionIndex =
                    if (selectedCategory != null) categoryNames.indexOf(
                        selectedCategory
                    ) else -1

                // Use androidx.appcompat.app.AlertDialog
                val dialog = AlertDialog.Builder(this@CalendarActivity)
                    .setTitle("Select Category")
                    .setSingleChoiceItems(
                        categoryNames,
                        currentSelectionIndex
                    ) { dialog, which ->
                        selectedCategory = categoryNames[which]
                        dialog.dismiss()
                        // Use the 'name' field from Category entity for the button text
                        categorySearchButton.text = "Category: $selectedCategory"
                        loadExpensesForSelectedPeriod() // Reload with category filter
                    }
                    .setNeutralButton("Clear Filter") { _, _ ->
                        selectedCategory = null
                        // Consider using a string resource for "Search Categories"
                        categorySearchButton.text = "Search Categories"
                        loadExpensesForSelectedPeriod() // Reload without category filter
                    }
                    .setNegativeButton("Cancel", null)
                    .create()

                dialog.show()
            }
        }
    }

    private fun showDatePickerDialog(isStartDate: Boolean) {
        val calendar = Calendar.getInstance()
        calendar.time = if (isStartDate) startDate else endDate

        DatePickerDialog(
            this,
            { _, year, month, dayOfMonth ->
                calendar.set(year, month, dayOfMonth)
                if (isStartDate) {
                    // Set time to beginning of the day
                    calendar.set(Calendar.HOUR_OF_DAY, 0)
                    calendar.set(Calendar.MINUTE, 0)
                    calendar.set(Calendar.SECOND, 0)
                    calendar.set(Calendar.MILLISECOND, 0)
                    startDate = calendar.time

                    // Ensure start date is not after end date
                    if (startDate.after(endDate)) {
                        endDate = startDate // Adjust end date if needed (or show error)
                    }
                } else {
                    // Set time to end of the day
                    calendar.set(Calendar.HOUR_OF_DAY, 23)
                    calendar.set(Calendar.MINUTE, 59)
                    calendar.set(Calendar.SECOND, 59)
                    calendar.set(Calendar.MILLISECOND, 999)
                    endDate = calendar.time

                    // Ensure end date is not before start date
                    if (endDate.before(startDate)) {
                        startDate = endDate // Adjust start date if needed (or show error)
                    }
                }
                updateDateButtonsText()
                // Don't reload here; Search button or calendar click triggers reload
            },
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        ).show()
    }

    // --- UPDATED FUNCTION ---
    private fun loadExpensesForSelectedPeriod() {
        lifecycleScope.launch {
            try {
                // Fetch expenses based on whether a category is selected
                val expenses = withContext(Dispatchers.IO) {
                    val category = selectedCategory // Local copy for background thread
                    if (category != null) {
                        // Use the corrected DAO method with category filter
                        database.expenseDao().getExpensesByCategoryAndDateRange(
                            currentUserId,
                            category,
                            startDate,
                            endDate
                        )
                    } else {
                        // Use the corrected DAO method without category filter
                        database.expenseDao().getExpensesByDateRange(
                            currentUserId,
                            startDate,
                            endDate
                        )
                    }
                }

                // Update UI with fetched expenses on the Main thread
                withContext(Dispatchers.Main) {
                    if (expenses.isNotEmpty()) {
                        noExpensesText.visibility = View.GONE
                        expensesRecyclerView.visibility = View.VISIBLE
                        // Pass the fetched expenses to the adapter
                        expensesRecyclerView.adapter =
                            ExpenseAdapter(expenses) { expense ->
                                // Handle click - show receipt if URI exists
                                expense.receiptUri?.let { uri ->
                                    showReceipt(uri)
                                } ?: Toast.makeText(
                                    this@CalendarActivity,
                                    "No receipt available",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                    } else {
                        noExpensesText.visibility = View.VISIBLE
                        expensesRecyclerView.visibility = View.GONE
                        // Set an empty adapter when no expenses are found
                        expensesRecyclerView.adapter =
                            ExpenseAdapter(emptyList()) {}
                    }
                }

            } catch (e: Exception) {
                Log.e("CalendarActivity", "Error loading expenses", e)
                withContext(Dispatchers.Main) {
                    Toast.makeText(
                        this@CalendarActivity,
                        "Error loading expenses: ${e.message}",
                        Toast.LENGTH_LONG // Longer duration for errors
                    ).show()
                    // Ensure UI reflects error state
                    noExpensesText.text = "Error loading expenses" // Inform user
                    noExpensesText.visibility = View.VISIBLE
                    expensesRecyclerView.visibility = View.GONE
                    expensesRecyclerView.adapter =
                        ExpenseAdapter(emptyList()) {} // Clear adapter
                }
            }
        }
    }

    // --- Consider removing or adjusting markDatesWithExpenses ---
    // This function marks dates based on *all* expenses in the month by default.
    // It does not respect the category filter. You might want to remove it,
    // disable it when a filter is active, or modify it significantly.
    private fun markDatesWithExpenses() {
        // If you keep this, it needs modification to respect the selectedCategory filter
        lifecycleScope.launch {
            try {
                val calendar = Calendar.getInstance()
                calendar.time = calendarView.date.let { Date(it) } // Get current calendar month

                calendar.set(Calendar.DAY_OF_MONTH, 1)
                calendar.set(Calendar.HOUR_OF_DAY, 0); calendar.set(Calendar.MINUTE, 0); calendar.set(Calendar.SECOND, 0); calendar.set(Calendar.MILLISECOND, 0)
                val monthStart = calendar.time

                calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMaximum(Calendar.DAY_OF_MONTH))
                calendar.set(Calendar.HOUR_OF_DAY, 23); calendar.set(Calendar.MINUTE, 59); calendar.set(Calendar.SECOND, 59); calendar.set(Calendar.MILLISECOND, 999)
                val monthEnd = calendar.time

                // Fetch dates *respecting the filter* if you want accurate marking
                val expenseDates = withContext(Dispatchers.IO) {
                    val category = selectedCategory
                    val dates = if (category != null) {
                        database.expenseDao().getExpensesByCategoryAndDateRange(currentUserId, category, monthStart, monthEnd)
                    } else {
                        database.expenseDao().getExpensesByDateRange(currentUserId, monthStart, monthEnd)
                    }
                    dates.map { it.startDate } // Get only the dates
                }

                // --- How to mark dates on Android's CalendarView is non-trivial ---
                // The standard CalendarView doesn't have a simple API to highlight specific dates.
                // You often need a custom CalendarView library for this feature.
                // The existing code only sets the *initial* displayed date.
                // For now, just logging the dates found.
                if (expenseDates.isNotEmpty()) {
                    Log.d("CalendarActivity", "Dates with expenses in this view/filter: $expenseDates")
                    // The following line just sets the *currently selected* date, it doesn't mark multiple dates.
                    // val earliestExpenseDate = expenseDates.minOrNull()
                    // earliestExpenseDate?.let {
                    //     calendarView.date = it.time
                    // }
                } else {
                    Log.d("CalendarActivity", "No expenses found for marking in this view/filter.")
                }

            } catch (e: Exception) {
                Log.e("CalendarActivity", "Error getting dates for marking", e)
            }
        }
    }


    private fun showReceipt(receiptUriString: String) {
        try {
            val intent = Intent(this, ImageViewerActivity::class.java).apply {
                putExtra("IMAGE_URI", receiptUriString)
            }
            startActivity(intent)
        } catch (e: Exception) {
            Toast.makeText(
                this,
                "Cannot open receipt: ${e.message}",
                Toast.LENGTH_SHORT
            ).show()
            Log.e("CalendarActivity", "Error showing receipt", e)
        }
    }

    // --- ExpenseAdapter (Inner Class) - No changes needed here ---
    inner class ExpenseAdapter(
        private val expenses: List<Expense>,
        private val onExpenseClick: (Expense) -> Unit
    ) : RecyclerView.Adapter<ExpenseAdapter.ExpenseViewHolder>() {

        inner class ExpenseViewHolder(itemView: View) :
            RecyclerView.ViewHolder(itemView) {
            // Ensure these IDs match your item_expense.xml layout
            val typeText: TextView = itemView.findViewById(R.id.expenseTypeText)
            val amountText: TextView =
                itemView.findViewById(R.id.expenseAmountText)
            val dateText: TextView = itemView.findViewById(R.id.expenseDateText)
            val hasReceiptIndicator: View =
                itemView.findViewById(R.id.receiptIndicator)
        }

        override fun onCreateViewHolder(
            parent: ViewGroup,
            viewType: Int
        ): ExpenseViewHolder {
            val itemView = LayoutInflater.from(parent.context).inflate(
                R.layout.item_expense, // Ensure this layout exists
                parent,
                false
            )
            return ExpenseViewHolder(itemView)
        }

        override fun onBindViewHolder(holder: ExpenseViewHolder, position: Int) {
            val expense = expenses[position]

            // Use the 'type' field from Expense entity for category name
            holder.typeText.text = expense.type
            // Format currency appropriately (consider locale)
            holder.amountText.text = "R%.2f".format(expense.amount)
            holder.dateText.text = dateFormat.format(expense.startDate)

            holder.hasReceiptIndicator.visibility =
                if (expense.receiptUri != null) View.VISIBLE else View.GONE

            holder.itemView.setOnClickListener {
                onExpenseClick(expense)
            }
        }

        override fun getItemCount() = expenses.size
    }
}
//***************************************************** END OF CODE ***********************************************************//
