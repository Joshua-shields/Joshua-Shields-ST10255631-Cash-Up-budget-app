package com.example.cashup.Database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

//----------------------------------START OF FILE--------------------------------------//
@Dao
interface UserDao {
  //user login
 //retrieve the row where both the email and password match and only display one of them
   @Query("SELECT * FROM user_table WHERE email = :email AND password = :password LIMIT 1")
    suspend fun findUserByCredentials(email: String, password: String): User?

//handles inserting a user object into the user table/ database
    @Insert
    suspend fun insertUser(user: User)

//retrieve the row where the email matches the request and only display one of them
    @Query("SELECT * FROM user_table WHERE email = :email LIMIT 1")
    suspend fun getUserByEmail(email: String): User? // Use suspend for coroutines
}
//--------------------------------END OF FILE-----------------------------------------//