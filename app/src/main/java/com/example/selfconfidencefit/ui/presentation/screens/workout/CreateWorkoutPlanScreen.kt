package com.example.selfconfidencefit.ui.presentation.screens.workout

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Checkbox
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.selfconfidencefit.data.local.models.workout.EditableExercise
import com.example.selfconfidencefit.features.workout.CreateWorkoutViewModel
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateWorkoutPlanScreen(
    viewModel: CreateWorkoutViewModel = hiltViewModel(),
    onBack: () -> Unit,
    onSaveComplete: (Long) -> Unit
) {
    val coroutineScope = rememberCoroutineScope()
    var showExerciseDialog by remember { mutableStateOf(false) }
    var editedExerciseIndex by remember { mutableStateOf<Int?>(null) }

    data class DropdownItem(
        val title: String
    )

    val typeList= listOf(
        DropdownItem( "Общая"),
        DropdownItem("На руки"),
        DropdownItem( "На ноги"),
        DropdownItem("На пресс"),
        DropdownItem("На спину")
    )

    var expanded by remember { mutableStateOf(false) }
    var selectedItem by remember { mutableStateOf(DropdownItem("Общая")) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Создание плана тренировки") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, "Назад")
                    }
                }
            )
        },
        floatingActionButton = {
            ExtendedFloatingActionButton(
                onClick = {
                    editedExerciseIndex = null
                    showExerciseDialog = true
                },
                icon = { Icon(Icons.Default.Add, "Добавить") },
                text = { Text("Добавить упражнение") }
            )
        }
    ) { padding ->
        Column(modifier = Modifier.padding(padding)) {
            // Форма плана
            OutlinedTextField(
                value = viewModel.planName,
                onValueChange = { viewModel.planName = it },
                label = { Text("Название плана") },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 12.dp),

            )

            OutlinedTextField(
                value = viewModel.planDescription,
                onValueChange = { viewModel.planDescription = it },
                label = { Text("Описание") },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 12.dp),
            )

            ExposedDropdownMenuBox(
                expanded = expanded,
                onExpandedChange = { expanded = !expanded }
            ) {

                OutlinedTextField(
                    modifier = Modifier
                        .menuAnchor()
                        .fillMaxWidth()
                        .padding(horizontal = 12.dp),
                    readOnly = true,
                    value = selectedItem.title,
                    onValueChange = {},
                    label = { Text("Choose option") },
                    trailingIcon = {
                        ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded)
                    },
                    colors = ExposedDropdownMenuDefaults.outlinedTextFieldColors()
                )

                // Выпадающее меню
                ExposedDropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { expanded = false }
                ) {
                    typeList.forEach { option ->
                        DropdownMenuItem(
                            text = { Text(option.title) },
                            onClick = {
                                viewModel.planType = option.title
                                selectedItem = option
                                expanded = false

                            }
                        )
                    }
                }
            }

            // Список упражнений
            LazyColumn {
                items(viewModel.exercises.size) { index ->
                    val exercise = viewModel.exercises[index]
                    ExerciseListItem(
                        exercise = exercise,
                        onEditClick = {
                            editedExerciseIndex = index
                            showExerciseDialog = true
                        },
                        onRemoveClick = { viewModel.removeExercise(index) }
                    )
                }
            }

            Button(
                onClick = {
                    coroutineScope.launch {
                        viewModel.saveWorkoutPlan()
                            .onSuccess { planId -> onSaveComplete(planId) }
                            .onFailure { /* Обработка ошибки */ }
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                enabled = viewModel.planName.isNotBlank() && viewModel.exercises.isNotEmpty()
            ) {
                Text("Сохранить план тренировки")
            }
        }
    }

    // Диалог редактирования упражнения
    if (showExerciseDialog) {
        ExerciseEditDialog(
            exercise = editedExerciseIndex?.let { viewModel.exercises[it] } ?: EditableExercise(),
            onDismiss = { showExerciseDialog = false },
            onSave = { exercise ->
                editedExerciseIndex?.let { index ->
                    viewModel.updateExercise(index, exercise)
                } ?: viewModel.addEmptyExercise().also {
                    viewModel.updateExercise(viewModel.exercises.lastIndex, exercise)
                }
                showExerciseDialog = false
            }
        )
    }
}

