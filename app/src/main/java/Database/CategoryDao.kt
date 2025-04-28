package com.example.cashup.Database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface CategoryDao {
    @Insert
    suspend fun insertCategory(category: Category)

    @Query("SELECT COUNT(*) FROM category_table WHERE name = :name")
    suspend fun categoryExists(name: String): Int

    @Query("SELECT * FROM category_table")
    suspend fun getAllCategories(): List<Category>
}