package com.workouttracker.app.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.workouttracker.app.data.model.Exercise
import com.workouttracker.app.data.model.Workout
import com.workouttracker.app.data.repository.WorkoutRepository
import com.workouttracker.app.ui.viewmodel.WorkoutSessionViewModel
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WorkoutSessionScreen(
    viewModel: WorkoutSessionViewModel,
    repository: WorkoutRepository,
    workoutId: String,
    onNavigateBack: () -> Unit
) {
    val workout by viewModel.workout.collectAsState()
    val currentExerciseIndex by viewModel.currentExerciseIndex.collectAsState()
    val completedSets by viewModel.completedSets.collectAsState()
    val restTimeRemaining by viewModel.restTimeRemaining.collectAsState()
    val isResting by viewModel.isResting.collectAsState()
    var showFinishDialog by remember { mutableStateOf(false) }
    val scope = rememberCoroutineScope()

    LaunchedEffect(workoutId) {
        val loadedWorkout = repository.getWorkouts().find { it.id == workoutId }
        loadedWorkout?.let { viewModel.startWorkout(it) }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(workout?.name ?: "Workout") },
                navigationIcon = {
                    IconButton(onClick = { showFinishDialog = true }) {
                        Icon(Icons.Default.Close, contentDescription = "Finish")
                    }
                },
                actions = {
                    TextButton(
                        onClick = { showFinishDialog = true },
                        colors = ButtonDefaults.textButtonColors(
                            contentColor = MaterialTheme.colorScheme.primary
                        )
                    ) {
                        Text("FINISH", fontWeight = FontWeight.Bold)
                    }
                }
            )
        }
    ) { padding ->
        // Show loading or error state if workout not loaded
        if (workout == null) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    CircularProgressIndicator()
                    Text("Loading workout...")
                }
            }
        } else {
            val currentWorkout = workout!!
            
            // Check if workout has exercises
            if (currentWorkout.exercises.isEmpty()) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(padding),
                    contentAlignment = Alignment.Center
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        Icon(
                            Icons.Default.FitnessCenter,
                            contentDescription = null,
                            modifier = Modifier.size(64.dp),
                            tint = MaterialTheme.colorScheme.primary
                        )
                        Text(
                            "This workout has no exercises",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            "Add exercises to this workout before starting a session",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            textAlign = TextAlign.Center
                        )
                    }
                }
            } else {
                val currentExercise = currentWorkout.exercises.getOrNull(currentExerciseIndex)

                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(padding)
                ) {
                // Progress indicator
                LinearProgressIndicator(
                    progress = (currentExerciseIndex + 1) / currentWorkout.exercises.size.toFloat(),
                    modifier = Modifier.fillMaxWidth()
                )

                // Rest timer banner
                if (isResting) {
                    RestTimerBanner(
                        timeRemaining = restTimeRemaining,
                        onSkip = { viewModel.skipRest() }
                    )
                }

                // Exercise navigation
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(
                        onClick = { viewModel.previousExercise() },
                        enabled = currentExerciseIndex > 0
                    ) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Previous")
                    }

                    Text(
                        "Exercise ${currentExerciseIndex + 1} of ${currentWorkout.exercises.size}",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )

                    IconButton(
                        onClick = { viewModel.nextExercise() },
                        enabled = currentExerciseIndex < currentWorkout.exercises.size - 1
                    ) {
                        Icon(Icons.Default.ArrowForward, contentDescription = "Next")
                    }
                }

                // Current exercise details
                currentExercise?.let { exercise ->
                    ExerciseSessionCard(
                        exercise = exercise,
                        completedSetsForExercise = completedSets[exercise.id] ?: emptyList(),
                        onCompleteSet = { setNum, reps, weight ->
                            viewModel.completeSet(exercise.id, setNum, reps, weight)
                        },
                        onSkipSet = { setNum ->
                            viewModel.skipSet(exercise.id, setNum)
                        }
                    )
                }
            }
        }
        }
    }

    // Finish workout dialog
    if (showFinishDialog) {
        AlertDialog(
            onDismissRequest = { showFinishDialog = false },
            title = { Text("Finish Workout?") },
            text = { Text("Are you sure you want to finish this workout session?") },
            confirmButton = {
                TextButton(
                    onClick = {
                        scope.launch {
                            viewModel.finishWorkout()
                            onNavigateBack()
                        }
                    }
                ) {
                    Text("Finish")
                }
            },
            dismissButton = {
                TextButton(onClick = { showFinishDialog = false }) {
                    Text("Continue")
                }
            }
        )
    }
}

