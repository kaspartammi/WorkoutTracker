package com.workouttracker.app.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.workouttracker.app.data.model.AppTheme
import com.workouttracker.app.data.model.UserSettings
import com.workouttracker.app.data.model.WeightUnit
import com.workouttracker.app.data.repository.WorkoutRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class SettingsViewModel(private val repository: WorkoutRepository) : ViewModel() {
    private val _settings = MutableStateFlow(UserSettings())
    val settings: StateFlow<UserSettings> = _settings.asStateFlow()

    init {
        loadSettings()
    }

    private fun loadSettings() {
        viewModelScope.launch {
            repository.getSettingsFlow().collect { settings ->
                _settings.value = settings
            }
        }
    }

    fun updateWeightUnit(unit: WeightUnit) {
        viewModelScope.launch {
            val updated = _settings.value.copy(weightUnit = unit)
            repository.saveSettings(updated)
        }
    }

    fun updateTheme(theme: AppTheme) {
        viewModelScope.launch {
            val updated = _settings.value.copy(theme = theme)
            repository.saveSettings(updated)
        }
    }

    fun updateRestTimerEnabled(enabled: Boolean) {
        viewModelScope.launch {
            val updated = _settings.value.copy(restTimerEnabled = enabled)
            repository.saveSettings(updated)
        }
    }

    fun updateDefaultRestTime(seconds: Int) {
        viewModelScope.launch {
            val updated = _settings.value.copy(defaultRestTime = seconds)
            repository.saveSettings(updated)
        }
    }
}
