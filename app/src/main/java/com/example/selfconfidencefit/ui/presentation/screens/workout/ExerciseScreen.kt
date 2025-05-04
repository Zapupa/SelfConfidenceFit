package com.example.selfconfidencefit.ui.presentation.screens.workout

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.LinearProgressIndicator
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.ProgressIndicatorDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.selfconfidencefit.data.local.models.workout.Exercise
import com.example.selfconfidencefit.data.local.models.workout.WorkoutPlanProgress
import com.example.selfconfidencefit.features.workout.WorkoutViewModel
import kotlinx.coroutines.delay

@Composable
fun ExerciseScreen(
    exercise: Exercise,
    progress: WorkoutPlanProgress,
    onComplete: () -> Unit,
    onSkip: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: WorkoutViewModel = hiltViewModel(),
) {
    var isRunning by remember { mutableStateOf(true) }
    var timeLeft by remember { mutableStateOf(if (exercise.isTimed) exercise.durationSeconds else 0) }
    var repsDone by remember { mutableStateOf(0) }

    LaunchedEffect(isRunning) {
        if (exercise.isTimed && isRunning && timeLeft > 0) {
            while (timeLeft > 0 && isRunning) {
                delay(1000)
                timeLeft--
            }
            if (timeLeft == 0) onComplete()
        }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Column {
            Text(
                text = exercise.name,
                style = MaterialTheme.typography.headlineMedium,
                modifier = Modifier.padding(bottom = 8.dp)
            )

            Text(
                text = exercise.description,
                style = MaterialTheme.typography.bodyLarge,
                modifier = Modifier.padding(bottom = 16.dp)
            )

            if (exercise.isTimed) {
                TimedExerciseView(
                    timeLeft = timeLeft,
                    totalTime = exercise.durationSeconds,
                    isRunning = isRunning,
                    onToggleRunning = { isRunning = !isRunning }
                )
            } else {
                RepetitionExerciseView(
                    repsDone = repsDone,
                    totalReps = exercise.repetitions,
                    onIncrementRep = {
                        repsDone++
                        if (repsDone >= exercise.repetitions) {
                            onComplete()
                        }
                    }
                )
            }
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            OutlinedButton(
                onClick = { viewModel.completeCurrentExercise() },
                modifier = Modifier.weight(1f),
                colors = ButtonDefaults.outlinedButtonColors(
                    contentColor = MaterialTheme.colorScheme.error
                )
            ) {
                Text("Пропустить")
            }

            Spacer(modifier = Modifier.width(16.dp))

            Button(
                onClick = { viewModel.completeCurrentExercise() },
                modifier = Modifier.weight(1f),
                enabled = !exercise.isTimed || timeLeft == 0
            ) {
                Text("Завершить")
            }
        }
    }
}

@Composable
private fun TimedExerciseView(
    timeLeft: Int,
    totalTime: Int,
    isRunning: Boolean,
    onToggleRunning: () -> Unit
) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        CircularProgressIndicator(
            progress = { if (totalTime > 0) 1 - (timeLeft.toFloat() / totalTime.toFloat()) else 0f },
            modifier = Modifier.size(200.dp),
            strokeWidth = 8.dp,
            trackColor = ProgressIndicatorDefaults.circularTrackColor,
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = formatTime(timeLeft),
            style = MaterialTheme.typography.displayLarge
        )

        Spacer(modifier = Modifier.height(16.dp))

        IconButton(
            onClick = onToggleRunning,
            modifier = Modifier.size(64.dp)
        ) {
            Icon(
                imageVector = if (isRunning) Icons.Default.Menu else Icons.Default.PlayArrow,
                contentDescription = if (isRunning) "Pause" else "Resume",
                modifier = Modifier.size(48.dp)
            )
        }
    }
}

@Composable
private fun RepetitionExerciseView(
    repsDone: Int,
    totalReps: Int,
    onIncrementRep: () -> Unit
) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(
            text = "Повторений",
            style = MaterialTheme.typography.titleLarge
        )

        Text(
            text = "$repsDone / $totalReps",
            style = MaterialTheme.typography.displayMedium,
            modifier = Modifier.padding(vertical = 16.dp)
        )

        LinearProgressIndicator(
            progress = if (totalReps > 0) repsDone.toFloat() / totalReps.toFloat() else 0f,
            modifier = Modifier
                .fillMaxWidth()
                .height(8.dp)
        )

        Spacer(modifier = Modifier.height(32.dp))

        Button(
            onClick = onIncrementRep,
            modifier = Modifier.size(120.dp),
            shape = CircleShape
        ) {
            Text("+1")
        }
    }
}

private fun formatTime(seconds: Int): String {
    val minutes = seconds / 60
    val remainingSeconds = seconds % 60
    return String.format("%02d:%02d", minutes, remainingSeconds)
}