package com.example.selfconfidencefit.features.workout

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.selfconfidencefit.data.local.dao.workout.WorkoutPlanWithExercises
import com.example.selfconfidencefit.data.local.models.workout.Exercise
import com.example.selfconfidencefit.data.local.models.workout.WorkoutPlan
import com.example.selfconfidencefit.data.local.repository.workout.WorkoutRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class WorkoutViewModel @Inject constructor(private val repository: WorkoutRepository) : ViewModel() {
    // WorkoutPlans
    private val _workoutPlans = MutableStateFlow<List<WorkoutPlan>>(emptyList())
    val workoutPlans = repository.getAllWorkoutPlans()
        .stateIn(viewModelScope, SharingStarted.Lazily, emptyList())

    private val _currentWorkoutPlan = MutableStateFlow<WorkoutPlanWithExercises?>(null)
    val currentWorkoutPlan = _currentWorkoutPlan.asStateFlow()

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

    suspend fun loadWorkoutPlan(planId: Long) {
        _currentWorkoutPlan.value = repository.getWorkoutPlanWithExercises(planId)
    }

    fun completeCurrentExercise() {
        viewModelScope.launch {
            currentWorkoutPlan.value?.let { plan ->
                val currentExercise = getCurrentExercise(plan)
                currentExercise?.let {
                    repository.completeExercise(plan.workoutPlan.id, it.id)
                }
            }
        }
    }

    fun getCurrentExercise(plan: WorkoutPlanWithExercises): Exercise? {
        return plan.progress
            .filterNot { it.completed }
            .minByOrNull { it.id }
            ?.let { progress ->
                plan.exercises.find { it.id == progress.exerciseId }
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

    fun addExercise(name: String, description: String, caloriesBurned: Int, type: String, imageUrl: String?, isTimed: Boolean, durationSeconds: Int, repetitions: Int) {
        viewModelScope.launch {
            repository.insertExercise(
                Exercise(
                    name = name,
                    description = description,
                    caloriesBurned = caloriesBurned,
                    type = type,
                    imageUrl = imageUrl,
                    isTimed = isTimed,
                    durationSeconds = durationSeconds,
                    repetitions = repetitions
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