package com.example.cashup.com.example.cashup

//---------------------------------------- START OF IMPORTS -------------------------------------//

import android.app.DatePickerDialog // ALLOWS USER TO SELECT DATE

import android.content.Intent // ALLOWS DATA TO BE SENT BETWEEN COMPONENTS

import android.os.Bundle // STORES DATA

import android.util.Log // SENDING MESSAGES TO THE LOGCAT

import android.view.LayoutInflater // LINK UI TO VIEW OBJECT

import android.view.View // UI COMPONENT

import android.view.ViewGroup

import android.widget.Button // CLICKABLE BUTTON ACCESS

import android.widget.CalendarView // CALENDAR INTERFACE

import android.widget.ImageButton // ALLLOWS FOR IMAGES TO BE USED AS A BUTTON AS PER TEXT

import android.widget.TextView // OUTPUT TEXT TO USER

import android.widget.Toast // DISPLAY MESSAGES AND NOTIFICATIONS

import androidx.appcompat.app.AlertDialog // USED TO SEND ERROR MESSAGES TO USER

import androidx.appcompat.app.AppCompatActivity

import androidx.lifecycle.lifecycleScope // launches coroutine

import androidx.recyclerview.widget.LinearLayoutManager // SETS LAYOUT BETWEEN VERICAL AND HORIZONTAL

import androidx.recyclerview.widget.RecyclerView

import com.example.cashup.Database.Expense // EXPENSE DATABASE

import com.example.cashup.R // GRANTS ACCESS TO THE APP DATABASE RESOURCES

import com.google.android.material.button.MaterialButton

import kotlinx.coroutines.Dispatchers

import kotlinx.coroutines.launch

import kotlinx.coroutines.withContext

import java.text.SimpleDateFormat

import java.util.Calendar // MANAGES THE DATE AND TIME

import java.util.Date // SPECIFIC  DATE

import java.util.Locale // LOCATION

import com.example.cashup.Database.ExpenseDatabase // EXPENSE DATABASE

import com.example.cashup.ImageViewerActivity // ALLOW FOR VIEWING OF IMAGES

//---------------------------------------- END OF IMPORTS -------------------------------------//



//***************************************************** START OF CODE ***********************************************************//
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

    private lateinit var categorySearchButton: MaterialButton // BUTTON VARIABLE TO SEARCH BY CATEGORY

    //////////////////////////// DATABASE VARIABLES ///////////////////////////

    private lateinit var database: ExpenseDatabase

    //---------------------- DATE & FILTER VARIABLES -------------------------//

    private var startDate: Date = Calendar.getInstance().time
    private var endDate: Date = Calendar.getInstance().time
    private val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())

    private var selectedCategory: String? = null // Stores the selected category name

    // --- Hardcoded User ID , WILL IMPLEMENT INCREMENTAL INDEXING IN PART 3  --- //

    private val currentUserId = 1

    //------------------- END OF GLOBAL VARIABLES ----------------------//

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_calendar)

        database = ExpenseDatabase.getDatabase(this)

        initializeViews() // SET UP OF VIEW
        setupListeners() // ' ' LISTENERS
        setDefaultDateRange()

        updateDateButtonsText()
        loadExpensesForSelectedPeriod() // Load initial expenses

    }


    /*
    * THE BELOW CODE IS SETTING UP THE VIEWS
    * I AM SETTING UP BY USING THE ID AS IT IS UNIQUE TO EACH FIELD
    * WILL REQUIRE INDEXING IN PART 3
    *
    * */
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
    // DATE
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

        // SEARCH BUTTON
        searchButton.setOnClickListener {
            // Reload expenses based on current date range and category filter
            loadExpensesForSelectedPeriod()
        }

        calendarView.setOnDateChangeListener { _, year, month, dayOfMonth ->
            val calendar = Calendar.getInstance()
            // Set start date to the beginning
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



    /*
    *
    *
    * DECLERATION OF AI USAGE
    * NATURE : DEBUGGING
    * AI USED : CHATGPT
    * LINK TO CHAT :  https://chatgpt.com/c/6810f7fd-1634-8010-ae5e-46556bc0125d
    *
    *
    * */
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

            // Ensure UI operations run
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


                    if (startDate.after(endDate)) { // THIS IS TO SET THE START DATE TO BE FOLLOWED BY THE END DATE. IF THE END DATE IS BEFORE THE START DATE, IT WILL BE SWITCHED.
                        endDate = startDate
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

  /*
  * BELOW CODE IS TO LOAD UP EXXPENSES FROM THE DATABASE AND DISPLAY THEM. THIS WILL ALLOW US TO SEARCH BY CATEGORY OR EXPENSE.
  * */
    private fun loadExpensesForSelectedPeriod() {
        lifecycleScope.launch {
            try {
                // Fetch expenses based on which category is selected
                val expenses = withContext(Dispatchers.IO) {
                    val category = selectedCategory // Local copy for background
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





    private fun markDatesWithExpenses() {

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

                if (expenseDates.isNotEmpty()) {
                    Log.d("CalendarActivity", "Dates with expenses in this view/filter: $expenseDates")

                } else {
                    Log.d("CalendarActivity", "No expenses found for marking in this view/filter.")
                }

            } catch (e: Exception) {
                Log.e("CalendarActivity", "Error getting dates for marking", e)
            }
        }
    }

    /*
    *
    * THE BELOW CODE IS USED TO DISPLAY RECPTS WHICH WERE UPLOADED BY THE USER IN THE CREATE EXPENSE SCREEN.
    * INFORMATION IS RETRIVED FROM THE DATABASE AND THEN DISPLAYED BASED ON URI
    *
    * */

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


    inner class ExpenseAdapter(
        private val expenses: List<Expense>,
        private val onExpenseClick: (Expense) -> Unit
    ) : RecyclerView.Adapter<ExpenseAdapter.ExpenseViewHolder>() {

        inner class ExpenseViewHolder(itemView: View) :
            RecyclerView.ViewHolder(itemView) {

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
