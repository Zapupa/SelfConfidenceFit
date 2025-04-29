package com.example.selfconfidencefit.data.local.models.workout

import androidx.room.Entity
import androidx.room.ForeignKey

@Entity(
    tableName = "workout_plan_exercise_join",
    primaryKeys = ["workoutPlanId", "exerciseId"],
    foreignKeys = [
        ForeignKey(
            entity = WorkoutPlan::class,
            parentColumns = ["id"],
            childColumns = ["workoutPlanId"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = Exercise::class,
            parentColumns = ["id"],
            childColumns = ["exerciseId"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class WorkoutPlanExerciseJoin(
    val workoutPlanId: Long,
    val exerciseId: Long
)