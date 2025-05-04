package com.example.selfconfidencefit.data.local.dao.workout

import androidx.room.Dao
import androidx.room.Embedded
import androidx.room.Insert
import androidx.room.Junction
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Relation
import androidx.room.Transaction
import androidx.room.Update
import com.example.selfconfidencefit.data.local.models.workout.Exercise
import com.example.selfconfidencefit.data.local.models.workout.WorkoutPlan
import com.example.selfconfidencefit.data.local.models.workout.WorkoutPlanExerciseJoin
import com.example.selfconfidencefit.data.local.models.workout.WorkoutPlanProgress
import com.example.selfconfidencefit.data.local.models.workout.WorkoutPlanWithDetails
import kotlinx.coroutines.flow.Flow

@Dao
interface WorkoutPlanDao {
    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun insertWorkoutPlan(workoutPlan: WorkoutPlan): Long

    @Update
    suspend fun updateWorkoutPlan(workoutPlan: WorkoutPlan)

    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun insertExercise(exercise: Exercise): Long

    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun insertJoin(join: WorkoutPlanExerciseJoin)

    @Query("SELECT * FROM workout_plans")
    fun getWorkoutPlans(): Flow<List<WorkoutPlan>>

    @Query("SELECT * FROM workout_plans")
    fun getAllWorkoutPlans(): Flow<List<WorkoutPlanWithProgress>>

    @Transaction
    @Query("SELECT * FROM workout_plans WHERE id = :workoutPlanId")
    suspend fun getWorkoutPlanWithExercises(workoutPlanId: Long): WorkoutPlanWithExercises

    @Query("SELECT * FROM workout_plans WHERE id = :workoutPlanId")
    suspend fun getWorkoutPlan(workoutPlanId: Long): WorkoutPlan?

    @Query("SELECT * FROM workout_plan_exercise_join WHERE workoutPlanId = :workoutPlanId")
    suspend fun getExerciseJoins(workoutPlanId: Long): List<WorkoutPlanExerciseJoin>

    @Query("SELECT * FROM exercises WHERE id IN (:exerciseIds)")
    suspend fun getExercisesByIds(exerciseIds: List<Long>): List<Exercise>

    @Query("SELECT exerciseId FROM workout_plan_exercise_join WHERE workoutPlanId = :workoutPlanId")
    suspend fun getExerciseIdsForPlan(workoutPlanId: Long): List<Long>

    @Query("SELECT * FROM exercises")
    fun getExercises(): Flow<List<Exercise>>

    @Query("SELECT * FROM exercises WHERE id IN (SELECT exerciseId FROM workout_plan_exercise_join WHERE workoutPlanId = :workoutPlanId)")
    suspend fun getExercisesForWorkoutPlan(workoutPlanId: Long): List<Exercise>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertProgress(progress: WorkoutPlanProgress)

    @Update(onConflict = OnConflictStrategy.REPLACE)
    suspend fun updateProgress(progress: WorkoutPlanProgress)

    suspend fun getWorkoutPlanWithDetails(workoutPlanId: Long): WorkoutPlanWithDetails? {
        val workoutPlan = getWorkoutPlanById(workoutPlanId) ?: return null
        val exercises = getExercisesForPlan(workoutPlanId)
        val progress = getProgressForPlan(workoutPlanId)

        return WorkoutPlanWithDetails(workoutPlan, exercises, progress)
    }

    @Query("SELECT * FROM workout_plans WHERE id = :workoutPlanId")
    suspend fun getWorkoutPlanById(workoutPlanId: Long): WorkoutPlan?

    @Query("""
        SELECT exercises.* FROM exercises
        INNER JOIN workout_plan_exercise_join 
        ON exercises.id = workout_plan_exercise_join.exerciseId
        WHERE workout_plan_exercise_join.workoutPlanId = :workoutPlanId
    """)
    suspend fun getExercisesForPlan(workoutPlanId: Long): List<Exercise>

    @Query("SELECT * FROM workout_plan_progress WHERE workoutPlanId = :workoutPlanId")
    suspend fun getProgressForPlan(workoutPlanId: Long): List<WorkoutPlanProgress>
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
    val exercises: List<Exercise>,
    @Relation(
        parentColumn = "id",
        entityColumn = "workoutPlanId"
    )
    val progress: List<WorkoutPlanProgress>
)

data class WorkoutPlanWithProgress(
    @Embedded val workoutPlan: WorkoutPlan,
    @Relation(
        parentColumn = "id",
        entityColumn = "workoutPlanId"
    )
    val progress: List<WorkoutPlanProgress>
)



