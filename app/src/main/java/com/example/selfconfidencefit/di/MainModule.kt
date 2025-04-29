package com.example.selfconfidencefit.di

import android.app.Application
import com.example.selfconfidencefit.data.local.DatabaseApp
import com.example.selfconfidencefit.data.local.dao.pedometer.StepsDao
import com.example.selfconfidencefit.data.local.dao.workout.WorkoutPlanDao
import com.example.selfconfidencefit.data.local.repository.pedometer.StepsRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object MainModule {
    @Singleton
    @Provides
    fun provideDatabase(application: Application) : DatabaseApp {
        return DatabaseApp.getDatabase(application)
    }

    @Singleton
    @Provides
    fun provideStepsDao(databaseApp: DatabaseApp): StepsDao {
        return databaseApp.stepsDao
    }

    @Singleton
    @Provides
    fun provideStepsRepository(stepsDao: StepsDao): StepsRepository {
        return StepsRepository(stepsDao)
    }

    @Provides
    @Singleton
    fun provideWorkoutPlanDao(databaseApp: DatabaseApp): WorkoutPlanDao {
        return databaseApp.workoutPlanDao()
    }
}