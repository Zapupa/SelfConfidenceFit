package com.example.selfconfidencefit.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.selfconfidencefit.data.local.dao.StepsDao
import com.example.selfconfidencefit.data.local.models.StepsDay
import com.example.selfconfidencefit.data.local.models.StepsGoal

@Database(entities = [StepsDay::class, StepsGoal::class], version = 1)
abstract class DatabaseApp : RoomDatabase() {
    abstract val stepsDao: StepsDao
}