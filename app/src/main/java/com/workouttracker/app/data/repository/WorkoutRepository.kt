package com.workouttracker.app.data.repository

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.workouttracker.app.data.model.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import java.util.UUID

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "workout_tracker")

class WorkoutRepository(private val context: Context) {
    private val json = Json { 
        ignoreUnknownKeys = true
        encodeDefaults = true
    }

    private object PreferenceKeys {
        val WORKOUTS = stringPreferencesKey("workouts")
        val SESSIONS = stringPreferencesKey("sessions")
        val SETTINGS = stringPreferencesKey("settings")
    }
    
    // Initialize repository with sample data if needed
    suspend fun initialize() {
        val data = context.dataStore.data.first()
        if (data[PreferenceKeys.WORKOUTS] == null) {
            val samples = getSampleWorkouts()
            saveWorkouts(samples)
        }
    }

    // Workouts
    suspend fun getWorkouts(): List<Workout> {
        val data = context.dataStore.data.first()
        val workoutsJson = data[PreferenceKeys.WORKOUTS]
        
        // If no data exists, initialize with sample workouts and save them
        if (workoutsJson == null) {
            val samples = getSampleWorkouts()
            saveWorkouts(samples)
            return samples
        }
        
        return try {
            json.decodeFromString(workoutsJson)
        } catch (e: Exception) {
            e.printStackTrace()
            // If parsing fails, return sample workouts but don't overwrite bad data
            getSampleWorkouts()
        }
    }

    fun getWorkoutsFlow(): Flow<List<Workout>> {
        return context.dataStore.data.map { preferences ->
            val workoutsJson = preferences[PreferenceKeys.WORKOUTS]
            
            // Note: We can't suspend inside map, so we return samples without saving
            // The first getWorkouts() call will handle initialization
            if (workoutsJson == null) {
                return@map getSampleWorkouts()
            }
            
            try {
                json.decodeFromString(workoutsJson)
            } catch (e: Exception) {
                e.printStackTrace()
                getSampleWorkouts()
            }
        }
    }

    suspend fun saveWorkout(workout: Workout) {
        val workouts = getWorkouts().toMutableList()
        val index = workouts.indexOfFirst { it.id == workout.id }
        if (index >= 0) {
            workouts[index] = workout
        } else {
            workouts.add(workout)
        }
        saveWorkouts(workouts)
    }

    suspend fun deleteWorkout(workoutId: String) {
        val workouts = getWorkouts().filterNot { it.id == workoutId }
        saveWorkouts(workouts)
    }

    private suspend fun saveWorkouts(workouts: List<Workout>) {
        context.dataStore.edit { preferences ->
            preferences[PreferenceKeys.WORKOUTS] = json.encodeToString(workouts)
        }
    }

    // Workout Sessions
    suspend fun getSessions(): List<WorkoutSession> {
        val data = context.dataStore.data.first()
        val sessionsJson = data[PreferenceKeys.SESSIONS] ?: return emptyList()
        return try {
            json.decodeFromString(sessionsJson)
        } catch (e: Exception) {
            emptyList()
        }
    }

    fun getSessionsFlow(): Flow<List<WorkoutSession>> {
        return context.dataStore.data.map { preferences ->
            val sessionsJson = preferences[PreferenceKeys.SESSIONS] ?: return@map emptyList()
            try {
                json.decodeFromString(sessionsJson)
            } catch (e: Exception) {
                emptyList()
            }
        }
    }

    suspend fun saveSession(session: WorkoutSession) {
        val sessions = getSessions().toMutableList()
        val index = sessions.indexOfFirst { it.id == session.id }
        if (index >= 0) {
            sessions[index] = session
        } else {
            sessions.add(session)
        }
        context.dataStore.edit { preferences ->
            preferences[PreferenceKeys.SESSIONS] = json.encodeToString(sessions)
        }
    }

    // User Settings
    suspend fun getSettings(): UserSettings {
        val data = context.dataStore.data.first()
        val settingsJson = data[PreferenceKeys.SETTINGS] ?: return UserSettings()
        return try {
            json.decodeFromString(settingsJson)
        } catch (e: Exception) {
            UserSettings()
        }
    }

    fun getSettingsFlow(): Flow<UserSettings> {
        return context.dataStore.data.map { preferences ->
            val settingsJson = preferences[PreferenceKeys.SETTINGS] ?: return@map UserSettings()
            try {
                json.decodeFromString(settingsJson)
            } catch (e: Exception) {
                UserSettings()
            }
        }
    }

    suspend fun saveSettings(settings: UserSettings) {
        context.dataStore.edit { preferences ->
            preferences[PreferenceKeys.SETTINGS] = json.encodeToString(settings)
        }
    }

    // Sample data for first launch
    private fun getSampleWorkouts(): List<Workout> {
        return listOf(
            Workout(
                id = UUID.randomUUID().toString(),
                name = "Push Day",
                description = "Chest, Shoulders, and Triceps",
                exercises = listOf(
                    Exercise(
                        id = UUID.randomUUID().toString(),
                        name = "Bench Press",
                        sets = 4,
                        reps = "8-10",
                        category = ExerciseCategory.CHEST,
                        restTime = 90
                    ),
                    Exercise(
                        id = UUID.randomUUID().toString(),
                        name = "Overhead Press",
                        sets = 3,
                        reps = "8-10",
                        category = ExerciseCategory.SHOULDERS,
                        restTime = 90
                    ),
                    Exercise(
                        id = UUID.randomUUID().toString(),
                        name = "Tricep Dips",
                        sets = 3,
                        reps = "10-12",
                        category = ExerciseCategory.ARMS,
                        restTime = 60
                    )
                )
            ),
            Workout(
                id = UUID.randomUUID().toString(),
                name = "Pull Day",
                description = "Back and Biceps",
                exercises = listOf(
                    Exercise(
                        id = UUID.randomUUID().toString(),
                        name = "Pull-ups",
                        sets = 4,
                        reps = "8-10",
                        category = ExerciseCategory.BACK,
                        restTime = 90
                    ),
                    Exercise(
                        id = UUID.randomUUID().toString(),
                        name = "Barbell Rows",
                        sets = 4,
                        reps = "8-10",
                        category = ExerciseCategory.BACK,
                        restTime = 90
                    ),
                    Exercise(
                        id = UUID.randomUUID().toString(),
                        name = "Bicep Curls",
                        sets = 3,
                        reps = "10-12",
                        category = ExerciseCategory.ARMS,
                        restTime = 60
                    )
                )
            ),
            Workout(
                id = UUID.randomUUID().toString(),
                name = "Leg Day",
                description = "Quads, Hamstrings, and Glutes",
                exercises = listOf(
                    Exercise(
                        id = UUID.randomUUID().toString(),
                        name = "Squats",
                        sets = 4,
                        reps = "8-10",
                        category = ExerciseCategory.LEGS,
                        restTime = 120
                    ),
                    Exercise(
                        id = UUID.randomUUID().toString(),
                        name = "Romanian Deadlifts",
                        sets = 3,
                        reps = "10-12",
                        category = ExerciseCategory.LEGS,
                        restTime = 90
                    ),
                    Exercise(
                        id = UUID.randomUUID().toString(),
                        name = "Leg Press",
                        sets = 3,
                        reps = "12-15",
                        category = ExerciseCategory.LEGS,
                        restTime = 90
                    )
                )
            )
        )
    }
}