@Composable
fun ExerciseEditDialog(
    exercise: EditableExercise,
    onDismiss: () -> Unit,
    onSave: (EditableExercise) -> Unit
) {
    var currentExercise by remember { mutableStateOf(exercise) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(if (exercise.name.isEmpty()) "Новое упражнение" else "Редактирование") },
        text = {
            Column {
                OutlinedTextField(
                    value = currentExercise.name,
                    onValueChange = { currentExercise = currentExercise.copy(name = it) },
                    label = { Text("Название упражнения") },
                    modifier = Modifier.fillMaxWidth()
                )

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text("Тип:")
                    Spacer(modifier = Modifier.width(8.dp))
                    RadioButton(
                        selected = currentExercise.isTimed,
                        onClick = { currentExercise = currentExercise.copy(isTimed = true) }
                    )
                    Text("На время")
                    RadioButton(
                        selected = !currentExercise.isTimed,
                        onClick = { currentExercise = currentExercise.copy(isTimed = false) }
                    )
                    Text("На повторения")
                }

                if (currentExercise.isTimed) {
                    OutlinedTextField(
                        value = currentExercise.durationSeconds.toString(),
                        onValueChange = {
                            currentExercise = currentExercise.copy(
                                durationSeconds = it.toIntOrNull() ?: 30
                            )
                        },
                        label = { Text("Длительность (сек)") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        modifier = Modifier.fillMaxWidth()
                    )
                } else {
                    OutlinedTextField(
                        value = currentExercise.repetitions.toString(),
                        onValueChange = {
                            currentExercise = currentExercise.copy(
                                repetitions = it.toIntOrNull() ?: 10
                            )
                        },
                        label = { Text("Количество повторений") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        modifier = Modifier.fillMaxWidth()
                    )
                }

                OutlinedTextField(
                    value = currentExercise.description,
                    onValueChange = { currentExercise = currentExercise.copy(description = it) },
                    label = { Text("Описание") },
                    modifier = Modifier.fillMaxWidth()
                )

                OutlinedTextField(
                    value = currentExercise.caloriesBurned.toString(),
                    onValueChange = { currentExercise = currentExercise.copy(caloriesBurned = it.toIntOrNull() ?: 10) },
                    label = { Text("Количество каллорий") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    modifier = Modifier.fillMaxWidth()
                )

                OutlinedTextField(
                    value = currentExercise.type,
                    onValueChange = { currentExercise = currentExercise.copy(type = it) },
                    label = { Text("Тип упражнения") },
                    modifier = Modifier.fillMaxWidth()
                )
            }
        },
        confirmButton = {
            Button(onClick = { onSave(currentExercise) }) {
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

@Composable
fun ExerciseListItem(
    exercise: EditableExercise,
    onEditClick: () -> Unit,
    onRemoveClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = exercise.name.ifEmpty { "Новое упражнение" },
                    style = MaterialTheme.typography.headlineSmall
                )

                Row {
                    IconButton(onClick = onEditClick) {
                        Icon(Icons.Default.Edit, "Редактировать")
                    }
                    IconButton(onClick = onRemoveClick) {
                        Icon(Icons.Default.Delete, "Удалить")
                    }
                }
            }

            Text(text = exercise.description, modifier = Modifier.padding(top = 8.dp))

            Row(modifier = Modifier.padding(top = 8.dp)) {
                if (exercise.isTimed) {
                    Text("${exercise.durationSeconds} сек")
                } else {
                    Text("${exercise.repetitions} повторений")
                }

                Spacer(modifier = Modifier.width(16.dp))

                Text("${exercise.caloriesBurned} ккал")
            }
        }
    }
}