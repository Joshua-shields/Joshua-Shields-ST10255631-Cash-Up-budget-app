package com.example.cashup.Database

import androidx.room.Entity
import androidx.room.PrimaryKey

//--------------------------------START OF FILE---------------------------//
@Entity(tableName = "goals_table") //setting the name of the table responsible for holding data about goals
data class Goal(
    @PrimaryKey(autoGenerate = true) //auto-generative primary key to keep them unique
    val id: Int = 0,
    val userId: Int, //uses the user's ID to match the goals with the correct user
    val title: String, //stores the title of the goal in the database
    val description: String, //attribute name and responsible for the storage of the description of the goals
    val targetAmount: Double, //stores the target amount the user set when they created the goal
    val currentAmount: Double,  //holds the current amount of money achieved for the goal
    val goalType: String,  //allows for the storage of the different types of categories be they "WEEKLY", "MONTHLY" or "YEARLY"
    val startDate: Long,   //storage of the start date
    val endDate: Long,     //storage of the end date
    val isCompleted: Boolean = false //used to hold the condition of a goal depending on its status
)
//------------------------------END OF FILE------------------------//