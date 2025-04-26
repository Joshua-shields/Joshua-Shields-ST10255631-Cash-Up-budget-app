package com.example.cashup.Database

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.sql.Blob
import java.util.Date

@Entity (tableName = "expense_table")
data class Expense(
@PrimaryKey(autoGenerate = true)
val id: Int = 0,
val userId: Int,
val type: String,
val amount: Double,
val notes: String,
val documentation: Blob, //blob storage for holding different types of documentation
val startDate: Date,
val endDate: Date,

)
