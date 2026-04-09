package com.daniloscataloni.liftking.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.daniloscataloni.liftking.domain.models.Workout
import com.daniloscataloni.liftking.data.repositories.IPeriodizationRepository
import com.daniloscataloni.liftking.data.repositories.IWorkoutRepository
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

    private val _showEditDialog = MutableStateFlow(false)
    val showEditDialog: StateFlow<Boolean> = _showEditDialog.asStateFlow()

    private val _workoutToEdit = MutableStateFlow<Workout?>(null)
    val workoutToEdit: StateFlow<Workout?> = _workoutToEdit.asStateFlow()

    private val _editWorkoutName = MutableStateFlow("")
    val editWorkoutName: StateFlow<String> = _editWorkoutName.asStateFlow()

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

    fun onShowEditDialog(workout: Workout) {
        _workoutToEdit.value = workout
        _editWorkoutName.value = workout.name
        _showEditDialog.value = true
    }

    fun onDismissEditDialog() {
        _showEditDialog.value = false
        _workoutToEdit.value = null
        _editWorkoutName.value = ""
    }

    fun onEditNameChange(name: String) {
        _editWorkoutName.value = name
    }

    fun confirmEditWorkout() {
        val current = _workoutToEdit.value ?: return
        val name = _editWorkoutName.value.trim()
        if (name.isBlank()) return
        viewModelScope.launch {
            workoutRepository.updateWorkout(current.copy(name = name))
            onDismissEditDialog()
        }
    }

    fun deleteWorkout(workout: Workout) {
        viewModelScope.launch {
            workoutRepository.deleteWorkout(workout)
        }
    }
}
