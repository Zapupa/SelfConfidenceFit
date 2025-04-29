package com.example.selfconfidencefit.data.local.dao.workout

import androidx.room.Dao
import androidx.room.Embedded
import androidx.room.Insert
import androidx.room.Junction
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Relation
import androidx.room.Transaction
import com.example.selfconfidencefit.data.local.models.workout.Exercise
import com.example.selfconfidencefit.data.local.models.workout.WorkoutPlan
import com.example.selfconfidencefit.data.local.models.workout.WorkoutPlanExerciseJoin
import kotlinx.coroutines.flow.Flow

@Dao
interface WorkoutPlanDao {
    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun insertWorkoutPlan(workoutPlan: WorkoutPlan)

    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun insertExercise(exercise: Exercise)

    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun insertJoin(join: WorkoutPlanExerciseJoin)

    @Query("SELECT * FROM workout_plans")
    fun getWorkoutPlans(): Flow<List<WorkoutPlan>>

    @Transaction
    @Query("SELECT * FROM workout_plans WHERE id = :workoutPlanId")
    suspend fun getWorkoutPlanWithExercises(workoutPlanId: Long): WorkoutPlanWithExercises

    @Query("SELECT * FROM exercises")
    fun getExercises(): Flow<List<Exercise>>

    @Query("SELECT * FROM exercises WHERE id IN (SELECT exerciseId FROM workout_plan_exercise_join WHERE workoutPlanId = :workoutPlanId)")
    suspend fun getExercisesForWorkoutPlan(workoutPlanId: Long): List<Exercise>
}

data class WorkoutPlanWithExercises(
    @Embedded val workoutPlan: WorkoutPlan,
    @Relation(
        entity = Exercise::class,
        parentColumn = "id",
        entityColumn = "id",
        associateBy = Junction(
            WorkoutPlanExerciseJoin::class,
            parentColumn = "workoutPlanId",
            entityColumn = "exerciseId"
        )
    )
    val exercises: List<Exercise>
)