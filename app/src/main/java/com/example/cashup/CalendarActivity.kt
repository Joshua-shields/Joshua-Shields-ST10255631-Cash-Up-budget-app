
package com.example.cashup.com.example.cashup

//---------------------------------------- START OF IMPORTS -------------------------------------//

    import android.app.DatePickerDialog

    import android.content.Intent

    import android.net.Uri

    import android.os.Bundle

    import android.view.View

    import android.widget.Button

    import android.widget.CalendarView

    import android.widget.TextView

    import android.widget.Toast

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

    import java.text.SimpleDateFormat // DATE FORMATING

    import java.util.Calendar // CALENDAR ACCESS

    import java.util.Date // DATE CLASS

    import java.util.Locale

    import com.example.cashup.Database.ExpenseDatabase


//---------------------------------------- END  OF IMPORTS -------------------------------------//
















class CalendarActivity : AppCompatActivity() {

    //------------------- START OF GLOBAL VARIABLES ----------------------//




    //*************************** UI  VARIABLES **************************//

    private lateinit var calendarView: CalendarView
    private lateinit var startDateButton: Button
    private lateinit var endDateButton: Button
    private lateinit var searchButton: MaterialButton
    private lateinit var expensesRecyclerView: RecyclerView
    private lateinit var noExpensesText: TextView


    //////////////////////////// DATABASE VARIABLES ///////////////////////////

    private lateinit var database: ExpenseDatabase

    //---------------------- DATE VARIABLES ----------------------------------//

