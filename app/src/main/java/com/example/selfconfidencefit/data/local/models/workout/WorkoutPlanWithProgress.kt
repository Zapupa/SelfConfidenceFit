package com.example.selfconfidencefit.data.local.models.workout

import androidx.room.Embedded
import androidx.room.Relation

data class WorkoutPlanWithProgress(
    @Embedded val workoutPlan: WorkoutPlan,
    @Relation(
        parentColumn = "id",
        entityColumn = "workoutPlanId"
    )
    val progress: List<WorkoutPlanProgress>
)