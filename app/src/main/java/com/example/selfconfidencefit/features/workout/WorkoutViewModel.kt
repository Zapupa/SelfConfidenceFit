package com.example.selfconfidencefit.features.workout

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.selfconfidencefit.data.local.models.workout.Exercise
import com.example.selfconfidencefit.data.local.models.workout.WorkoutPlan
import com.example.selfconfidencefit.data.local.models.workout.WorkoutPlanWithDetails
import com.example.selfconfidencefit.data.local.models.workout.WorkoutPlanWithExercises
import com.example.selfconfidencefit.data.local.repository.workout.WorkoutRepository
import com.example.selfconfidencefit.ui.presentation.screens.workout.ExerciseWithProgress
import com.example.selfconfidencefit.utils.Resource
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

    private val _workoutPlanState = MutableStateFlow<Resource<WorkoutPlanWithDetails>>(Resource.Loading)
    val workoutPlanState = _workoutPlanState.asStateFlow()

    // Exercises
    private val _exercises = MutableStateFlow<List<Exercise>>(emptyList())
    val exercises: StateFlow<List<Exercise>> = _exercises.asStateFlow()

    init {
        loadWorkoutPlans()
        loadExercises()
    }

    fun completeCurrentExercise() {
        viewModelScope.launch{
            val currentState = _workoutPlanState.value
            if (currentState is Resource.Success) {
                val currentPlan = currentState.data
                Log.d("ExerciseExecution", currentPlan.toString())
                getCurrentExercise(currentPlan)?.let { exerciseWithProgress ->
                    Log.d("ExerciseExecution", exerciseWithProgress.toString())
                    try{
                        repository.completeExercise(
                            progressId = exerciseWithProgress.progress.id,
                            workoutPlanId = currentPlan.workoutPlan.id,
                            exerciseId = exerciseWithProgress.exercise.id
                        )
                    }
                    catch(e: Exception) {
                        Log.e("ExerciseExecution", e.toString())
                    }
                    repository.completeExercise(
                        progressId = exerciseWithProgress.progress.id,
                        workoutPlanId = currentPlan.workoutPlan.id,
                        exerciseId = exerciseWithProgress.exercise.id
                    )
                    loadWorkoutPlan(currentPlan.workoutPlan.id) // Перезагружаем обновленные данные
                }
            }
        }

    }

    fun getCurrentExercise(plan: WorkoutPlanWithDetails): ExerciseWithProgress? {
        // Убедитесь, что упражнения и прогресс синхронизированы
        if (plan.exercises.size != plan.progress.size) {
            return null
        }

        return plan.progress
            .zip(plan.exercises)
            .firstOrNull { (progress, _) -> !progress.completed }
            ?.let { (progress, exercise) ->
                ExerciseWithProgress(exercise, progress)
            }
    }

    private fun loadWorkoutPlans() {
        viewModelScope.launch {
            repository.getWorkoutPlans().collect { plans ->
                _workoutPlans.value = plans
            }
        }
    }

    suspend fun loadWorkoutPlan(planId: Long) {
        try {
            // Сначала исправляем целостность данных
            repository.fixDataIntegrity(planId)

            // Затем загружаем план
            val plan = repository.getWorkoutPlanWithDetails(planId)
            _workoutPlanState.value = Resource.Success(plan)
        } catch (e: Exception) {
            _workoutPlanState.value = Resource.Error(e.message ?: "Ошибка загрузки")
        }
    }

    private fun loadExercises() {
        viewModelScope.launch {
            repository.getExercises().collect { exercises ->
                _exercises.value = exercises
            }
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
}