package com.example.selfconfidencefit.data.local.repository.workout

import com.example.selfconfidencefit.data.local.dao.workout.WorkoutPlanDao
import com.example.selfconfidencefit.data.local.models.workout.Exercise
import com.example.selfconfidencefit.data.local.models.workout.WorkoutPlan
import com.example.selfconfidencefit.data.local.models.workout.WorkoutPlanExerciseJoin
import com.example.selfconfidencefit.data.local.models.workout.WorkoutPlanProgress
import com.example.selfconfidencefit.data.local.models.workout.WorkoutPlanWithDetails
import com.example.selfconfidencefit.data.local.models.workout.WorkoutPlanWithProgress
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class WorkoutRepository @Inject constructor(private val workoutPlanDao: WorkoutPlanDao): IWorkoutRepository {
    // WorkoutPlan
    override suspend fun getWorkoutPlans(): Flow<List<WorkoutPlan>> = workoutPlanDao.getWorkoutPlans()

    override fun getAllWorkoutPlans(): Flow<List<WorkoutPlanWithProgress>> = workoutPlanDao.getAllWorkoutPlans()

    override suspend fun getWorkoutPlanWithExercises(workoutPlanId: Long) = workoutPlanDao.getWorkoutPlanWithExercises(workoutPlanId)

    override suspend fun getWorkoutPlanWithDetails(workoutPlanId: Long): WorkoutPlanWithDetails {
        val workoutPlan = workoutPlanDao.getWorkoutPlan(workoutPlanId)
            ?: throw IllegalArgumentException("Workout plan not found")

        val exercises = workoutPlanDao.getExercisesForPlan(workoutPlanId)
        val progress = workoutPlanDao.getProgressForPlan(workoutPlanId)

        return WorkoutPlanWithDetails(
            workoutPlan = workoutPlan,
            exercises = exercises,
            progress = progress
        )
    }

    override suspend fun createWorkoutPlanWithExercises(plan: WorkoutPlan, exercises: List<Exercise>): Long {
        val planId = workoutPlanDao.insertWorkoutPlan(plan)

        exercises.forEach { exercise ->
            val exerciseId = workoutPlanDao.insertExercise(exercise)

            workoutPlanDao.insertJoin(WorkoutPlanExerciseJoin(planId, exerciseId))

            workoutPlanDao.insertProgress(
                WorkoutPlanProgress(
                    workoutPlanId = planId,
                    exerciseId = exerciseId,
                    completed = false
                )
            )
        }

        return planId
    }

    // Exercise
    override suspend fun insertExercise(exercise: Exercise) = workoutPlanDao.insertExercise(exercise)

    override suspend fun getExercises(): Flow<List<Exercise>> = workoutPlanDao.getExercises()

    override suspend fun getExercisesForWorkoutPlan(workoutPlanId: Long) = workoutPlanDao.getExercisesForWorkoutPlan(workoutPlanId)

    // Join
    override suspend fun addExerciseToWorkoutPlan(workoutPlanId: Long, exerciseId: Long) {
        workoutPlanDao.insertJoin(WorkoutPlanExerciseJoin(workoutPlanId, exerciseId))
    }

    //Проверяет таблицу Join на наличие данных
    override suspend fun fixDataIntegrity(workoutPlanId: Long) {
        val existingJoins = workoutPlanDao.getExerciseJoins(workoutPlanId)

        val allProgress = workoutPlanDao.getProgressForPlan(workoutPlanId)

        val missingJoins = allProgress.filter { progress ->
            existingJoins.none { join -> join.exerciseId == progress.exerciseId }
        }

        missingJoins.forEach { progress ->
            workoutPlanDao.insertJoin(WorkoutPlanExerciseJoin(workoutPlanId, progress.exerciseId))
        }
    }

    override suspend fun completeExercise(progressId:Long, workoutPlanId: Long, exerciseId: Long) {
        // прогресс упражнения
        workoutPlanDao.updateProgress(
            WorkoutPlanProgress(
                id = progressId,
                workoutPlanId = workoutPlanId,
                exerciseId = exerciseId,
                completed = true,
                completionTime = System.currentTimeMillis()
            )
        )

        // прогресс плана
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