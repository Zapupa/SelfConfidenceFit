package com.example.selfconfidencefit.data.local.models.workout

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "workout_plan_progress")
data class WorkoutPlanProgress(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val workoutPlanId: Long,
    val exerciseId: Long,
    val completed: Boolean = false,
    val completionTime: Long = 0 // Timestamp выполнения
)
