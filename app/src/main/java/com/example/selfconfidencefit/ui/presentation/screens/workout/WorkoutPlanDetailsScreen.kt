package com.example.selfconfidencefit.ui.presentation.screens.workout

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.selfconfidencefit.data.local.models.workout.Exercise
import com.example.selfconfidencefit.features.workout.WorkoutViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WorkoutPlanDetailsScreen(
    workoutPlanId: Long,
    viewModel: WorkoutViewModel = hiltViewModel(),
    onBack: () -> Unit
) {
    val workoutPlan by viewModel.selectedWorkoutPlan.collectAsState()
    val exercises by viewModel.exercises.collectAsState()

    LaunchedEffect(workoutPlanId) {
        viewModel.selectWorkoutPlan(workoutPlanId)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(workoutPlan?.name ?: "Workout Plan") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { padding ->
        Column(modifier = Modifier.padding(padding)) {
            if (workoutPlan != null) {
                Text(
                    text = workoutPlan!!.description,
                    modifier = Modifier.padding(16.dp)
                )
                Text(
                    text = "Type: ${workoutPlan!!.type}",
                    modifier = Modifier.padding(16.dp)
                )
            }

            // Exercises in this plan
            LazyColumn {
                items(exercises) { exercise ->
                    ExerciseItem(exercise = exercise)
                }
            }
        }
    }
}

@Composable
fun ExerciseItem(exercise: Exercise) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(text = exercise.name, style = MaterialTheme.typography.headlineSmall)
            Text(text = "${exercise.caloriesBurned} kcal", style = MaterialTheme.typography.bodyMedium)
            Text(text = exercise.description, style = MaterialTheme.typography.bodyMedium)
        }
    }
}