package com.example.selfconfidencefit.data.local.repository.workout

import com.example.selfconfidencefit.data.local.dao.workout.WorkoutPlanWithExercises
import com.example.selfconfidencefit.data.local.dao.workout.WorkoutPlanWithProgress
import com.example.selfconfidencefit.data.local.models.workout.Exercise
import com.example.selfconfidencefit.data.local.models.workout.WorkoutPlan
import kotlinx.coroutines.flow.Flow

interface IWorkoutRepository {

    // WorkoutPlan
    suspend fun getWorkoutPlans() :Flow<List<WorkoutPlan>>
    fun getAllWorkoutPlans() :Flow<List<WorkoutPlanWithProgress>>
    suspend fun getWorkoutPlanWithExercises(workoutPlanId: Long) : WorkoutPlanWithExercises

    // Exercise
    suspend fun getExercises() :Flow<List<Exercise>>
    suspend fun getExercisesForWorkoutPlan(workoutPlanId: Long): List<Exercise>

    // Join
    suspend fun addExerciseToWorkoutPlan(workoutPlanId: Long, exerciseId: Long)

    suspend fun createWorkoutPlanWithExercises(plan: WorkoutPlan, exercises: List<Exercise>): Long

    suspend fun completeExercise(progressId: Long, workoutPlanId: Long, exerciseId: Long)
}