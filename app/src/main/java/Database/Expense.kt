package com.example.cashup.Database

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Date

//----------------------------------START OF FILE-------------------------------//
@Entity (tableName = "expense_table") //setting the name of the table responsible for holding data about expenses
data class Expense(
    @PrimaryKey(autoGenerate = true) //auto-generative primary key to keep them unique
    val id: Int = 0,
    val userId: Int, //attribute for the ID for the user
    val type: String, //attribute for holding the name of the expense like fuel
    val amount: Double, // attribute for holding the amount for an expense
    val notes: String? = null, //attribute for notes which can be implemented in part 3 of the POE
    val receiptUri: String? = null, //attribute used for storing the receipts of the user
    val startDate: Date, //start date attribute for use in the filtering
    val endDate: Date, //end date attribute also for the filtering section
    val createdAt: Date = Date(), //the creation date attribute
    val category: String? = null // responsible for holding the category name
)
//-------------------------------END OF FILE----------------------------//