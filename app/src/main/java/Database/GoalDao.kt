package com.example.cashup.Database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import androidx.room.Delete

//-----------------------START of FILE-------------------------------//
@Dao
interface GoalDao {
    //inserts a new goal into the goal database
    @Insert
    suspend fun insertGoal(goal: Goal)

    //get all goals for a specific user
    @Query("SELECT * FROM goals_table WHERE userId = :userId")
    suspend fun getAllGoalsForUser(userId: Int): List<Goal>

    //get goals by type for a specific user
    @Query("SELECT * FROM goals_table WHERE userId = :userId AND goalType = :goalType")
    suspend fun getGoalsByType(userId: Int, goalType: String): List<Goal>

    //get the weekly goals
    @Query("SELECT * FROM goals_table WHERE userId = :userId AND goalType = 'WEEKLY'")
    suspend fun getWeeklyGoals(userId: Int): List<Goal>

    //get the monthly goals
    @Query("SELECT * FROM goals_table WHERE userId = :userId AND goalType = 'MONTHLY'")
    suspend fun getMonthlyGoals(userId: Int): List<Goal>

    //get the yearly goals
    @Query("SELECT * FROM goals_table WHERE userId = :userId AND goalType = 'YEARLY'")
    suspend fun getYearlyGoals(userId: Int): List<Goal>

    //updates the goal's table
    @Update
    suspend fun updateGoal(goal: Goal)

    //deletes a goal from the goals_table
    @Delete
    suspend fun deleteGoal(goal: Goal)

    //retrieves a specific goal via its ID
    @Query("SELECT * FROM goals_table WHERE id = :goalId")
    suspend fun getGoalById(goalId: Int): Goal?

    //gets any incomplete goals
    @Query("SELECT * FROM goals_table WHERE userId = :userId AND isCompleted = 0")
    suspend fun getIncompleteGoals(userId: Int): List<Goal>

    //gets completed goals
    @Query("SELECT * FROM goals_table WHERE userId = :userId AND isCompleted = 1")
    suspend fun getCompletedGoals(userId: Int): List<Goal>
}
//-------------------------------------END OF FILE-------------------------------//