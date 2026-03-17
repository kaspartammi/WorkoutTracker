package com.workouttracker.app.ui.navigation

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.workouttracker.app.data.repository.WorkoutRepository
import com.workouttracker.app.ui.screens.*
import com.workouttracker.app.ui.viewmodel.*

sealed class Screen(val route: String) {
    object Home : Screen("home")
    object SelectWorkout : Screen("select_workout")
    object NewWorkout : Screen("new_workout")
    object EditWorkout : Screen("edit_workout/{workoutId}") {
        fun createRoute(workoutId: String) = "edit_workout/$workoutId"
    }
    object WorkoutSession : Screen("workout_session/{workoutId}") {
        fun createRoute(workoutId: String) = "workout_session/$workoutId"
    }
    object Settings : Screen("settings")
}

@Composable
fun WorkoutNavigation(
    navController: NavHostController,
    repository: WorkoutRepository
) {
    // Create factory once for all ViewModels
    val factory = WorkoutViewModelFactory(repository)
    
    NavHost(
        navController = navController,
        startDestination = Screen.Home.route
    ) {
        composable(Screen.Home.route) {
            val homeViewModel: HomeViewModel = viewModel(factory = factory)
            HomeScreen(
                viewModel = homeViewModel,
                onNavigateToSelectWorkout = {
                    navController.navigate(Screen.SelectWorkout.route)
                },
                onNavigateToNewWorkout = {
                    navController.navigate(Screen.NewWorkout.route)
                },
                onNavigateToEditWorkout = { workoutId ->
                    navController.navigate(Screen.EditWorkout.createRoute(workoutId))
                },
                onNavigateToSettings = {
                    navController.navigate(Screen.Settings.route)
                }
            )
        }

        composable(Screen.SelectWorkout.route) {
            val homeViewModel: HomeViewModel = viewModel(factory = factory)
            SelectWorkoutScreen(
                viewModel = homeViewModel,
                onNavigateBack = { navController.popBackStack() },
                onStartWorkout = { workoutId ->
                    navController.navigate(Screen.WorkoutSession.createRoute(workoutId))
                }
            )
        }

        composable(Screen.NewWorkout.route) {
            val editViewModel: EditWorkoutViewModel = viewModel(factory = factory)
            EditWorkoutScreen(
                viewModel = editViewModel,
                workoutId = null,
                onNavigateBack = { navController.popBackStack() }
            )
        }

        composable(
            route = Screen.EditWorkout.route,
            arguments = listOf(navArgument("workoutId") { type = NavType.StringType })
        ) { backStackEntry ->
            val workoutId = backStackEntry.arguments?.getString("workoutId")
            val editViewModel: EditWorkoutViewModel = viewModel(factory = factory)
            EditWorkoutScreen(
                viewModel = editViewModel,
                workoutId = workoutId,
                onNavigateBack = { navController.popBackStack() }
            )
        }

        composable(
            route = Screen.WorkoutSession.route,
            arguments = listOf(navArgument("workoutId") { type = NavType.StringType })
        ) { backStackEntry ->
            val workoutId = backStackEntry.arguments?.getString("workoutId") ?: return@composable
            val sessionViewModel: WorkoutSessionViewModel = viewModel(factory = factory)
            WorkoutSessionScreen(
                viewModel = sessionViewModel,
                repository = repository,
                workoutId = workoutId,
                onNavigateBack = {
                    navController.popBackStack(Screen.Home.route, inclusive = false)
                }
            )
        }

        composable(Screen.Settings.route) {
            val settingsViewModel: SettingsViewModel = viewModel(factory = factory)
            SettingsScreen(
                viewModel = settingsViewModel,
                onNavigateBack = { navController.popBackStack() }
            )
        }
    }
}
