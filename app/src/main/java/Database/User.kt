package com.example.cashup.Database

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "user_table")
data class User(

    @PrimaryKey(autoGenerate = true)


    val id: Int = 0,
    val firstName: String,
    val lastName: String,
     val email: String,
    val password: String,

    ///////////////////

   // val username: String,
    ///////////////////

)
