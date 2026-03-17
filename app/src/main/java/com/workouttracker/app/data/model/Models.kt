package com.workouttracker.app.data.model

import kotlinx.serialization.Serializable

@Serializable
data class Workout(
    val id: String,
    val name: String,
    val description: String = "",
    val exercises: List<Exercise> = emptyList(),
    val createdAt: Long = System.currentTimeMillis(),
    val lastPerformed: Long? = null
)

@Serializable
data class Exercise(
    val id: String,
    val name: String,
    val sets: Int = 3,
    val reps: String = "10", // Can be "10", "8-12", "AMRAP", etc.
    val weight: String = "", // e.g., "135 lbs", "bodyweight"
    val restTime: Int = 60, // seconds
    val notes: String = "",
    val category: ExerciseCategory = ExerciseCategory.OTHER
)

@Serializable
enum class ExerciseCategory {
    CHEST, BACK, LEGS, SHOULDERS, ARMS, CORE, CARDIO, OTHER
}

@Serializable
data class WorkoutSession(
    val id: String,
    val workoutId: String,
    val workoutName: String,
    val startTime: Long,
    val endTime: Long? = null,
    val completedExercises: List<CompletedExercise> = emptyList(),
    val notes: String = ""
)

@Serializable
data class CompletedExercise(
    val exerciseId: String,
    val exerciseName: String,
    val sets: List<CompletedSet> = emptyList()
)

@Serializable
data class CompletedSet(
    val setNumber: Int,
    val reps: Int,
    val weight: Double,
    val completed: Boolean = true,
    val notes: String = ""
)

@Serializable
data class UserSettings(
    val weightUnit: WeightUnit = WeightUnit.LBS,
    val theme: AppTheme = AppTheme.SYSTEM,
    val restTimerEnabled: Boolean = true,
    val defaultRestTime: Int = 60
)

@Serializable
enum class WeightUnit {
    LBS, KG
}

@Serializable
enum class AppTheme {
    LIGHT, DARK, SYSTEM
}
