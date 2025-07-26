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

    private val selectedTools = mutableListOf<SelectedTool>()

    init {
        loadTools()
    }

    fun loadTools(query: String = "") {
        viewModelScope.launch {
            toolRepository.getToolsWithAvailability().collect { tools ->
                val filtered = if (query.isBlank()) tools
                else tools.filter { it.tool.name.contains(query, ignoreCase = true) }

                _uiState.value = _uiState.value.copy(
                    tools = filtered,
                    searchQuery = query,
                    isLoading = false,
                    errorMessage = null
                )
            }
        }
    }

    fun onToolChecked(toolId: Long, isChecked: Boolean, quantity: Int = 1) {
        if (isChecked) {
            selectedTools.removeAll { it.toolId == toolId }
            selectedTools.add(SelectedTool(toolId, quantity))
        } else {
            selectedTools.removeAll { it.toolId == toolId }
        }
        _uiState.value = _uiState.value.copy(selectedTools = selectedTools.toList())
    }

    fun onQuantityChanged(toolId: Long, quantity: Int) {
        val index = selectedTools.indexOfFirst { it.toolId == toolId }
        if (index != -1) {
            selectedTools[index] = SelectedTool(toolId, quantity)
            _uiState.value = _uiState.value.copy(selectedTools = selectedTools.toList())
        }
    }

    fun onDateClicked() {
        viewModelScope.launch {
            _uiEvent.emit(ToolSelectionUiEvent.ShowDatePicker(_uiState.value.selectedDate))
        }
    }

    fun onDateSelected(newDate: LocalDate) {
        _uiState.value = _uiState.value.copy(selectedDate = newDate)
    }

    fun onSearchChanged(query: String) {
        loadTools(query)
    }

    fun onSubmitClicked() {
        if (selectedTools.isEmpty()) {
            viewModelScope.launch {
                _uiEvent.emit(ToolSelectionUiEvent.ShowToast("Select at least one tool"))
            }
            return
        }
        if (_uiState.value.selectedDate == null) {
            viewModelScope.launch {
                _uiEvent.emit(ToolSelectionUiEvent.ShowToast("Please select a date"))
            }
            return
        }
        viewModelScope.launch {
            _uiEvent.emit(ToolSelectionUiEvent.NavigateToConfirmation)
        }
    }
}
