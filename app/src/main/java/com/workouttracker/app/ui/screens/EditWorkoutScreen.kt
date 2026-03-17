package com.workouttracker.app.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.workouttracker.app.data.model.Exercise
import com.workouttracker.app.data.model.ExerciseCategory
import com.workouttracker.app.ui.viewmodel.EditWorkoutViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditWorkoutScreen(
    viewModel: EditWorkoutViewModel,
    workoutId: String?,
    onNavigateBack: () -> Unit
) {
    val workoutName by viewModel.workoutName.collectAsState()
    val workoutDescription by viewModel.workoutDescription.collectAsState()
    val exercises by viewModel.exercises.collectAsState()
    val isSaving by viewModel.isSaving.collectAsState()

    LaunchedEffect(workoutId) {
        viewModel.loadWorkout(workoutId)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(if (workoutId == null) "New Workout" else "Edit Workout") },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.Default.Close, contentDescription = "Cancel")
                    }
                },
                actions = {
                    TextButton(
                        onClick = {
                            viewModel.saveWorkout(onNavigateBack)
                        },
                        enabled = viewModel.canSave() && !isSaving
                    ) {
                        if (isSaving) {
                            CircularProgressIndicator(
                                modifier = Modifier.size(20.dp),
                                strokeWidth = 2.dp
                            )
                        } else {
                            Text("SAVE", fontWeight = FontWeight.Bold)
                        }
                    }
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { viewModel.addExercise() },
                containerColor = MaterialTheme.colorScheme.primary
            ) {
                Icon(Icons.Default.Add, contentDescription = "Add Exercise")
            }
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Workout details
            item {
                Card(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Text(
                            "Workout Details",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold
                        )

                        OutlinedTextField(
                            value = workoutName,
                            onValueChange = { viewModel.updateWorkoutName(it) },
                            label = { Text("Workout Name") },
                            placeholder = { Text("e.g., Push Day, Leg Day") },
                            modifier = Modifier.fillMaxWidth(),
                            singleLine = true
                        )

                        OutlinedTextField(
                            value = workoutDescription,
                            onValueChange = { viewModel.updateWorkoutDescription(it) },
                            label = { Text("Description (optional)") },
                            placeholder = { Text("e.g., Chest, Shoulders, and Triceps") },
                            modifier = Modifier.fillMaxWidth(),
                            maxLines = 3
                        )
                    }
                }
            }

            // Exercises header
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        "Exercises",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        "${exercises.size} exercises",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }

            if (exercises.isEmpty()) {
                item {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.surfaceVariant
                        )
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(32.dp),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Icon(
                                Icons.Default.FitnessCenter,
                                contentDescription = null,
                                modifier = Modifier.size(48.dp),
                                tint = MaterialTheme.colorScheme.primary
                            )
                            Text(
                                "No exercises yet",
                                style = MaterialTheme.typography.titleMedium
                            )
                            Text(
                                "Tap the + button to add an exercise",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                }
            }

            // Exercise list
            itemsIndexed(exercises, key = { _, ex -> ex.id }) { index, exercise ->
                ExerciseEditCard(
                    exercise = exercise,
                    index = index,
                    totalExercises = exercises.size,
                    onUpdate = { updated ->
                        viewModel.updateExercise(exercise.id, updated)
                    },
                    onDelete = {
                        viewModel.removeExercise(exercise.id)
                    },
                    onMoveUp = {
                        viewModel.moveExerciseUp(index)
                    },
                    onMoveDown = {
                        viewModel.moveExerciseDown(index)
                    }
                )
            }

            // Bottom padding for FAB
            item {
                Spacer(modifier = Modifier.height(72.dp))
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ExerciseEditCard(
    exercise: Exercise,
    index: Int,
    totalExercises: Int,
    onUpdate: (Exercise) -> Unit,
    onDelete: () -> Unit,
    onMoveUp: () -> Unit,
    onMoveDown: () -> Unit
) {
    var expanded by remember { mutableStateOf(exercise.name.isBlank()) }
    var showDeleteDialog by remember { mutableStateOf(false) }

    Card(
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            // Header
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.weight(1f)
                ) {
                    Text(
                        "${index + 1}.",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary
                    )
                    Text(
                        exercise.name.ifBlank { "New Exercise" },
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Medium
                    )
                }

                Row {
                    if (totalExercises > 1) {
                        IconButton(
                            onClick = onMoveUp,
                            enabled = index > 0
                        ) {
                            Icon(Icons.Default.KeyboardArrowUp, contentDescription = "Move up")
                        }
                        IconButton(
                            onClick = onMoveDown,
                            enabled = index < totalExercises - 1
                        ) {
                            Icon(Icons.Default.KeyboardArrowDown, contentDescription = "Move down")
                        }
                    }
                    IconButton(onClick = { expanded = !expanded }) {
                        Icon(
                            if (expanded) Icons.Default.ExpandLess else Icons.Default.ExpandMore,
                            contentDescription = if (expanded) "Collapse" else "Expand"
                        )
                    }
                    IconButton(onClick = { showDeleteDialog = true }) {
                        Icon(Icons.Default.Delete, contentDescription = "Delete")
                    }
                }
            }

            if (expanded) {
                Spacer(modifier = Modifier.height(12.dp))

                OutlinedTextField(
                    value = exercise.name,
                    onValueChange = { onUpdate(exercise.copy(name = it)) },
                    label = { Text("Exercise Name") },
                    placeholder = { Text("e.g., Bench Press") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )

                Spacer(modifier = Modifier.height(12.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    OutlinedTextField(
                        value = exercise.sets.toString(),
                        onValueChange = {
                            it.toIntOrNull()?.let { sets ->
                                onUpdate(exercise.copy(sets = sets))
                            }
                        },
                        label = { Text("Sets") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        modifier = Modifier.weight(1f)
                    )

                    OutlinedTextField(
                        value = exercise.reps,
                        onValueChange = { onUpdate(exercise.copy(reps = it)) },
                        label = { Text("Reps") },
                        placeholder = { Text("10 or 8-12") },
                        modifier = Modifier.weight(1f)
                    )

                    OutlinedTextField(
                        value = exercise.restTime.toString(),
                        onValueChange = {
                            it.toIntOrNull()?.let { rest ->
                                onUpdate(exercise.copy(restTime = rest))
                            }
                        },
                        label = { Text("Rest (s)") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        modifier = Modifier.weight(1f)
                    )
                }

                Spacer(modifier = Modifier.height(12.dp))

                OutlinedTextField(
                    value = exercise.notes,
                    onValueChange = { onUpdate(exercise.copy(notes = it)) },
                    label = { Text("Notes (optional)") },
                    placeholder = { Text("Form cues, tips, etc.") },
                    modifier = Modifier.fillMaxWidth(),
                    maxLines = 2
                )
            }
        }
    }

    if (showDeleteDialog) {
        AlertDialog(
            onDismissRequest = { showDeleteDialog = false },
            title = { Text("Delete Exercise") },
            text = { Text("Are you sure you want to delete this exercise?") },
            confirmButton = {
                TextButton(
                    onClick = {
                        onDelete()
                        showDeleteDialog = false
                    }
                ) {
                    Text("Delete", color = MaterialTheme.colorScheme.error)
                }
            },
            dismissButton = {
                TextButton(onClick = { showDeleteDialog = false }) {
                    Text("Cancel")
                }
            }
        )
    }
}
