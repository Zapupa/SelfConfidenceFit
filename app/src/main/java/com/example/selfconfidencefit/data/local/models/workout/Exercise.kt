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
    val type: String              // Тип упражнения
)
