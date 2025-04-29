package com.example.cashup.Database

import androidx.room.Entity
import androidx.room.PrimaryKey

//------------------------------START OF FILE--------------------------------//
@Entity(tableName = "category_table") //setting the name of the table that manages the categories
data class Category(
    @PrimaryKey(autoGenerate = true) //auto generating of the primary key for each category entity
    val id: Int = 0,
    val name: String, //declaring of the name of the category attribute
    val isDefault: Boolean = false //attribute used to decide if the category name is a custom name or preset name
)
//---------------------------------END OF FILE----------------------------------//