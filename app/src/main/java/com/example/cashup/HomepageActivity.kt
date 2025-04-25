package com.example.cashup // Make sure this matches your package name

import android.content.ActivityNotFoundException // Import specific exception
import android.content.Intent
import android.os.Bundle
import android.util.Log // Import Log for better error reporting
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.cashup.com.example.cashup.CalendarActivity



import com.example.cashup.ProfileActivity
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
            showToast("Menu button clicked")
        }

        statsButton.setOnClickListener {
            showToast("Stats button clicked")
            // TODO: Navigate to Statistics Activity if needed
        }

        calendarButton.setOnClickListener {

            val intent = Intent(this, CalendarActivity::class.java)
            startActivity(intent)
        }

        // PREMIUM / CROWN Button Listener
        premiumButton.setOnClickListener {
            // Navigate to GamifyActivity
            try {
                val intent = Intent(this, GamifyActivity::class.java)
                startActivity(intent)
            } catch (e: ActivityNotFoundException) {
                showToast("GamifyActivity not found!")
                Log.e(
                    "HomepageActivity",
                    "Ensure GamifyActivity exists and is in AndroidManifest.xml",
                    e
                )
            } catch (e: Exception) {
                showToast("Error opening gamification screen: ${e.localizedMessage}")
                Log.e("HomepageActivity", "Error starting GamifyActivity", e)
            }
        }

        // --- Main Action Buttons ---

        // INCOME Button Listener
        incomeButton.setOnClickListener {
            // Navigate to AddIncomeActivity
            // ** Ensure AddIncomeActivity.kt exists and is in AndroidManifest.xml **
            try {
                val intent = Intent(this, AddIncomeActivity::class.java)
                startActivity(intent)
            } catch (e: ActivityNotFoundException) {
                showToast("AddIncomeActivity not found!")
                Log.e(
                    "HomepageActivity",
                    "Ensure AddIncomeActivity exists and is in AndroidManifest.xml",
                    e
                )
            } catch (e: Exception) {
                showToast("Error opening income screen: ${e.localizedMessage}")
                Log.e("HomepageActivity", "Error starting AddIncomeActivity", e)
            }
        }

        searchButton.setOnClickListener {
            showToast("Search button clicked")
            // TODO: Navigate to Search Activity if needed
        }

        // EXPENSE Button Listener
        expenseButton.setOnClickListener {
            // Navigate to AddExpenseActivity
            // ** Ensure AddExpenseActivity.kt exists and is in AndroidManifest.xml **
            try {
                // Use the AddExpenseActivity class name
                val intent = Intent(this, AddExpenseActivity::class.java)
                startActivity(intent)
            } catch (e: ActivityNotFoundException) {
                showToast("AddExpenseActivity not found!")
                Log.e(
                    "HomepageActivity",
                    "Ensure AddExpenseActivity exists and is in AndroidManifest.xml",
                    e
                )
            } catch (e: Exception) {
                showToast("Error opening expense screen: ${e.localizedMessage}")
                Log.e("HomepageActivity", "Error starting AddExpenseActivity", e)
            }
        }

        // Monthly Filter Button
        monthlyFilterButton.setOnClickListener {
            showToast("Monthly filter clicked")
            // TODO: Implement month selection logic
        }

        // --- Bottom Navigation Buttons ---
        goalsButton.setOnClickListener {
            // Navigate to GoalsActivity
            try {
                val intent = Intent(this, GoalsActivity::class.java)
                startActivity(intent)
            } catch (e: ActivityNotFoundException) {
                showToast("GoalsActivity not found!")
                Log.e(
                    "HomepageActivity",
                    "Ensure GoalsActivity exists and is in AndroidManifest.xml",
                    e
                )
            } catch (e: Exception) {
                showToast("Error opening goals screen: ${e.localizedMessage}")
                Log.e("HomepageActivity", "Error starting GoalsActivity", e)
            }
        }

        calendarButton.setOnClickListener {
            // Navigate to CalendarActivity
            try {
                val intent = Intent(this, CalendarActivity::class.java)
                startActivity(intent)
            } catch (e: ActivityNotFoundException) {
                showToast("CalendarActivity not found!")
                Log.e(
                    "HomepageActivity",
                    "Ensure CalendarActivity exists and is in AndroidManifest.xml",
                    e
                )
            } catch (e: Exception) {
                showToast("Error opening calendar screen: ${e.localizedMessage}")
                Log.e("HomepageActivity", "Error starting CalendarActivity", e)
            }
        }

        profileButton.setOnClickListener {
            // Navigate to ProfileActivity
            try {
                val intent = Intent(this, ProfileActivity::class.java)
                startActivity(intent)
            } catch (e: ActivityNotFoundException) {
                showToast("ProfileActivity not found!")
                Log.e(
                    "HomepageActivity",
                    "Ensure ProfileActivity exists and is in AndroidManifest.xml",
                    e
                )
            } catch (e: Exception) {
                showToast("Error opening profile screen: ${e.localizedMessage}")
                Log.e("HomepageActivity", "Error starting ProfileActivity", e)
            }
        }
    }

    // Helper function for showing placeholder messages (optional)
    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}
