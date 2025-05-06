package com.example.selfconfidencefit.ui.presentation.screens.home

import android.icu.util.Calendar
import android.util.Log
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.RadioButton
import androidx.compose.material3.TextButton
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.wear.compose.material.Text
import com.example.selfconfidencefit.data.local.models.pedometer.StepsDay
import com.example.selfconfidencefit.data.local.models.pedometer.StepsGoal
import com.example.selfconfidencefit.data.local.models.workout.EditableExercise
import com.example.selfconfidencefit.features.auth.AuthViewModel
import com.example.selfconfidencefit.ui.theme.lightGray
import com.example.selfconfidencefit.features.pedometer.StepsViewModel
import com.example.selfconfidencefit.features.workout.WorkoutViewModel
import com.example.selfconfidencefit.ui.presentation.screens.workout.ExerciseEditDialog
import com.example.selfconfidencefit.utils.DateFormat
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun MainScreen(
    stepVM: StepsViewModel = hiltViewModel<StepsViewModel>(),
    navController: NavController
){
    val steps by stepVM.todaySteps.collectAsState(initial = 0)

    val goalSteps by stepVM.activeGoal.collectAsState(initial = 10000)

    var showGoalDialog by remember { mutableStateOf(false) }

    val stepDays = stepVM.readAllStepsDays().observeAsState(listOf()).value
    val lastDay = stepVM.readLatestStepsDayObservable().observeAsState().value

    Box(
        modifier = Modifier.fillMaxSize().padding(vertical = 30.dp)
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier.size(250.dp)
            ) {
                // Фоновый круг
                Canvas(modifier = Modifier.fillMaxSize()) {
                    drawCircle(
                        color = Color.LightGray.copy(alpha = 0.3f),
                        radius = size.minDimension / 2,
                        style = Stroke(width = 20f)
                    )
                }

                // Прогресс
                CircularProgressBar(
                    progress = steps.toFloat() / goalSteps,
                    modifier = Modifier.fillMaxSize()
                )

                // Количество шагов
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "$steps",
                        fontSize = 48.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary
                    )
                    Text(
                        text = "шагов",
                        fontSize = 16.sp,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    // Прогресс в процентах
                    Text(
                        text = "${(steps * 100 / goalSteps).coerceAtMost(100)}%",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Medium,
                        color = MaterialTheme.colorScheme.primary
                    )
                }
            }

            Spacer(modifier = Modifier.height(32.dp))

            // Дополнительная информация
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                InfoCard(
                    title = "Цель",
                    value = "$goalSteps",
                    modifier = Modifier.weight(1f),
                    onClick = {showGoalDialog = true}
                )
                InfoCard(
                    title = "Осталось",
                    value = "${(goalSteps - steps).coerceAtLeast(0)}",
                    modifier = Modifier.weight(1f),
                    onClick = {}
                )
            }
            Card(
                modifier = Modifier
                    .padding(8.dp)
                    .fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surfaceVariant
                )
            ){
                LazyRow(
                    modifier = Modifier.padding(8.dp),
                    horizontalArrangement = Arrangement.Start
                ) {
                    items(stepDays) { stepDay ->
                        StepItem(stepDay = stepDay, viewModel = stepVM)
                    }
                }
            }

        }
    }
    if (showGoalDialog) {
        GoalDialog(
            onDismiss = { showGoalDialog = false },
            onSave = {
                showGoalDialog = false
            }
        )
    }
}

@Composable
fun CircularProgressBar(
    progress: Float,
    modifier: Modifier = Modifier,
    color: Color = MaterialTheme.colorScheme.primary,
    strokeWidth: Float = 20f
) {
    Canvas(modifier = modifier) {
        val sweepAngle = progress * 360
        drawArc(
            color = color,
            startAngle = -90f,
            sweepAngle = sweepAngle,
            useCenter = false,
            style = Stroke(
                width = strokeWidth,
                cap = StrokeCap.Round
            ),
            size = Size(size.width, size.height)
        )
    }
}

@Composable
fun InfoCard(title: String, value: String, modifier: Modifier = Modifier, onClick: () -> Unit) {
    Card(
        modifier = modifier.padding(8.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        ),
        onClick = onClick
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = title,
                fontSize = 14.sp,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
            )
            Text(
                text = value,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary
            )
        }
    }
}

@Composable
fun StepItem(stepDay: StepsDay, viewModel: StepsViewModel) {

    val dayOfWeek = DateFormat.parseDayOfWeek(stepDay.date)

    Card(
        modifier = Modifier.padding(16.dp),
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = dayOfWeek,
                fontSize = 14.sp,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
            )

            Spacer(modifier = Modifier.height(6.dp))

            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier.size(20.dp)
            ){
                Canvas(modifier = Modifier.size(20.dp)) {
                    drawCircle(
                        color = Color.White,
                        radius = size.minDimension / 2,
                        style = Stroke(width = 20f)
                    )
                }

                CircularProgressBar(
                    progress = stepDay.steps.toFloat() / 10000,
                    modifier = Modifier.size(20.dp)
                )
            }

            Spacer(modifier = Modifier.height(6.dp))

            Text(
                text = stepDay.steps.toString(),
                fontSize = 14.sp,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
            )
        }

    }
}

@Composable
fun GoalDialog(
    onDismiss: () -> Unit,
    onSave: () -> Unit,
    viewModel: StepsViewModel = hiltViewModel()
) {

    var goalValue by remember { mutableIntStateOf(10) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(text = "Цель") },
        text = {
            Column {
                OutlinedTextField(
                    value = goalValue.toString(),
                    onValueChange = {
                        goalValue = it.toIntOrNull() ?: 0
                    },
                    label = { androidx.compose.material3.Text("Название упражнения") },
                    modifier = Modifier.fillMaxWidth()
                )
            }
        },
        confirmButton = {
            Button(onClick = {
                viewModel.insertGoal(
                    goal = StepsGoal(
                        id = 1,
                        goal = goalValue
                    ),
                    onSuccess = onDismiss
                )
            }
            ) {
                Text("Сохранить")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Отмена")
            }
        }
    )
}