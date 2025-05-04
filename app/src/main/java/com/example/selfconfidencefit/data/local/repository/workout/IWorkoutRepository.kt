package com.example.selfconfidencefit.data.local.repository.workout

import com.example.selfconfidencefit.data.local.models.workout.Exercise
import com.example.selfconfidencefit.data.local.models.workout.WorkoutPlan
import com.example.selfconfidencefit.data.local.models.workout.WorkoutPlanWithDetails
import com.example.selfconfidencefit.data.local.models.workout.WorkoutPlanWithExercises
import com.example.selfconfidencefit.data.local.models.workout.WorkoutPlanWithProgress
import kotlinx.coroutines.flow.Flow

interface IWorkoutRepository {

    // WorkoutPlan
    suspend fun getWorkoutPlans() :Flow<List<WorkoutPlan>>

    fun getAllWorkoutPlans() :Flow<List<WorkoutPlanWithProgress>>

    suspend fun getWorkoutPlanWithExercises(workoutPlanId: Long) : WorkoutPlanWithExercises

    suspend fun getWorkoutPlanWithDetails(workoutPlanId: Long): WorkoutPlanWithDetails

    // Exercise
    suspend fun insertExercise(exercise: Exercise): Long

    suspend fun getExercises() :Flow<List<Exercise>>

    suspend fun getExercisesForWorkoutPlan(workoutPlanId: Long): List<Exercise>

    suspend fun fixDataIntegrity(workoutPlanId: Long)

    // Join
    suspend fun addExerciseToWorkoutPlan(workoutPlanId: Long, exerciseId: Long)

    suspend fun createWorkoutPlanWithExercises(plan: WorkoutPlan, exercises: List<Exercise>): Long

    suspend fun completeExercise(progressId: Long, workoutPlanId: Long, exerciseId: Long)
}