package com.example.cashup.Database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import java.util.Date

//-------------------------------------------START OF FILE---------------------------------------//
@Dao
interface ExpenseDao {
    @Insert
    suspend fun insertExpense(expense: Expense)

    //get all expenses for a specific user
    @Query("SELECT * FROM expense_table WHERE userId = :userId ORDER BY startDate DESC")
    suspend fun getUserExpenses(userId: Int): List<Expense>

    //get all the expenses
    @Query("SELECT * FROM expense_table ORDER BY startDate DESC")
    suspend fun getExpenses(): List<Expense>


    //get expenses by category within a specified date-range (using BETWEEN for startDate)
    @Query("SELECT * FROM expense_table WHERE userId = :userId AND category = :category AND startDate BETWEEN :startDate AND :endDate ORDER BY startDate DESC") // <-- FIXED: Changed 'type' to 'category'
    suspend fun getExpensesByCategoryAndDateRange(userId: Int, category: String, startDate: Date, endDate: Date): List<Expense>

    // Get expenses within a specified date-range (using BETWEEN for startDate)
    @Query("SELECT * FROM expense_table WHERE userId = :userId AND startDate BETWEEN :startDate AND :endDate ORDER BY startDate DESC")
    suspend fun getExpensesByDateRange(userId: Int, startDate: Date, endDate: Date): List<Expense> // Renamed params

    //get expenses that start within a specific month needed for search function
    @Query("SELECT * FROM expense_table WHERE userId = :userId AND startDate >= :monthStart AND startDate < :monthEnd ORDER BY startDate")
    suspend fun getExpensesByMonth(userId: Int, monthStart: Date, monthEnd: Date): List<Expense>

    //get expenses that start within a specific week
    @Query("SELECT * FROM expense_table WHERE userId = :userId AND startDate >= :weekStart AND startDate < :weekEnd ORDER BY startDate")
    suspend fun getExpensesByWeek(userId: Int, weekStart: Date, weekEnd: Date): List<Expense>

    //get a specific expense by ID
    @Query("SELECT * FROM expense_table WHERE id = :expenseId")
    suspend fun getExpenseID(expenseId: Int): Expense?

    //filter attempt by matching the type to the category name
    @Query("SELECT * FROM expense_table WHERE userId = :userId AND type = :categoryName ORDER BY startDate DESC") // <-- PROBLEM HERE
    suspend fun getExpensesCategoryName(userId: Int, categoryName: String): List<Expense>


    //update expense
    @Update
    suspend fun updateExpense(expense: Expense)

    //delete an expense (check with group if desired)
    @Delete
    suspend fun deleteExpense(expense: Expense)

    //retrieve total expenses  for a user
    @Query("SELECT SUM(amount) FROM expense_table WHERE userId = :userId")
    suspend fun getTotalExpenseAmount(userId: Int): Double?

    //get total expense amount within a date range (using BETWEEN for startDate)
    @Query("SELECT SUM(amount) FROM expense_table WHERE userId = :userId AND startDate BETWEEN :startDate AND :endDate")
    suspend fun getTotalExpenseAmountInRange(userId: Int, startDate: Date, endDate: Date): Double? // Renamed params

    //get expenses with notes
    @Query("SELECT * FROM expense_table WHERE userId = :userId AND notes IS NOT NULL AND notes != '' ORDER BY startDate DESC")
    suspend fun getExpensesWithNotes(userId: Int): List<Expense>

    //get expenses without notes
    @Query("SELECT * FROM expense_table WHERE userId = :userId AND (notes IS NULL OR notes = '') ORDER BY startDate DESC")
    suspend fun getExpensesWithoutNotes(userId: Int): List<Expense>

    //get expenses with receipt attachments
    @Query("SELECT * FROM expense_table WHERE userId = :userId AND receiptUri IS NOT NULL ORDER BY startDate DESC")
    suspend fun getExpensesWithReceipts(userId: Int): List<Expense>

    //delete all expenses for a user (check with group)
    @Query("DELETE FROM expense_table WHERE userId = :userId")
    suspend fun deleteExpensesForUser(userId: Int)
}

//--------------------------END OF FILE---------------------------------------------------//