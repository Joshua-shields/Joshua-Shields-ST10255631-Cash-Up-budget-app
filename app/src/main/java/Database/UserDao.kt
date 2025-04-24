package com.example.cashup.Database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.cashup.Database.User

@Dao
interface UserDao {

    @Query("SELECT * FROM user_table WHERE email = :email AND password = :password LIMIT 1")
    suspend fun findUserByCredentials(email: String, password: String): User?

    @Insert
    suspend fun insertUser(user: User)

    // Removed the invalid getUserByEmail function

    /*
    // If you wanted a query to get user by email:
    @Query("SELECT * FROM user_table WHERE email = :email LIMIT 1")
    suspend fun getUserByEmail(email: String): User?
    */

    /* @Insert
     suspend fun insertEmail(email : Email)
     */

}
