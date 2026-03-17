package com.workouttracker.app.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.workouttracker.app.data.model.AppTheme
import com.workouttracker.app.data.model.WeightUnit
import com.workouttracker.app.ui.viewmodel.SettingsViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    viewModel: SettingsViewModel,
    onNavigateBack: () -> Unit
) {
    val settings by viewModel.settings.collectAsState()
    var showWeightUnitDialog by remember { mutableStateOf(false) }
    var showThemeDialog by remember { mutableStateOf(false) }
    var showRestTimeDialog by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Settings") },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            // General Section
            item {
                SectionHeader("General")
            }

            item {
                SettingItem(
                    icon = Icons.Default.Scale,
                    title = "Weight Unit",
                    subtitle = settings.weightUnit.name,
                    onClick = { showWeightUnitDialog = true }
                )
            }

            item {
                SettingItem(
                    icon = Icons.Default.Palette,
                    title = "Theme",
                    subtitle = when (settings.theme) {
                        AppTheme.LIGHT -> "Light"
                        AppTheme.DARK -> "Dark"
                        AppTheme.SYSTEM -> "System Default"
                    },
                    onClick = { showThemeDialog = true }
                )
            }

            item {
                Spacer(modifier = Modifier.height(8.dp))
            }

            // Workout Section
            item {
                SectionHeader("Workout")
            }

            item {
                SettingItem(
                    icon = Icons.Default.Timer,
                    title = "Rest Timer",
                    subtitle = if (settings.restTimerEnabled) "Enabled" else "Disabled",
                    onClick = { },
                    trailing = {
                        Switch(
                            checked = settings.restTimerEnabled,
                            onCheckedChange = { viewModel.updateRestTimerEnabled(it) }
                        )
                    }
                )
            }

            item {
                SettingItem(
                    icon = Icons.Default.HourglassEmpty,
                    title = "Default Rest Time",
                    subtitle = "${settings.defaultRestTime} seconds",
                    onClick = { showRestTimeDialog = true },
                    enabled = settings.restTimerEnabled
                )
            }

            item {
                Spacer(modifier = Modifier.height(8.dp))
            }

            // About Section
            item {
                SectionHeader("About")
            }

            item {
                SettingItem(
                    icon = Icons.Default.Info,
                    title = "Version",
                    subtitle = "1.0.0",
                    onClick = { }
                )
            }

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
                            .padding(16.dp)
                    ) {
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                Icons.Default.FitnessCenter,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.onPrimaryContainer
                            )
                            Text(
                                "Workout Tracker",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onPrimaryContainer
                            )
                        }
                        Text(
                            "Track your workouts, monitor progress, and achieve your fitness goals.",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.8f),
                            modifier = Modifier.padding(top = 8.dp)
                        )
                    }
                }
            }
        }
    }

    // Weight Unit Dialog
    if (showWeightUnitDialog) {
        AlertDialog(
            onDismissRequest = { showWeightUnitDialog = false },
            title = { Text("Weight Unit") },
            text = {
                Column {
                    WeightUnit.values().forEach { unit ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 8.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            RadioButton(
                                selected = settings.weightUnit == unit,
                                onClick = {
                                    viewModel.updateWeightUnit(unit)
                                    showWeightUnitDialog = false
                                }
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(unit.name)
                        }
                    }
                }
            },
            confirmButton = {
                TextButton(onClick = { showWeightUnitDialog = false }) {
                    Text("Close")
                }
            }
        )
    }

    // Theme Dialog
    if (showThemeDialog) {
        AlertDialog(
            onDismissRequest = { showThemeDialog = false },
            title = { Text("Theme") },
            text = {
                Column {
                    listOf(
                        AppTheme.LIGHT to "Light",
                        AppTheme.DARK to "Dark",
                        AppTheme.SYSTEM to "System Default"
                    ).forEach { (theme, label) ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 8.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            RadioButton(
                                selected = settings.theme == theme,
                                onClick = {
                                    viewModel.updateTheme(theme)
                                    showThemeDialog = false
                                }
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(label)
                        }
                    }
                }
            },
            confirmButton = {
                TextButton(onClick = { showThemeDialog = false }) {
                    Text("Close")
                }
            }
        )
    }

    // Rest Time Dialog
    if (showRestTimeDialog) {
        var tempRestTime by remember { mutableStateOf(settings.defaultRestTime.toString()) }

        AlertDialog(
            onDismissRequest = { showRestTimeDialog = false },
            title = { Text("Default Rest Time") },
            text = {
                Column {
                    Text(
                        "Enter default rest time in seconds",
                        style = MaterialTheme.typography.bodyMedium,
                        modifier = Modifier.padding(bottom = 16.dp)
                    )
                    OutlinedTextField(
                        value = tempRestTime,
                        onValueChange = { tempRestTime = it },
                        label = { Text("Seconds") },
                        singleLine = true
                    )
                }
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        tempRestTime.toIntOrNull()?.let { seconds ->
                            if (seconds > 0) {
                                viewModel.updateDefaultRestTime(seconds)
                                showRestTimeDialog = false
                            }
                        }
                    }
                ) {
                    Text("Save")
                }
            },
            dismissButton = {
                TextButton(onClick = { showRestTimeDialog = false }) {
                    Text("Cancel")
                }
            }
        )
    }
}

@Composable
fun SectionHeader(text: String) {
    Text(
        text = text,
        style = MaterialTheme.typography.titleSmall,
        fontWeight = FontWeight.Bold,
        color = MaterialTheme.colorScheme.primary,
        modifier = Modifier.padding(top = 8.dp, bottom = 4.dp)
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingItem(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    title: String,
    subtitle: String,
    onClick: () -> Unit,
    enabled: Boolean = true,
    trailing: @Composable (() -> Unit)? = null
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        onClick = onClick,
        enabled = enabled
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
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.weight(1f)
            ) {
                Icon(
                    icon,
                    contentDescription = null,
                    tint = if (enabled) MaterialTheme.colorScheme.primary 
                           else MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.4f)
                )
                Column {
                    Text(
                        text = title,
                        style = MaterialTheme.typography.bodyLarge,
                        fontWeight = FontWeight.Medium,
                        color = if (enabled) MaterialTheme.colorScheme.onSurface
                                else MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.4f)
                    )
                    Text(
                        text = subtitle,
                        style = MaterialTheme.typography.bodySmall,
                        color = if (enabled) MaterialTheme.colorScheme.onSurfaceVariant
                                else MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.4f)
                    )
                }
            }

            if (trailing != null) {
                trailing()
            } else if (enabled) {
                Icon(
                    Icons.Default.ChevronRight,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}
