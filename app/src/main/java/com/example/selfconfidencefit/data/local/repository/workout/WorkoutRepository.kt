package com.example.selfconfidencefit.data.local.repository.workout

import com.example.selfconfidencefit.data.local.dao.workout.WorkoutPlanDao
import com.example.selfconfidencefit.data.local.models.workout.Exercise
import com.example.selfconfidencefit.data.local.models.workout.WorkoutPlan
import com.example.selfconfidencefit.data.local.models.workout.WorkoutPlanExerciseJoin
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class WorkoutRepository @Inject constructor(private val workoutPlanDao: WorkoutPlanDao): IWorkoutRepository {
    // WorkoutPlan
    override suspend fun insertWorkoutPlan(workoutPlan: WorkoutPlan) = workoutPlanDao.insertWorkoutPlan(workoutPlan)
    override suspend fun getWorkoutPlans(): Flow<List<WorkoutPlan>> = workoutPlanDao.getWorkoutPlans()
    override suspend fun getWorkoutPlanWithExercises(workoutPlanId: Long) = workoutPlanDao.getWorkoutPlanWithExercises(workoutPlanId)

    // Exercise
    override suspend fun insertExercise(exercise: Exercise) = workoutPlanDao.insertExercise(exercise)
    override suspend fun getExercises(): Flow<List<Exercise>> = workoutPlanDao.getExercises()
    override suspend fun getExercisesForWorkoutPlan(workoutPlanId: Long) = workoutPlanDao.getExercisesForWorkoutPlan(workoutPlanId)

    // Join
    override suspend fun addExerciseToWorkoutPlan(workoutPlanId: Long, exerciseId: Long) {
        workoutPlanDao.insertJoin(WorkoutPlanExerciseJoin(workoutPlanId, exerciseId))
    }
}