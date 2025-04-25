package com.example.cashup.Database

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "goals_table")
data class Goal(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val userId: Int,
    val title: String,
    val description: String,
    val targetAmount: Double,
    val currentAmount: Double = 0.0,
    val goalType: String,  // "WEEKLY", "MONTHLY", or "YEARLY"
    val startDate: Long,   // Storing as timestamp
    val endDate: Long,     // Store as timestamp
    val isCompleted: Boolean = false
)
