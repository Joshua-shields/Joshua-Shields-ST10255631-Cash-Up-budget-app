package com.example.cashup.Database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import androidx.room.Delete

@Dao
interface GoalDao {
    // Insert a new goal
    @Insert
    suspend fun insertGoal(goal: Goal)

    // Get all goals for a specific user
    @Query("SELECT * FROM goals_table WHERE userId = :userId")
    suspend fun getAllGoalsForUser(userId: Int): List<Goal>

    // Get goals by type for a specific user
    @Query("SELECT * FROM goals_table WHERE userId = :userId AND goalType = :goalType")
    suspend fun getGoalsByType(userId: Int, goalType: String): List<Goal>

    // Get weekly goals
    @Query("SELECT * FROM goals_table WHERE userId = :userId AND goalType = 'WEEKLY'")
    suspend fun getWeeklyGoals(userId: Int): List<Goal>

    // Get monthly goals
    @Query("SELECT * FROM goals_table WHERE userId = :userId AND goalType = 'MONTHLY'")
    suspend fun getMonthlyGoals(userId: Int): List<Goal>

    // Get yearly goals
    @Query("SELECT * FROM goals_table WHERE userId = :userId AND goalType = 'YEARLY'")
    suspend fun getYearlyGoals(userId: Int): List<Goal>

    // Update goal
    @Update
    suspend fun updateGoal(goal: Goal)

    // Delete a goal
    @Delete
    suspend fun deleteGoal(goal: Goal)

    // Get a specific goal by ID
    @Query("SELECT * FROM goals_table WHERE id = :goalId")
    suspend fun getGoalById(goalId: Int): Goal?

    // Get incomplete goals
    @Query("SELECT * FROM goals_table WHERE userId = :userId AND isCompleted = 0")
    suspend fun getIncompleteGoals(userId: Int): List<Goal>

    // Get completed goals
    @Query("SELECT * FROM goals_table WHERE userId = :userId AND isCompleted = 1")
    suspend fun getCompletedGoals(userId: Int): List<Goal>
}
