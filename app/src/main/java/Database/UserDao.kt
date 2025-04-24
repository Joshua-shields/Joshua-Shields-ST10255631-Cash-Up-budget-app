package com.example.cashup.Database

import androidx.room.Dao
import androidx.room.Query
import com.example.cashup.Database.User


@Dao
interface UserDao {

    @Query("SELECT * FROM user_table WHERE email = :email AND password = :password LIMIT 1")
    suspend fun findUserByCredentials(email: String, password: String): User?
}
