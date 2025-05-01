package com.example.selfconfidencefit.ui.presentation.screens.workout

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.selfconfidencefit.features.workout.WorkoutViewModel
import kotlinx.coroutines.delay

@Composable
fun ExerciseExecutionScreen(
    viewModel: WorkoutViewModel = hiltViewModel(),
    planId: Long,
    onComplete: () -> Unit
) {
    val plan by viewModel.currentWorkoutPlan.collectAsState()
    val currentExercise = remember(plan) { plan?.let { viewModel.getCurrentExercise(it) } }

    if (currentExercise == null) {
        LaunchedEffect(Unit) {
            viewModel.loadWorkoutPlan(planId)
        }
        CircularProgressIndicator()
        return
    }

    var timeLeft by remember { mutableStateOf(currentExercise.durationSeconds) }
    var repsDone by remember { mutableStateOf(0) }

    LaunchedEffect(currentExercise.isTimed) {
        if (currentExercise.isTimed) {
            while (timeLeft > 0) {
                delay(1000)
                timeLeft--
            }
            viewModel.completeCurrentExercise()
            onComplete()
        }
    }

    Column {
        Text(currentExercise.name, style = MaterialTheme.typography.headlineSmall)

        if (currentExercise.isTimed) {
            Text("Осталось: ${timeLeft} сек")
        } else {
            Text("Повторений: ${repsDone}/${currentExercise.repetitions}")
            Button(onClick = {
                repsDone++
                if (repsDone >= currentExercise.repetitions) {
                    viewModel.completeCurrentExercise()
                    onComplete()
                }
            }) {
                Text("Завершить повторение")
            }
        }

        Button(onClick = {
            viewModel.completeCurrentExercise()
            onComplete()
        }) {
            Text("Пропустить упражнение")
        }
    }
}