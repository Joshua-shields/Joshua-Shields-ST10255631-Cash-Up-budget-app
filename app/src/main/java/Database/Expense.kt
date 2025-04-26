package com.example.cashup.Database

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Date

@Entity (tableName = "expense_table")
data class Expense(
@PrimaryKey(autoGenerate = true)
val id: Int = 0,
val userId: Int,
val type: String,
val amount: Double,
val notes: String? = null,
val receiptUri: String? = null,
val startDate: Date,
val endDate: Date,
val createdAt: Date = Date()
)