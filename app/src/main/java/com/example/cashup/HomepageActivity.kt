package com.example.cashup // Make sure this matches your package name

import android.content.Intent
import android.os.Bundle
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.button.MaterialButton
import com.google.android.material.card.MaterialCardView // Import for the CardView


class HomepageActivity : AppCompatActivity() {

    // Declare View variables
    private lateinit var menuButton: ImageButton
    private lateinit var monthTitle: TextView
    private lateinit var balanceLabel: TextView
    private lateinit var balanceAmount: TextView
    private lateinit var statsButton: ImageButton
    private lateinit var premiumButton: ImageButton
    private lateinit var incomeButton: MaterialCardView // The clickable item is the CardView
    private lateinit var searchButton: ImageButton
    private lateinit var expenseButton: MaterialCardView // The clickable item is the CardView
    private lateinit var monthlyFilterButton: MaterialButton
    private lateinit var goalsButton: ImageButton
    private lateinit var calendarButton: ImageButton
    private lateinit var profileButton: ImageButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Set the content view using the layout resource ID
        setContentView(R.layout.activity_homepage)

        // Initialize Views using findViewById
        menuButton = findViewById(R.id.menuButton)
        monthTitle = findViewById(R.id.monthTitle)
        balanceLabel = findViewById(R.id.balanceLabel)
        balanceAmount = findViewById(R.id.balanceAmount)
        statsButton = findViewById(R.id.statsButton)
        premiumButton = findViewById(R.id.premiumButton)
        incomeButton = findViewById(R.id.incomeButton) // Find the CardView by its ID
        searchButton = findViewById(R.id.searchButton)
        expenseButton = findViewById(R.id.expenseButton) // Find the CardView by its ID
        monthlyFilterButton = findViewById(R.id.monthlyFilterButton)
        goalsButton = findViewById(R.id.goalsButton)
        calendarButton = findViewById(R.id.calendarButton)
        profileButton = findViewById(R.id.profileButton)

        // --- Setup Click Listeners ---

        // Top Bar Buttons
        menuButton.setOnClickListener {
            // TODO: Implement menu functionality
            showToast("Menu button clicked")
        }

        statsButton.setOnClickListener {
            // TODO: Navigate to Statistics Activity
            // val intent = Intent(this, StatsActivity::class.java)
            // startActivity(intent)
            showToast("Stats button clicked")
        }

        premiumButton.setOnClickListener {
            // TODO: Implement premium feature action
            showToast("Premium button clicked")
        }

        // Main Action Buttons (Listeners on the CardViews)
        incomeButton.setOnClickListener {
            // TODO: Navigate to Add Income Activity
            // val intent = Intent(this, AddIncomeActivity::class.java)
            // startActivity(intent)
            showToast("Income button clicked")
        }

        searchButton.setOnClickListener {
            // TODO: Implement search functionality or navigate to Search Activity
            // val intent = Intent(this, SearchActivity::class.java)
            // startActivity(intent)
            showToast("Search button clicked")
        }

        expenseButton.setOnClickListener {
            // Navigate to AddExpenseActivity
            // Make sure AddExpenseActivity exists and is imported correctly
            try {
                val intent = Intent(this, activity_add_expense::class.java)
                startActivity(intent)
            } catch (e: ClassNotFoundException) {
                showToast("AddExpenseActivity not found!")
                // Log the error or handle it appropriately
            } catch (e: Exception) {
                showToast("Error opening expense screen")
                // Log the error
            }
        }


        // Monthly Filter Button
        monthlyFilterButton.setOnClickListener {
            // TODO: Implement month selection
            showToast("Monthly filter clicked")
        }

        // Bottom Navigation Buttons
        goalsButton.setOnClickListener {
            // TODO: Navigate to Goals Activity/Fragment
            // val intent = Intent(this, GoalsActivity::class.java)
            // startActivity(intent)
            showToast("Goals button clicked")
        }

        calendarButton.setOnClickListener {
            // TODO: Navigate to Calendar Activity/Fragment
            // val intent = Intent(this, CalendarActivity::class.java)
            // startActivity(intent)
            showToast("Calendar button clicked")
        }

        profileButton.setOnClickListener {
            // TODO: Navigate to Profile Activity/Fragment (Maybe Login?)
            // val intent = Intent(this, ProfileActivity::class.java) // Or LoginActivity
            // startActivity(intent)
            showToast("Profile button clicked")
        }

        // --- You can also update UI elements dynamically ---
        // Example: Set the month title dynamically if needed
        // monthTitle.text = getCurrentMonth()
        // balanceAmount.text = "R${getCurrentBalance()}" // Format appropriately
    }

    // Helper function for showing placeholder messages (optional)
    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    // Example placeholder functions (replace with your actual logic)
    // private fun getCurrentMonth(): String {
    //     // Logic to get the current month name
    //     return "April"
    // }
    //
    // private fun getCurrentBalance(): String {
    //     // Logic to get the current balance from data source (e.g., Room DB)
    //     return "3000.00"
    // }
}
