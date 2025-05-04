package com.example.selfconfidencefit.data.local.models.workout

import androidx.room.Embedded

data class WorkoutPlanWithDetails(
    @Embedded
    val workoutPlan: WorkoutPlan,

    val exercises: List<Exercise>,

    val progress: List<WorkoutPlanProgress>
)