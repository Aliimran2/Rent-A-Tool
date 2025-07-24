package com.miassolutions.rentatool.ui.fragments.toolselection

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.savedstate.savedState
import com.miassolutions.rentatool.data.entities.RentalOrderEntity
import com.miassolutions.rentatool.data.repository.ToolRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.LocalDate
import javax.inject.Inject

@HiltViewModel
class ToolSelectionViewModel @Inject constructor(
    private val toolRepository: ToolRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(ToolSelectionUiState())
    val uiState: StateFlow<ToolSelectionUiState> = _uiState

    private val _uiEvent = MutableSharedFlow<ToolSelectionUiEvent>()
    val uiEvent = _uiEvent.asSharedFlow()

    fun setCustomer(id: Long, name: String) {

    }

    fun loadAvailableTools() {
        viewModelScope.launch {
            toolRepository.getToolsWithAvailability().collect { tools ->
                _uiState.update { state ->
                    state.copy(
                        tools = tools.map {
                            ToolItemUiModel(
                                toolId = it.tool.toolId,
                                name = it.tool.name,
                                availableQuantity = it.availableQuantity
                            )
                        }
                    )
                }
            }
        }
    }

    fun onEvent(event: ToolSelectionUiEvent) {
        when (event) {
            is ToolSelectionUiEvent.OnToolChecked -> toggleToolSelection(event.toolId, event.checked)
            is ToolSelectionUiEvent.OnQuantityChanged -> updateQuantity(event.toolId, event.quantity)
            is ToolSelectionUiEvent.OnEstimatedDateChanged -> _uiState.update { it.copy(estimatedReturnDate = event.date) }
            is ToolSelectionUiEvent.OnSubmit -> submitRental()
        }
    }

    private fun toggleToolSelection(toolId: Long, isChecked: Boolean) {
        _uiState.update { state ->
            state.copy(
                tools = state.tools.map {
                    if (it.toolId == toolId) it.copy(isSelected = isChecked) else it
                }
            )
        }
    }

    private fun updateQuantity(toolId: Long, quantity: String) {
        _uiState.update { state ->
            state.copy(
                tools = state.tools.map {
                    if (it.toolId == toolId) it.copy(selectedQuantity = quantity) else it
                }
            )
        }
    }

    private fun submitRental() {
        // You can hook this to RentalRepository logic
    }

}
