package com.miassolutions.rentatool.ui.fragments.toolselection

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.miassolutions.rentatool.data.entities.RentalOrderEntity
import com.miassolutions.rentatool.data.repository.RentalRepository
import com.miassolutions.rentatool.data.repositoryimpl.ToolRepositoryImpl
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.LocalDate
import javax.inject.Inject

@HiltViewModel
class ToolSelectionViewModel @Inject constructor(
    private val toolRepository: ToolRepositoryImpl
) : ViewModel() {

    private val _uiState = MutableStateFlow(ToolSelectionUiState())
    val uiState: StateFlow<ToolSelectionUiState> = _uiState

    fun setCustomerId(id: Long) {
        _uiState.update { it.copy(customerId = id) }
    }

    fun loadAllTools() {
        viewModelScope.launch {
            toolRepository.getAllTools().collect { tools ->
                _uiState.update {
                    it.copy(allTools = tools, filteredTools = tools)
                }
            }
        }
    }

    fun onUiEvent(event: ToolSelectionUiEvent) {
        when (event) {
            is ToolSelectionUiEvent.OnToolSelectionChanged -> {
                _uiState.update { state ->
                    val updatedMap = state.selectedTools.toMutableMap()
                    if (event.isChecked && event.quantity > 0) {
                        updatedMap[event.tool.toolId] = event.quantity
                    } else {
                        updatedMap.remove(event.tool.toolId)
                    }
                    state.copy(selectedTools = updatedMap)
                }
            }

            is ToolSelectionUiEvent.OnSearchQueryChanged -> {
                _uiState.update { state ->
                    val filtered = state.allTools.filter {
                        it.name.contains(event.query, ignoreCase = true)
                    }
                    state.copy(filteredTools = filtered)
                }
            }

            is ToolSelectionUiEvent.OnEstimatedDateSelected -> {
                _uiState.update { it.copy(estimatedReturnDate = event.date) }
            }

            is ToolSelectionUiEvent.OnSubmitRental -> {
                submitRental()
            }

            is ToolSelectionUiEvent.ShowError -> {
                _uiState.update { it.copy(errorMessage = event.message) }
            }

            is ToolSelectionUiEvent.RentalCompleted -> {
                _uiState.update { it.copy(rentalSuccess = true) }
            }
        }
    }

    private fun submitRental() {
        val state = _uiState.value
        val customerId = state.customerId
        val estimatedReturnDate = state.estimatedReturnDate

        if (customerId == null || estimatedReturnDate == null || state.selectedTools.isEmpty()) {
            _uiState.update { it.copy(errorMessage = "Please select tools, customer and return date.") }
            return
        }

        val order = RentalOrderEntity(
            customerId = customerId,
            rentDate = LocalDate.now(),
            estimatedReturnDate = estimatedReturnDate
        )

        val rentedTools = state.selectedTools.map { (toolId, qty) ->

        }

        viewModelScope.launch {
            try {
                _uiState.update { it.copy(isSubmitting = true) }

//                rentalRepository.rentToolsToCustomer(order, rentedTools)

                _uiState.update {
                    it.copy(
                        isSubmitting = false,
                        rentalSuccess = true,
                        errorMessage = null
                    )
                }
            } catch (e: Exception) {
                _uiState.update {
                    it.copy(
                        isSubmitting = false,
                        errorMessage = e.message ?: "Rental failed"
                    )
                }
            }
        }
    }
}
