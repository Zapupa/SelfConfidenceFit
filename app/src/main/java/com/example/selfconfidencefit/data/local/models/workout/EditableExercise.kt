package com.example.selfconfidencefit.data.local.models.workout

data class EditableExercise(
    val id: Long = 0,
    var name: String = "",
    var description: String = "",
    var isTimed: Boolean = true,
    var durationSeconds: Int = 30,
    var repetitions: Int = 10,
    var caloriesBurned: Int = 100,
    var type: String = "Общая",
    var imageUrl: String? = null
)