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
}
