package com.example.cashup.Database

import androidx.room.Dao
import androidx.room.Query
import com.example.cashup.Database.User


@Dao
interface UserDao {

    @Query("SELECT * FROM user_table WHERE email = :email AND password = :password LIMIT 1")
    suspend fun findUserByCredentials(email: String, password: String): User?



    @Insert
    suspend fun insertUser(user: User)
    fun getUserByEmail(email: String) {

    }

    /* @Insert
     suspend fun insertEmail(email : Email)
     */

    
}

