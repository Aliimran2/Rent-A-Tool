package com.miassolutions.rentatool.ui.fragments.toolselection

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.miassolutions.rentatool.data.repository.RentalRepositoryImpl
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ToolSelectionViewModel @Inject constructor(
    private val repository: RentalRepositoryImpl
) : ViewModel() {

    private val _uiState = MutableStateFlow(ToolSelectionUiState())
    val uiState: StateFlow<ToolSelectionUiState> = _uiState.asStateFlow()

    fun onEvent(event: ToolSelectionUiEvent) {
        when (event) {
            is ToolSelectionUiEvent.OnEstimatedDateSelected -> {
                _uiState.update { it.copy(estimatedReturnDate = event.date) }
            }

            is ToolSelectionUiEvent.OnSearchQueryChanged -> {
                val filtered = _uiState.value.allTools.filter {
                    it.name.contains(event.query, ignoreCase = true)
                }
                _uiState.update { it.copy(filteredTools = filtered) }
            }

            is ToolSelectionUiEvent.OnSubmitRental -> {
                submitRental()
            }

            is ToolSelectionUiEvent.OnToolSelectionChanged -> {
                val updated = _uiState.value.selectedTools.toMutableMap()
                if (event.isChecked && event.quantity > 0) {
                    updated[event.tool.toolId] = event.quantity
                } else {
                    updated.remove(event.tool.toolId)
                }
                _uiState.update { it.copy(selectedTools = updated) }
            }

            is ToolSelectionUiEvent.RentalCompleted -> {
                _uiState.update {
                    it.copy(
                        isSubmitting = false,
                        rentalSuccess = true,
                        selectedTools = emptyMap()
                    )
                }
            }

            is ToolSelectionUiEvent.ShowError -> {
                _uiState.update { it.copy(errorMessage = event.message) }
            }
        }
    }

    private fun submitRental() {
        viewModelScope.launch {
            val selected = _uiState.value.selectedTools
            val date = _uiState.value.estimatedReturnDate

            if (selected.isEmpty() || date == null){
                onEvent(ToolSelectionUiEvent.ShowError("Please select tools and date"))
                return@launch
            }

            _uiState.update { it.copy(isSubmitting = true) }


        }
    }


}