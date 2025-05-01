package com.example.selfconfidencefit.ui.presentation.screens.workout

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material3.Card
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.selfconfidencefit.data.local.models.workout.WorkoutPlan
import com.example.selfconfidencefit.features.workout.WorkoutViewModel
import androidx.compose.ui.Modifier

@Composable
fun WorkoutPlansScreen(
    viewModel: WorkoutViewModel = hiltViewModel(),
    onNavigateToAddPlan: () -> Unit,
    onNavigateToPlanDetails: (Long) -> Unit
) {
    val workoutPlans by viewModel.workoutPlans.collectAsState()

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(onClick = onNavigateToAddPlan) {
                Icon(Icons.Default.Add, contentDescription = "Add Workout Plan")
            }
            FloatingActionButton(onClick = onNavigateToAddPlan) {
                Icon(Icons.Default.AddCircle, contentDescription = "Add Workout Plan")
            }
        }

    ) { padding ->
        LazyColumn(modifier = Modifier.padding(padding)) {
            items(workoutPlans) { plan ->
                WorkoutPlanItem(
                    plan = plan,
                    onClick = { onNavigateToPlanDetails(plan.id) }
                )
            }
        }
    }
}

@Composable
fun WorkoutPlanItem(plan: WorkoutPlan, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxWidth()
            .padding(8.dp)
            .clickable(onClick = onClick)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(text = plan.name, style = MaterialTheme.typography.headlineSmall)
            Text(text = plan.type, style = MaterialTheme.typography.displayMedium)
            Text(text = plan.description, style = MaterialTheme.typography.bodyMedium)
        }
    }
}