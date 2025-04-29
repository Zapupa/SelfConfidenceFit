package com.example.selfconfidencefit.features.workout

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.selfconfidencefit.data.local.models.workout.Exercise
import com.example.selfconfidencefit.data.local.models.workout.WorkoutPlan
import com.example.selfconfidencefit.data.local.repository.workout.WorkoutRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class WorkoutViewModel @Inject constructor(private val repository: WorkoutRepository) : ViewModel() {
    // WorkoutPlans
    private val _workoutPlans = MutableStateFlow<List<WorkoutPlan>>(emptyList())
    val workoutPlans: StateFlow<List<WorkoutPlan>> = _workoutPlans.asStateFlow()

    // Exercises
    private val _exercises = MutableStateFlow<List<Exercise>>(emptyList())
    val exercises: StateFlow<List<Exercise>> = _exercises.asStateFlow()

    // Выбранный WorkoutPlan
    private val _selectedWorkoutPlan = MutableStateFlow<WorkoutPlan?>(null)
    val selectedWorkoutPlan: StateFlow<WorkoutPlan?> = _selectedWorkoutPlan.asStateFlow()

    init {
        loadWorkoutPlans()
        loadExercises()
    }

    private fun loadWorkoutPlans() {
        viewModelScope.launch {
            repository.getWorkoutPlans().collect { plans ->
                _workoutPlans.value = plans
            }
        }
    }

    private fun loadExercises() {
        viewModelScope.launch {
            repository.getExercises().collect { exercises ->
                _exercises.value = exercises
            }
        }
    }

    fun addWorkoutPlan(name: String, description: String, type: String) {
        viewModelScope.launch {
            repository.insertWorkoutPlan(
                WorkoutPlan(
                    name = name,
                    description = description,
                    type = type
                )
            )
        }
    }

    fun addExercise(name: String, description: String, caloriesBurned: Int, type: String, imageUrl: String?) {
        viewModelScope.launch {
            repository.insertExercise(
                Exercise(
                    name = name,
                    description = description,
                    caloriesBurned = caloriesBurned,
                    type = type,
                    imageUrl = imageUrl
                )
            )
        }
    }

    fun addExerciseToWorkoutPlan(workoutPlanId: Long, exerciseId: Long) {
        viewModelScope.launch {
            repository.addExerciseToWorkoutPlan(workoutPlanId, exerciseId)
        }
    }

    fun selectWorkoutPlan(workoutPlanId: Long) {
        viewModelScope.launch {
            _selectedWorkoutPlan.value = repository.getWorkoutPlanWithExercises(workoutPlanId).workoutPlan
        }
    }

}