@Composable
fun RestTimerBanner(
    timeRemaining: Int,
    onSkip: () -> Unit
) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        color = MaterialTheme.colorScheme.primaryContainer
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    Icons.Default.Timer,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onPrimaryContainer
                )
                Column {
                    Text(
                        "Rest Time",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onPrimaryContainer
                    )
                    Text(
                        formatTime(timeRemaining),
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onPrimaryContainer
                    )
                }
            }

            TextButton(onClick = onSkip) {
                Text("Skip Rest")
            }
        }
    }
}

@Composable
fun ExerciseSessionCard(
    exercise: Exercise,
    completedSetsForExercise: List<com.workouttracker.app.data.model.CompletedSet>,
    onCompleteSet: (Int, Int, Double) -> Unit,
    onSkipSet: (Int) -> Unit
) {
    var currentSetNum by remember(exercise.id) {
        mutableStateOf(completedSetsForExercise.size + 1)
    }
    var repsInput by remember { mutableStateOf(exercise.reps) }
    var weightInput by remember { mutableStateOf(exercise.weight) }

    LaunchedEffect(completedSetsForExercise.size) {
        currentSetNum = completedSetsForExercise.size + 1
    }

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
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
                        .padding(20.dp)
                ) {
                    Text(
                        text = exercise.name,
                        style = MaterialTheme.typography.headlineMedium,
                        fontWeight = FontWeight.Bold
                    )

                    if (exercise.notes.isNotBlank()) {
                        Text(
                            text = exercise.notes,
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            modifier = Modifier.padding(top = 8.dp)
                        )
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    Row(
                        horizontalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        InfoChip(label = "Sets", value = exercise.sets.toString())
                        InfoChip(label = "Reps", value = exercise.reps)
                        InfoChip(label = "Rest", value = "${exercise.restTime}s")
                    }
                }
            }
        }

        // Current set input
        if (currentSetNum <= exercise.sets) {
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.primaryContainer
                    )
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(20.dp)
                    ) {
                        Text(
                            "Set $currentSetNum of ${exercise.sets}",
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onPrimaryContainer
                        )

                        Spacer(modifier = Modifier.height(16.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            OutlinedTextField(
                                value = repsInput,
                                onValueChange = { repsInput = it },
                                label = { Text("Reps") },
                                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                                modifier = Modifier.weight(1f)
                            )

                            OutlinedTextField(
                                value = weightInput,
                                onValueChange = { weightInput = it },
                                label = { Text("Weight") },
                                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                                modifier = Modifier.weight(1f)
                            )
                        }

                        Spacer(modifier = Modifier.height(16.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            OutlinedButton(
                                onClick = { onSkipSet(currentSetNum) },
                                modifier = Modifier.weight(1f)
                            ) {
                                Text("Skip Set")
                            }

                            Button(
                                onClick = {
                                    val reps = repsInput.toIntOrNull() ?: 0
                                    val weight = weightInput.toDoubleOrNull() ?: 0.0
                                    onCompleteSet(currentSetNum, reps, weight)
                                },
                                modifier = Modifier.weight(1f)
                            ) {
                                Icon(Icons.Default.Check, contentDescription = null)
                                Spacer(modifier = Modifier.width(8.dp))
                                Text("Complete")
                            }
                        }
                    }
                }
            }
        }

        // Completed sets history
        if (completedSetsForExercise.isNotEmpty()) {
            item {
                Text(
                    "Completed Sets",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(vertical = 8.dp)
                )
            }

            items(completedSetsForExercise.reversed()) { set ->
                Card(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            "Set ${set.setNumber}",
                            style = MaterialTheme.typography.titleSmall,
                            fontWeight = FontWeight.Medium
                        )

                        if (set.completed) {
                            Row(
                                horizontalArrangement = Arrangement.spacedBy(16.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    "${set.reps} reps",
                                    style = MaterialTheme.typography.bodyMedium
                                )
                                Text(
                                    "${set.weight} lbs",
                                    style = MaterialTheme.typography.bodyMedium
                                )
                                Icon(
                                    Icons.Default.CheckCircle,
                                    contentDescription = "Completed",
                                    tint = MaterialTheme.colorScheme.primary,
                                    modifier = Modifier.size(20.dp)
                                )
                            }
                        } else {
                            Text(
                                "Skipped",
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun InfoChip(label: String, value: String) {
    Surface(
        color = MaterialTheme.colorScheme.surface,
        shape = MaterialTheme.shapes.small
    ) {
        Column(
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = label,
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Text(
                text = value,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

fun formatTime(seconds: Int): String {
    val mins = seconds / 60
    val secs = seconds % 60
    return "%d:%02d".format(mins, secs)
}
