package com.example.selfconfidencefit.features.workout

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.selfconfidencefit.data.local.models.workout.EditableExercise
import com.example.selfconfidencefit.data.local.models.workout.Exercise
import com.example.selfconfidencefit.data.local.models.workout.WorkoutPlan
import com.example.selfconfidencefit.data.local.repository.workout.WorkoutRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class CreateWorkoutViewModel @Inject constructor(
    private val repository: WorkoutRepository
) : ViewModel() {

    var planName by mutableStateOf("")
    var planDescription by mutableStateOf("")
    var planType by mutableStateOf("Общая")

    val exercises = mutableStateListOf<EditableExercise>()

    fun addEmptyExercise() {
        exercises.add(EditableExercise())
    }

    fun removeExercise(index: Int) {
        exercises.removeAt(index)
    }

    fun updateExercise(index: Int, exercise: EditableExercise) {
        exercises[index] = exercise
    }

    // Сохранение плана
    suspend fun saveWorkoutPlan(): Result<Long> = try {
        val workoutPlan = WorkoutPlan(
            name = planName,
            description = planDescription,
            type = planType
        )

        val exercisesToSave = exercises.map { editable ->
            Exercise(
                name = editable.name,
                description = editable.description,
                isTimed = editable.isTimed,
                durationSeconds = editable.durationSeconds,
                repetitions = editable.repetitions,
                caloriesBurned = editable.caloriesBurned,
                type = editable.type,
                imageUrl = editable.imageUrl
            )
        }

        val planId = repository.createWorkoutPlanWithExercises(workoutPlan, exercisesToSave)
        Result.success(planId)
    } catch (e: Exception) {
        Result.failure(e)
    }
}