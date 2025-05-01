package com.example.selfconfidencefit.data.local.repository.workout

import com.example.selfconfidencefit.data.local.dao.workout.WorkoutPlanDao
import com.example.selfconfidencefit.data.local.dao.workout.WorkoutPlanWithProgress
import com.example.selfconfidencefit.data.local.models.workout.Exercise
import com.example.selfconfidencefit.data.local.models.workout.WorkoutPlan
import com.example.selfconfidencefit.data.local.models.workout.WorkoutPlanExerciseJoin
import com.example.selfconfidencefit.data.local.models.workout.WorkoutPlanProgress
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class WorkoutRepository @Inject constructor(private val workoutPlanDao: WorkoutPlanDao): IWorkoutRepository {
    // WorkoutPlan
    suspend fun insertWorkoutPlan(workoutPlan: WorkoutPlan) = workoutPlanDao.insertWorkoutPlan(workoutPlan)
    override suspend fun getWorkoutPlans(): Flow<List<WorkoutPlan>> = workoutPlanDao.getWorkoutPlans()
    override fun getAllWorkoutPlans(): Flow<List<WorkoutPlanWithProgress>> = workoutPlanDao.getAllWorkoutPlans()
    override suspend fun getWorkoutPlanWithExercises(workoutPlanId: Long) = workoutPlanDao.getWorkoutPlanWithExercises(workoutPlanId)

    // Exercise
    suspend fun insertExercise(exercise: Exercise) = workoutPlanDao.insertExercise(exercise)
    override suspend fun getExercises(): Flow<List<Exercise>> = workoutPlanDao.getExercises()
    override suspend fun getExercisesForWorkoutPlan(workoutPlanId: Long) = workoutPlanDao.getExercisesForWorkoutPlan(workoutPlanId)

    // Join
    override suspend fun addExerciseToWorkoutPlan(workoutPlanId: Long, exerciseId: Long) {
        workoutPlanDao.insertJoin(WorkoutPlanExerciseJoin(workoutPlanId, exerciseId))
    }

    override suspend fun createWorkoutPlanWithExercises(
        plan: WorkoutPlan,
        exercises: List<Exercise>
    ): Long {
        val planId = workoutPlanDao.insertWorkoutPlan(plan)

        exercises.forEach { exercise ->
            val exerciseId = workoutPlanDao.insertExercise(exercise)
            workoutPlanDao.insertProgress(
                WorkoutPlanProgress(
                    workoutPlanId = planId,
                    exerciseId = exerciseId
                )
            )
        }

        return planId
    }

    override suspend fun completeExercise(workoutPlanId: Long, exerciseId: Long) {
        // Обновляем прогресс упражнения
        workoutPlanDao.updateProgress(
            WorkoutPlanProgress(
                workoutPlanId = workoutPlanId,
                exerciseId = exerciseId,
                completed = true,
                completionTime = System.currentTimeMillis()
            )
        )

        // Обновляем общий прогресс плана
        val plan = workoutPlanDao.getWorkoutPlanWithExercises(workoutPlanId)
        val totalExercises = plan.exercises.size
        val completedExercises = plan.progress.count { it.completed }
        val newProgress = (completedExercises.toFloat() / totalExercises.toFloat() * 100).toInt()

        workoutPlanDao.updateWorkoutPlan(
            plan.workoutPlan.copy(
                totalProgress = newProgress,
                lastCompleted = System.currentTimeMillis()
            )
        )
    }
}