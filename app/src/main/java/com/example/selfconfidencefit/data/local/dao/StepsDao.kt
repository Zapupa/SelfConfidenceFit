package com.example.selfconfidencefit.data.local.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.selfconfidencefit.data.local.models.StepsDay
import com.example.selfconfidencefit.data.local.models.StepsGoal

@Dao
interface StepsDao {
    //Шаги
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertStepsDay(steps: StepsDay)
    @Update(onConflict = OnConflictStrategy.REPLACE)
    suspend fun updateStepsDay(steps: StepsDay)
    @Query("UPDATE steps_days SET steps = steps + :stepsToAdd WHERE id IN(SELECT id FROM steps_days ORDER BY date DESC LIMIT 1)")
    suspend fun addLatestSteps(stepsToAdd: Int)
    @Query("SELECT * FROM steps_days WHERE id = :key")
    fun getStepsDay(key: Long): LiveData<StepsDay?>
    @Query("SELECT * FROM steps_days ORDER BY date DESC")
    fun getAllStepsDays(): LiveData<List<StepsDay>> //val
    @Query("SELECT * FROM steps_days ORDER BY date DESC LIMIT 1")
    fun getLatestStepsDay(): LiveData<StepsDay> //val?
    @Query("SELECT * FROM steps_days ORDER BY date DESC LIMIT 1")
    fun getLatestStepsDayObservable(): LiveData<StepsDay> //val
    @Query("DELETE FROM steps_days WHERE id NOT IN(SELECT id FROM steps_days ORDER BY date DESC LIMIT 1)")
    suspend fun deleteAllStepsDaysButLatest()
    @Query("DELETE FROM steps_days WHERE id NOT IN (SELECT id FROM(SELECT id FROM steps_days ORDER BY id DESC LIMIT 7) foo)")
    suspend fun deleteAllStepsDaysButSeven()

    //Цель
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertGoal(goal: StepsGoal)
    @Update(onConflict = OnConflictStrategy.REPLACE)
    suspend fun updateGoal(goal: StepsGoal)
    @Delete
    suspend fun deleteGoal(goal: StepsGoal)
    @Query("SELECT * FROM steps_goals WHERE id = :key")
    fun getGoal(key: Long): LiveData<StepsGoal?>
    @Query("SELECT * FROM steps_goals")
    fun getAllGoals(): LiveData<List<StepsGoal>> //val
}