    private var startDate: Date = Calendar.getInstance().time
    private var endDate: Date = Calendar.getInstance().time
    private val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())



    /*
    * THIS NEEDS ALTERATION. SET USERID TO 1.
    *
    * WE NEED TO IMPLEMENT AUTOINCREMENT OR SOMETHING TO THAT NATURE
    * */
    private val currentUserId = 1


    /*
    *
    *
    *
    *
    *
    *
    * */






    //------------------- END OF GLOBAL VARIABLES ----------------------//










    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_calendar)


        database = ExpenseDatabase.getDatabase(this)






        initializeViews()

        setupListeners()


        setDefaultDateRange() // SET DEFAUT TO CURRENT MONTH

        updateDateButtonsText()


        loadExpensesForSelectedPeriod() // CURRENT MINT EXPENSE LOADED


        markDatesWithExpenses() // DATES GET EXPENSES
    }

    private fun initializeViews() {
        calendarView = findViewById(R.id.calendarView)

        startDateButton = findViewById(R.id.startDateButton)

        endDateButton = findViewById(R.id.endDateButton)

        searchButton = findViewById(R.id.searchButton)

       expensesRecyclerView = findViewById(R.id.expensesRecyclerView)

        noExpensesText = findViewById(R.id.noExpensesText)





        ///////////////////////////////////////////////////////////////////////////////

        expensesRecyclerView.layoutManager = LinearLayoutManager(this)

        //////////////////////////////////////////////////////////////////////////////
    }

    private fun setupListeners() {

        startDateButton.setOnClickListener {
            showDatePickerDialog(true)
        }


        endDateButton.setOnClickListener {
            showDatePickerDialog(false)
        }

        // Search button
        searchButton.setOnClickListener {
            loadExpensesForSelectedPeriod()
        }



/////////////////////////////////////////////////////////////////////////


        // Calendar date selection
        calendarView.setOnDateChangeListener { _, year, month, dayOfMonth ->
            val calendar = Calendar.getInstance()
            calendar.set(year, month, dayOfMonth)

            // Update both start and end date to the selected date
            startDate = calendar.time
            endDate = calendar.time

            // Update UI
            updateDateButtonsText()
            loadExpensesForSelectedPeriod()
        }




///////////////////////////////////////////////////////////////////////////////////////////////////////



    }

    private fun setDefaultDateRange() {

        val calendar = Calendar.getInstance()

        //  first day of current month
        calendar.set(Calendar.DAY_OF_MONTH, 1)
        startDate = calendar.time

        // last day of current month
        calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMaximum(Calendar.DAY_OF_MONTH))
        endDate = calendar.time
    }

    private fun updateDateButtonsText() {

        startDateButton.text = dateFormat.format(startDate)


        endDateButton.text = dateFormat.format(endDate)
    }

    private fun showDatePickerDialog(isStartDate: Boolean) {

        val calendar = Calendar.getInstance()

        calendar.time = if (isStartDate) startDate else endDate

        DatePickerDialog(
            this,
            { _, year, month, dayOfMonth ->
                calendar.set(year, month, dayOfMonth)
                if (isStartDate) {


                    startDate = calendar.time

                    // If start date is after end date, adjust end date
                    if (startDate.after(endDate)) {
                        endDate = startDate
                    }
                } else {
                    endDate = calendar.time
                    // If end date is before start date, adjust start date
                    if (endDate.before(startDate)) {

                        startDate = endDate
                    }
                }
                updateDateButtonsText()
            },
            calendar.get(Calendar.YEAR),

            calendar.get(Calendar.MONTH),

            calendar.get(Calendar.DAY_OF_MONTH)

        ).show()
    }

    private fun loadExpensesForSelectedPeriod() {
        lifecycleScope.launch {
            try {
                // checking  selected date range
                val expenses = withContext(Dispatchers.IO) {

                    database.expenseDao().getExpensesByDateRange(currentUserId, startDate, endDate)
                }

                // Update UI with expenses
                withContext(Dispatchers.Main) {

                    if (expenses.isNotEmpty()) {

                        noExpensesText.visibility = View.GONE

                        expensesRecyclerView.visibility = View.VISIBLE

                        // Set adapter with expenses
                        expensesRecyclerView.adapter = ExpenseAdapter(expenses) { expense ->
                            // Handle expense click (show receipt if available)
                            expense.receiptUri?.let { uri ->
                                showReceipt(uri)
                            }
                        }
                    } else {
                        noExpensesText.visibility = View.VISIBLE
                        expensesRecyclerView.visibility = View.GONE
                    }
                }

                // Highlight dates with expenses on calendar
                markDatesWithExpenses()

            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    Toast.makeText(this@CalendarActivity,
                        "Error loading expenses: ${e.message}",
                        Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun markDatesWithExpenses() {
        lifecycleScope.launch {
            try {
                // Get month start and end
                val calendar = Calendar.getInstance()
                calendar.time = calendarView.date.let { Date(it) }

                calendar.set(Calendar.DAY_OF_MONTH, 1)
                val monthStart = calendar.time

                calendar.set(Calendar.DAY_OF_MONTH,
                    calendar.getActualMaximum(Calendar.DAY_OF_MONTH))
                val monthEnd = calendar.time

                // Get all expenses for this month
                val expenseDates = withContext(Dispatchers.IO) {

                    database.expenseDao().getExpensesByDateRange(

                        currentUserId, monthStart, monthEnd)
                        .map { it.startDate }
                }


                if (expenseDates.isNotEmpty()) {
                    val earliestExpenseDate = expenseDates.minOrNull()
                    earliestExpenseDate?.let {
                        calendarView.date = it.time
                    }
                }
            } catch (e: Exception) {
                /*
                *
                *
                *
                *
                *
                *
                *
                * */
            }
        }
    }

    private fun showReceipt(receiptUriString: String) {
        try {
            val receiptUri = Uri.parse(receiptUriString)
            val intent = Intent(Intent.ACTION_VIEW).apply {
                setDataAndType(receiptUri, "image/*")
                addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
            }
            startActivity(intent)
        } catch (e: Exception) {
            Toast.makeText(this,
                "Cannot open receipt: ${e.message}",
                Toast.LENGTH_SHORT).show()
        }
    }

















///******************************************************************************************///







    inner class ExpenseAdapter(
        private val expenses: List<Expense>,
        private val onExpenseClick: (Expense) -> Unit
    ) : RecyclerView.Adapter<ExpenseAdapter.ExpenseViewHolder>() {

        inner class ExpenseViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
            val typeText: TextView = itemView.findViewById(R.id.expenseTypeText)
            val amountText: TextView = itemView.findViewById(R.id.expenseAmountText)
            val dateText: TextView = itemView.findViewById(R.id.expenseDateText)
            val hasReceiptIndicator: View = itemView.findViewById(R.id.receiptIndicator)
        }

        override fun onCreateViewHolder(parent: android.view.ViewGroup, viewType: Int): ExpenseViewHolder {
            val itemView = layoutInflater.inflate(
                R.layout.item_expense, parent, false)
            return ExpenseViewHolder(itemView)
        }

        override fun onBindViewHolder(holder: ExpenseViewHolder, position: Int) {
            val expense = expenses[position]

            holder.typeText.text = expense.type
            holder.amountText.text = "R%.2f".format(expense.amount)
            holder.dateText.text = dateFormat.format(expense.startDate)

            // Show receipt indicator if receipt is available
            holder.hasReceiptIndicator.visibility =
                if (expense.receiptUri != null) View.VISIBLE else View.GONE

            // Set click listener
            holder.itemView.setOnClickListener {
                onExpenseClick(expense)
            }
        }

        override fun getItemCount() = expenses.size
    }
}