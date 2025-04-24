package com.example.cashup.Database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface IncomeDao {

    @Insert
    suspend fun insertIncome(income: Income)

    @Query("SELECT * FROM income_table")
    suspend fun getAllIncomes(): List<Income>
}