package com.example.cashup.Database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
//--------------------------------START OF FILE----------------------------------------//

@Database(entities = [Expense::class, Category:: class], version = 1, exportSchema = false)
@TypeConverters(DateConverter::class)
abstract class ExpenseDatabase : RoomDatabase() {

    abstract fun expenseDao(): ExpenseDao //allows access to methods for querying/inserting expenses.
    abstract fun categoryDao(): CategoryDao //allows access to methods for querying/inserting categories.

    companion object { //companion object ensures a single instance of the database is used throughout the app
        @Volatile
        private var INSTANCE: ExpenseDatabase? = null

        fun getDatabase(context: Context): ExpenseDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    ExpenseDatabase::class.java,
                    "expense_database"
                )
                    .fallbackToDestructiveMigration()
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}
//---------------------------------------END OF FILE------------------------------//