package com.example.selfconfidencefit.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.selfconfidencefit.data.local.dao.pedometer.StepsDao
import com.example.selfconfidencefit.data.local.dao.workout.WorkoutPlanDao
import com.example.selfconfidencefit.data.local.models.pedometer.StepsDay
import com.example.selfconfidencefit.data.local.models.pedometer.StepsGoal
import com.example.selfconfidencefit.data.local.models.workout.Exercise
import com.example.selfconfidencefit.data.local.models.workout.WorkoutPlan
import com.example.selfconfidencefit.data.local.models.workout.WorkoutPlanExerciseJoin
import com.example.selfconfidencefit.data.local.models.workout.WorkoutPlanProgress

@Database(
    entities = [
        StepsDay::class,
        StepsGoal::class,
        WorkoutPlan::class,
        Exercise::class,
        WorkoutPlanExerciseJoin::class,
        WorkoutPlanProgress::class
    ],
    version = 3,
    exportSchema = false
)
abstract class DatabaseApp : RoomDatabase() {
    abstract val stepsDao: StepsDao
    abstract fun workoutPlanDao(): WorkoutPlanDao

    companion object{
        @Volatile
        private var INSTANCE: DatabaseApp? = null

        fun getDatabase(context: Context): DatabaseApp{
            return INSTANCE ?: synchronized(this){
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    DatabaseApp::class.java,
                    "selfconfidencemain3.db"
                )
                .build()
                INSTANCE = instance
                instance
            }
        }
    }

}