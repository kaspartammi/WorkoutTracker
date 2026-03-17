package com.workouttracker.app.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.workouttracker.app.data.model.*
import com.workouttracker.app.data.repository.WorkoutRepository
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.util.UUID

class WorkoutSessionViewModel(
    private val repository: WorkoutRepository
) : ViewModel() {
    private val _workout = MutableStateFlow<Workout?>(null)
    val workout: StateFlow<Workout?> = _workout.asStateFlow()

    private val _currentExerciseIndex = MutableStateFlow(0)
    val currentExerciseIndex: StateFlow<Int> = _currentExerciseIndex.asStateFlow()

    private val _completedSets = MutableStateFlow<Map<String, List<CompletedSet>>>(emptyMap())
    val completedSets: StateFlow<Map<String, List<CompletedSet>>> = _completedSets.asStateFlow()

    private val _session = MutableStateFlow<WorkoutSession?>(null)
    val session: StateFlow<WorkoutSession?> = _session.asStateFlow()

    private val _restTimeRemaining = MutableStateFlow(0)
    val restTimeRemaining: StateFlow<Int> = _restTimeRemaining.asStateFlow()

    private val _isResting = MutableStateFlow(false)
    val isResting: StateFlow<Boolean> = _isResting.asStateFlow()

    private var restTimerJob: Job? = null

    fun startWorkout(workout: Workout) {
        _workout.value = workout
        _currentExerciseIndex.value = 0
        _completedSets.value = emptyMap()
        
        _session.value = WorkoutSession(
            id = UUID.randomUUID().toString(),
            workoutId = workout.id,
            workoutName = workout.name,
            startTime = System.currentTimeMillis()
        )
    }

    fun completeSet(exerciseId: String, setNumber: Int, reps: Int, weight: Double) {
        val currentSets = _completedSets.value[exerciseId]?.toMutableList() ?: mutableListOf()
        
        val newSet = CompletedSet(
            setNumber = setNumber,
            reps = reps,
            weight = weight,
            completed = true
        )
        
        currentSets.add(newSet)
        _completedSets.value = _completedSets.value.toMutableMap().apply {
            this[exerciseId] = currentSets
        }

        // Auto-start rest timer
        _workout.value?.exercises?.get(_currentExerciseIndex.value)?.let { exercise ->
            if (exercise.id == exerciseId) {
                startRestTimer(exercise.restTime)
            }
        }
    }

    fun skipSet(exerciseId: String, setNumber: Int) {
        val currentSets = _completedSets.value[exerciseId]?.toMutableList() ?: mutableListOf()
        
        val skippedSet = CompletedSet(
            setNumber = setNumber,
            reps = 0,
            weight = 0.0,
            completed = false
        )
        
        currentSets.add(skippedSet)
        _completedSets.value = _completedSets.value.toMutableMap().apply {
            this[exerciseId] = currentSets
        }
    }

    fun nextExercise() {
        val workout = _workout.value ?: return
        if (_currentExerciseIndex.value < workout.exercises.size - 1) {
            _currentExerciseIndex.value++
            stopRestTimer()
        }
    }

    fun previousExercise() {
        if (_currentExerciseIndex.value > 0) {
            _currentExerciseIndex.value--
            stopRestTimer()
        }
    }

    fun startRestTimer(seconds: Int) {
        restTimerJob?.cancel()
        _restTimeRemaining.value = seconds
        _isResting.value = true

        restTimerJob = viewModelScope.launch {
            while (_restTimeRemaining.value > 0) {
                delay(1000)
                _restTimeRemaining.value--
            }
            _isResting.value = false
        }
    }

    fun stopRestTimer() {
        restTimerJob?.cancel()
        _isResting.value = false
        _restTimeRemaining.value = 0
    }

    fun skipRest() {
        stopRestTimer()
    }

    fun finishWorkout() {
        viewModelScope.launch {
            val workout = _workout.value ?: return@launch
            val session = _session.value ?: return@launch

            // Create completed exercises list
            val completedExercises = workout.exercises.map { exercise ->
                CompletedExercise(
                    exerciseId = exercise.id,
                    exerciseName = exercise.name,
                    sets = _completedSets.value[exercise.id] ?: emptyList()
                )
            }

            // Save session
            val finishedSession = session.copy(
                endTime = System.currentTimeMillis(),
                completedExercises = completedExercises
            )
            repository.saveSession(finishedSession)

            // Update workout's last performed time
            val updatedWorkout = workout.copy(lastPerformed = System.currentTimeMillis())
            repository.saveWorkout(updatedWorkout)

            stopRestTimer()
        }
    }

    override fun onCleared() {
        super.onCleared()
        restTimerJob?.cancel()
    }
}
