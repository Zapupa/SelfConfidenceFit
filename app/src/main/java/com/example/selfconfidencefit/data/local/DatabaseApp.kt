package com.example.selfconfidencefit.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.selfconfidencefit.data.local.dao.StepsDao
import com.example.selfconfidencefit.data.local.models.StepsDay
import com.example.selfconfidencefit.data.local.models.StepsGoal

@Database(entities = [StepsDay::class, StepsGoal::class], version = 1, exportSchema = false)
abstract class DatabaseApp : RoomDatabase() {
    abstract val stepsDao: StepsDao

    companion object{
        @Volatile
        private var INSTANCE: DatabaseApp? = null

        fun getDatabase(context: Context): DatabaseApp{
            return INSTANCE ?: synchronized(this){
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    DatabaseApp::class.java,
                    "selfconfidencemain2.db"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}