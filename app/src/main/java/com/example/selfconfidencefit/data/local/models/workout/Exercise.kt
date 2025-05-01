package com.example.selfconfidencefit.data.local.models.workout

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "exercises")
data class Exercise(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    @ColumnInfo
    val name: String,              // Название упражнения
    @ColumnInfo
    val description: String,      // Описание
    @ColumnInfo
    val imageUrl: String?,         // Ссылка на фото
    @ColumnInfo
    val caloriesBurned: Int,       // Количество затрачиваемых калорий
    @ColumnInfo
    val type: String,              // Тип упражнения
    @ColumnInfo
    val isTimed: Boolean, // true - на время, false - на повторения
    @ColumnInfo
    val durationSeconds: Int, // Длительность в секундах (если isTimed = true)
    @ColumnInfo
    val repetitions: Int, // Количество повторений (если isTimed = false)
)
