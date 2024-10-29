package com.example.selfconfidencefit.data.local.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.LocalDate

@Entity(tableName = "steps_days")
data class StepsDay(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    @ColumnInfo
    val steps: Int = 0,
    @ColumnInfo
    val date: LocalDate // Date or LocalDate
)
