package com.example.cashup.Database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update

@Dao
interface ExpenseDao {
    @Insert
    suspend fun insertExpense(expense: Expense)

    //get all expenses for a specific user
    @Query("SELECT * FROM expense_table WHERE userId = :userId")
    suspend fun getUserExpenses(userId: Int): List<Expense>

    //get all expenses
    @Query("SELECT * FROM expense_table")
    suspend fun getExpenses(): List<Expense>

    //get expense by type
    @Query("SELECT * FROM expense_table WHERE userId = :userId AND type = :expenseType")
    suspend fun getExpensesByType(userId: Int, expenseType: String): List<Expense>

    //get expenses within a specified date-range
    @Query("SELECT * FROM expense_table WHERE userId = :userId AND startDate >= :fromDate AND endDate <= :toDate")
    suspend fun getExpensesByDateRange(

        userId: Int,
        fromDate: java.util.Date,
        toDate: java.util.Date
    ): List<Expense>

    //get a specific expense by ID
    @Query("SELECT * FROM expense_table WHERE id = :expenseId")
    suspend fun getExpenseID(expenseId: Int): Expense?

    //update expense
    @Update
    suspend fun updateExpense(expense: Expense)

    //deleting an expense (check with group if desired)
    @Delete
    suspend fun deleteExpense(expense: Expense)

    //retrieve total expense amount for a user
    @Query("SELECT SUM(amount) FROM expense_table WHERE userId = :userId")
    suspend fun getExpenseAmount(userId: Int): Double?

    //get expenses with notes
    @Query("SELECT * FROM expense_table WHERE userId = :userId AND notes != ''")
    suspend fun getExpensesNotes(userId: Int): List<Expense>

    //get expenses without notes
    @Query("SELECT * FROM expense_table WHERE userId = :userId AND (notes IS NULL OR notes = '')")
    suspend fun getExpensesWithoutNotes(userId: Int): List<Expense>


    @Query("SELECT * FROM expense_table WHERE userId = :userId AND documentation IS NOT NULL")
    suspend fun getExpenseDocumentation(userId: Int): List<Expense>

    //delete all expenses for a user
    @Query("DELETE FROM expense_table WHERE userId = :userId")
    suspend fun deleteExpensesForUser(userId: Int)
}