package com.example.selfconfidencefit.ui.presentation.screens.workout

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.selfconfidencefit.data.local.models.workout.WorkoutPlan
import com.example.selfconfidencefit.features.workout.WorkoutViewModel
import androidx.compose.ui.Modifier
import com.example.selfconfidencefit.data.local.models.workout.WorkoutPlanProgress

@Composable
fun WorkoutPlansScreen(
    viewModel: WorkoutViewModel = hiltViewModel(),
    onPlanSelected: (Long) -> Unit,
    onCreateNewPlan: () -> Unit
) {
    val plans by viewModel.workoutPlans.collectAsState()

    Column {
        Button(onClick = onCreateNewPlan) { Text("Создать новыый план") }

        LazyColumn {
            items(plans) { planWithProgress ->
                WorkoutPlanCard(
                    plan = planWithProgress.workoutPlan,
                    progress = planWithProgress.progress,
                    onClick = { onPlanSelected(planWithProgress.workoutPlan.id) }
                )
            }
        }
    }

}

@Composable
fun WorkoutPlanCard(
    plan: WorkoutPlan,
    progress: List<WorkoutPlanProgress>,
    onClick: () -> Unit
) {
    Card(onClick = onClick) {
        Column {
            Text(plan.name)
            LinearProgressIndicator(
                progress = { plan.totalProgress / 100f },
                modifier = Modifier.fillMaxWidth(),
            )
            Text("${plan.totalProgress}% завершено")
            Text("Упражнений: ${progress.size}")
        }
    }
}