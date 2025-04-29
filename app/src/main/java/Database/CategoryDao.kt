package com.example.cashup.Database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

//---------------------START OF FILE--------------------------//
@Dao
interface CategoryDao {
    @Insert
    suspend fun insertCategory(category: Category) //inserts a new category into the category table

    //count the number of rows that have the same name
    @Query("SELECT COUNT(*) FROM category_table WHERE name = :name")
    suspend fun categoryExists(name: String): Int

    //get everything from the category table
    @Query("SELECT * FROM category_table")
    suspend fun getAllCategories(): List<Category>
}
//---------------------------END OD FILE--------------------------//