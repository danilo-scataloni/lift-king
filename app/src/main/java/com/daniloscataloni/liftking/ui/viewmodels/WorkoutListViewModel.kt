package com.daniloscataloni.liftking.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.daniloscataloni.liftking.entities.Workout
import com.daniloscataloni.liftking.repositories.IPeriodizationRepository
import com.daniloscataloni.liftking.repositories.IWorkoutRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class WorkoutListViewModel(
    private val workoutRepository: IWorkoutRepository,
    private val periodizationRepository: IPeriodizationRepository
) : ViewModel() {

    private val _periodizationId = MutableStateFlow<Long?>(null)

    @OptIn(ExperimentalCoroutinesApi::class)
    val workouts = _periodizationId.flatMapLatest { id ->
        if (id != null) {
            workoutRepository.getWorkoutsByPeriodization(id)
        } else {
            flowOf(emptyList())
        }
    }.stateIn(viewModelScope, SharingStarted.Lazily, emptyList())

    val periodizationName = periodizationRepository.getActivePeriodization()
        .stateIn(viewModelScope, SharingStarted.Lazily, null)

    private val _showCreateDialog = MutableStateFlow(false)
    val showCreateDialog: StateFlow<Boolean> = _showCreateDialog.asStateFlow()

    private val _newWorkoutName = MutableStateFlow("")
    val newWorkoutName: StateFlow<String> = _newWorkoutName.asStateFlow()

    fun setPeriodizationId(id: Long) {
        _periodizationId.value = id
    }

    fun onShowCreateDialog() {
        _showCreateDialog.value = true
    }

    fun onDismissCreateDialog() {
        _showCreateDialog.value = false
        _newWorkoutName.value = ""
    }

    fun onNameChange(name: String) {
        _newWorkoutName.value = name
    }

    fun createWorkout() {
        val periodizationId = _periodizationId.value ?: return
        val name = _newWorkoutName.value.trim()
        if (name.isNotEmpty()) {
            viewModelScope.launch {
                val currentCount = workouts.value.size
                workoutRepository.insertWorkout(
                    Workout(
                        periodizationId = periodizationId,
                        name = name,
                        order = currentCount
                    )
                )
                onDismissCreateDialog()
            }
        }
    }

    fun deleteWorkout(workout: Workout) {
        viewModelScope.launch {
            workoutRepository.deleteWorkout(workout)
        }
    }
}
