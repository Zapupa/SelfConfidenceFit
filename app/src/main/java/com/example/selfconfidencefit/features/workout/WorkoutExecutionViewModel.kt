package com.example.selfconfidencefit.features.workout

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.selfconfidencefit.data.local.models.workout.WorkoutPlanWithDetails
import com.example.selfconfidencefit.data.local.repository.workout.WorkoutRepository
import com.example.selfconfidencefit.ui.presentation.screens.workout.ExerciseWithProgress
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class WorkoutExecutionViewModel @Inject constructor(
    private val repository: WorkoutRepository
) : ViewModel() {
    private val _currentExerciseState = MutableStateFlow<ExerciseExecutionState>(ExerciseExecutionState.Loading)
    val currentExerciseState = _currentExerciseState.asStateFlow()

    suspend fun loadInitialData(planId: Long) {
        repository.getWorkoutPlanWithDetails(planId).let { plan ->
            getCurrentExercise(plan)?.let { exercise ->
                _currentExerciseState.value = ExerciseExecutionState.Running(
                    exerciseWithProgress = exercise,
                    timeLeft = if (exercise.exercise.isTimed) exercise.exercise.durationSeconds else 0,
                    repsDone = 0
                )
            } ?: run {
                _currentExerciseState.value = ExerciseExecutionState.Completed
            }
        }
    }

    fun completeCurrentExercise() {
        viewModelScope.launch {
            val currentState = _currentExerciseState.value
            Log.d("Zalupa", currentState.toString())
            if (currentState is ExerciseExecutionState.Running) {                repository.completeExercise(
                    progressId = currentState.exerciseWithProgress.progress.id,
                    workoutPlanId = currentState.exerciseWithProgress.progress.workoutPlanId,
                    exerciseId = currentState.exerciseWithProgress.exercise.id
                )
                Log.d("Zalupa", currentState.exerciseWithProgress.progress.workoutPlanId.toString())
                // Загружаем следующий статус
                loadInitialData(currentState.exerciseWithProgress.progress.workoutPlanId)
            }
        }

    }

    fun updateTimeLeft(newTime: Int) {
        val currentState = _currentExerciseState.value
        if (currentState is ExerciseExecutionState.Running) {
            _currentExerciseState.value = currentState.copy(timeLeft = newTime)
        }
    }

    fun incrementRep() {
        val currentState = _currentExerciseState.value
        if (currentState is ExerciseExecutionState.Running) {
            _currentExerciseState.value = currentState.copy(repsDone = currentState.repsDone + 1)
        }
    }

    private suspend fun getCurrentExercise(plan: WorkoutPlanWithDetails): ExerciseWithProgress? {
        return plan.progress
            .firstOrNull { !it.completed }
            ?.let { progress ->
                plan.exercises.find { it.id == progress.exerciseId }?.let { exercise ->
                    ExerciseWithProgress(exercise, progress)
                }
            }
    }

    sealed class ExerciseExecutionState {
        object Loading : ExerciseExecutionState()
        data class Running(
            val exerciseWithProgress: ExerciseWithProgress, // Храним ExerciseWithProgress
            val timeLeft: Int,
            val repsDone: Int
        ) : ExerciseExecutionState()
        object Completed : ExerciseExecutionState()
    }
}