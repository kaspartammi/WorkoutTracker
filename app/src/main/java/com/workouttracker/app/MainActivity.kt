package com.workouttracker.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.lifecycle.lifecycleScope
import androidx.navigation.compose.rememberNavController
import com.workouttracker.app.data.model.AppTheme
import com.workouttracker.app.data.repository.WorkoutRepository
import com.workouttracker.app.ui.navigation.WorkoutNavigation
import com.workouttracker.app.ui.theme.WorkoutTrackerTheme
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    private lateinit var repository: WorkoutRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        
        repository = WorkoutRepository(applicationContext)
        
        // Initialize repository with sample data
        lifecycleScope.launch {
            repository.initialize()
        }

        setContent {
            val settings by repository.getSettingsFlow().collectAsState(initial = null)
            
            val darkTheme = when (settings?.theme) {
                AppTheme.LIGHT -> false
                AppTheme.DARK -> true
                AppTheme.SYSTEM, null -> isSystemInDarkTheme()
            }

            WorkoutTrackerTheme(darkTheme = darkTheme) {
                val navController = rememberNavController()
                WorkoutNavigation(
                    navController = navController,
                    repository = repository
                )
            }
        }
    }
}
