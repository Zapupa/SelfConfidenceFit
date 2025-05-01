package com.example.selfconfidencefit.data.local.models.workout

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "workout_plans")
data class WorkoutPlan(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    @ColumnInfo
    val name: String,              // Название плана тренировки
    @ColumnInfo
    val description: String,      // Описание
    @ColumnInfo
    val type: String,              // Тип тренировки
    @ColumnInfo
    val totalProgress: Int = 0, // Общий прогресс выполнения (0-100)
    @ColumnInfo
    val lastCompleted: Long = 0 // Timestamp последнего выполнения
)

