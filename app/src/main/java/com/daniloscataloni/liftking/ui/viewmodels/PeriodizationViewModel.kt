package com.daniloscataloni.liftking.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.daniloscataloni.liftking.domain.models.Periodization
import com.daniloscataloni.liftking.data.repositories.IPeriodizationRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class PeriodizationViewModel(
    private val repository: IPeriodizationRepository
) : ViewModel() {

    val periodizations = repository.getAllPeriodizations()
        .stateIn(viewModelScope, SharingStarted.Lazily, emptyList())

    val activePeriodization = repository.getActivePeriodization()
        .stateIn(viewModelScope, SharingStarted.Lazily, null)

    private val _showCreateDialog = MutableStateFlow(false)
    val showCreateDialog: StateFlow<Boolean> = _showCreateDialog.asStateFlow()

    private val _newPeriodizationName = MutableStateFlow("")
    val newPeriodizationName: StateFlow<String> = _newPeriodizationName.asStateFlow()

    private val _showEditDialog = MutableStateFlow(false)
    val showEditDialog: StateFlow<Boolean> = _showEditDialog.asStateFlow()

    private val _periodizationToEdit = MutableStateFlow<Periodization?>(null)
    val periodizationToEdit: StateFlow<Periodization?> = _periodizationToEdit.asStateFlow()

    private val _editPeriodizationName = MutableStateFlow("")
    val editPeriodizationName: StateFlow<String> = _editPeriodizationName.asStateFlow()

    fun onShowCreateDialog() {
        _showCreateDialog.value = true
    }

    fun onDismissCreateDialog() {
        _showCreateDialog.value = false
        _newPeriodizationName.value = ""
    }

    fun onNameChange(name: String) {
        _newPeriodizationName.value = name
    }

    fun createPeriodization() {
        val name = _newPeriodizationName.value.trim()
        if (name.isNotEmpty()) {
            viewModelScope.launch {
                val id = repository.insert(Periodization(name = name))
                repository.setActive(id)
                onDismissCreateDialog()
            }
        }
    }

    fun onShowEditDialog(periodization: Periodization) {
        _periodizationToEdit.value = periodization
        _editPeriodizationName.value = periodization.name
        _showEditDialog.value = true
    }

    fun onDismissEditDialog() {
        _showEditDialog.value = false
        _periodizationToEdit.value = null
        _editPeriodizationName.value = ""
    }

    fun onEditNameChange(name: String) {
        _editPeriodizationName.value = name
    }

    fun confirmEditPeriodization() {
        val current = _periodizationToEdit.value ?: return
        val name = _editPeriodizationName.value.trim()
        if (name.isBlank()) return
        viewModelScope.launch {
            repository.update(current.copy(name = name))
            onDismissEditDialog()
        }
    }

    fun setActive(periodization: Periodization) {
        viewModelScope.launch {
            repository.setActive(periodization.id)
        }
    }

    fun archive(periodization: Periodization) {
        viewModelScope.launch {
            repository.archive(periodization.id)
        }
    }

    fun deletePeriodization(periodization: Periodization) {
        viewModelScope.launch {
            repository.delete(periodization)
        }
    }
}
