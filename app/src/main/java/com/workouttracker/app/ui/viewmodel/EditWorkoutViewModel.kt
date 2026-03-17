package com.workouttracker.app.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.workouttracker.app.data.model.Exercise
import com.workouttracker.app.data.model.ExerciseCategory
import com.workouttracker.app.data.model.Workout
import com.workouttracker.app.data.repository.WorkoutRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.util.UUID

class EditWorkoutViewModel(private val repository: WorkoutRepository) : ViewModel() {
    private val _workoutId = MutableStateFlow<String?>(null)
    
    private val _workoutName = MutableStateFlow("")
    val workoutName: StateFlow<String> = _workoutName.asStateFlow()

    private val _workoutDescription = MutableStateFlow("")
    val workoutDescription: StateFlow<String> = _workoutDescription.asStateFlow()

    private val _exercises = MutableStateFlow<List<Exercise>>(emptyList())
    val exercises: StateFlow<List<Exercise>> = _exercises.asStateFlow()

    private val _isSaving = MutableStateFlow(false)
    val isSaving: StateFlow<Boolean> = _isSaving.asStateFlow()

    fun loadWorkout(workoutId: String?) {
        if (workoutId == null) {
            // New workout
            _workoutId.value = UUID.randomUUID().toString()
            _workoutName.value = ""
            _workoutDescription.value = ""
            _exercises.value = emptyList()
        } else {
            // Edit existing workout
            _workoutId.value = workoutId
            viewModelScope.launch {
                val workout = repository.getWorkouts().find { it.id == workoutId }
                workout?.let {
                    _workoutName.value = it.name
                    _workoutDescription.value = it.description
                    _exercises.value = it.exercises
                }
            }
        }
    }

    fun updateWorkoutName(name: String) {
        _workoutName.value = name
    }

    fun updateWorkoutDescription(description: String) {
        _workoutDescription.value = description
    }

    fun addExercise() {
        val newExercise = Exercise(
            id = UUID.randomUUID().toString(),
            name = "",
            sets = 3,
            reps = "10",
            category = ExerciseCategory.OTHER
        )
        _exercises.value = _exercises.value + newExercise
    }

    fun updateExercise(exerciseId: String, updatedExercise: Exercise) {
        _exercises.value = _exercises.value.map { exercise ->
            if (exercise.id == exerciseId) updatedExercise else exercise
        }
    }

    fun removeExercise(exerciseId: String) {
        _exercises.value = _exercises.value.filterNot { it.id == exerciseId }
    }

    fun moveExerciseUp(index: Int) {
        if (index > 0) {
            val list = _exercises.value.toMutableList()
            val temp = list[index]
            list[index] = list[index - 1]
            list[index - 1] = temp
            _exercises.value = list
        }
    }

    fun moveExerciseDown(index: Int) {
        if (index < _exercises.value.size - 1) {
            val list = _exercises.value.toMutableList()
            val temp = list[index]
            list[index] = list[index + 1]
            list[index + 1] = temp
            _exercises.value = list
        }
    }

    fun saveWorkout(onComplete: () -> Unit) {
        viewModelScope.launch {
            _isSaving.value = true
            
            val workout = Workout(
                id = _workoutId.value ?: UUID.randomUUID().toString(),
                name = _workoutName.value.trim(),
                description = _workoutDescription.value.trim(),
                exercises = _exercises.value.filter { it.name.isNotBlank() }
            )

            repository.saveWorkout(workout)
            _isSaving.value = false
            onComplete()
        }
    }

    fun canSave(): Boolean {
        // Allow saving if workout has a name
        // Empty exercises will be filtered out during save
        return _workoutName.value.trim().isNotBlank()
    }
}
