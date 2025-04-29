package com.example.cashup.Database

import androidx.room.Entity
import androidx.room.PrimaryKey

//--------------------------START OF FILE-----------------------------------//
@Entity(tableName = "user_table") //setting the name of the table responsible for holding data about user information
data class User(

    @PrimaryKey(autoGenerate = true) //auto-generative primary key to keep them unique
    val id: Int = 0, //allows for the unique ID number of each users
    val firstName: String, //attribute for the user's first name
    val lastName: String, //attribute for the user's last name
     val email: String, //attribute to hold the user's email
    val password: String, //database attribute for the storing of  passwords
)
//------------------------END OF FILE--------------------------------------//