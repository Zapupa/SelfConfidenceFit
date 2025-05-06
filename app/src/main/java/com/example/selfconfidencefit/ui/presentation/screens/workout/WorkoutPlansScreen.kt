package com.example.selfconfidencefit.ui.presentation.screens.workout

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.selfconfidencefit.data.local.models.workout.WorkoutPlan
import com.example.selfconfidencefit.features.workout.WorkoutViewModel
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.res.imageResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.selfconfidencefit.R
import com.example.selfconfidencefit.data.local.models.workout.WorkoutPlanProgress

@Composable
fun WorkoutPlansScreen(
    viewModel: WorkoutViewModel = hiltViewModel(),
    onPlanSelected: (Long) -> Unit,
    onCreateNewPlan: () -> Unit
) {
    val plans by viewModel.workoutPlans.collectAsState()
    Scaffold(
        floatingActionButton = {
            ExtendedFloatingActionButton(
                onClick = onCreateNewPlan,
                icon = { Icon(Icons.Default.Add, "Добавить") },
                text = { Text("Добавить план") }
            )
        }
    ) { padding ->
        LazyColumn (modifier = Modifier.padding(padding)) {
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
    Card(
        onClick = onClick,
        modifier = Modifier.padding(horizontal = 18.dp, vertical = 6.dp),
    ) {
        Row (
            modifier = Modifier.padding(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                modifier = Modifier.size(90.dp),
                bitmap = ImageBitmap.imageResource(R.drawable.exercisesicon),
                contentDescription = "Rfhnbyrf"
            )

            Spacer(modifier = Modifier.width(6.dp))

            Column {
                Text(
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary,
                    text = plan.name
                )

                Text(plan.type)

                Spacer(modifier = Modifier.height(6.dp))

                LinearProgressIndicator(
                    progress = { plan.totalProgress / 100f },
                    modifier = Modifier.fillMaxWidth(),
                )

                Text("${plan.totalProgress}% завершено")

                Text("Упражнений: ${progress.size}")
            }

        }
    }
}