package com.example.selfconfidencefit.ui.presentation.screens.workout

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.selfconfidencefit.data.local.models.workout.Exercise
import com.example.selfconfidencefit.data.local.models.workout.WorkoutPlanProgress
import com.example.selfconfidencefit.data.local.models.workout.WorkoutPlanWithDetails
import com.example.selfconfidencefit.features.workout.WorkoutViewModel
import com.example.selfconfidencefit.utils.Resource
import kotlinx.coroutines.delay

@Composable
fun WorkoutExecutionScreen(
    planId: Long,
    viewModel: WorkoutViewModel = hiltViewModel(),
    onFinish: () -> Unit
) {
    val state by viewModel.workoutPlanState.collectAsState()

    LaunchedEffect(planId) {
        viewModel.loadWorkoutPlan(planId)
    }

    when (val currentState = state) {
        is Resource.Loading -> {
            Box(modifier = Modifier.fillMaxSize()) {
                CircularProgressIndicator()
            }
        }
        is Resource.Error -> {
            ErrorScreen(
                message = (state as Resource.Error).message,
                onRetry = {  }
            )
        }
        is Resource.Success -> {
            val plan = currentState.data
            val currentExercise = viewModel.getCurrentExercise(plan)

            if (currentExercise == null) {
                // Показываем экран завершения только если действительно нет текущих упражнений
                val allExercises = plan.exercises
                val allProgress = plan.progress

                if (allExercises.isNotEmpty() && allProgress.isNotEmpty()) {
                    WorkoutCompleteScreen(onFinish = onFinish)
                } else {
                    // Если упражнений нет вообще - показываем ошибку
                    ErrorScreen(
                        message = "В этом плане нет упражнений",
                        onRetry = {  }
                    )
                }
            } else {
                ExerciseScreen(
                    exercise = currentExercise.exercise,
                    progress = currentExercise.progress,
                    onComplete = { viewModel.completeCurrentExercise() },
                    onSkip = { viewModel.completeCurrentExercise() }
                )
            }
        }
    }
}

data class ExerciseWithProgress(
    val exercise: Exercise,
    val progress: WorkoutPlanProgress
)

@Composable
fun ErrorScreen(
    message: String,
    onRetry: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Icon(
            imageVector = Icons.Default.Clear,
            contentDescription = "Error",
            tint = MaterialTheme.colorScheme.error,
            modifier = Modifier.size(80.dp)
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "Ошибка",
            style = MaterialTheme.typography.headlineMedium,
            color = MaterialTheme.colorScheme.error
        )

        Text(
            text = message,
            style = MaterialTheme.typography.bodyLarge,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(vertical = 16.dp)
        )

        Button(onClick = onRetry) {
            Text("Повторить попытку")
        }
    }
}