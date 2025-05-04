package com.example.selfconfidencefit.data.local.models.workout

import androidx.room.Embedded
import androidx.room.Junction
import androidx.room.Relation

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