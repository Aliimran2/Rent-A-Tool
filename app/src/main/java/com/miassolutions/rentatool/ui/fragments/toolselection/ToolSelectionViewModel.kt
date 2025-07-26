package com.miassolutions.rentatool.ui.fragments.toolselection

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.gson.Gson
import com.miassolutions.rentatool.data.repository.ToolRepository
import com.miassolutions.rentatool.utils.extenstions.toFormattedDate
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import java.time.LocalDate
import javax.inject.Inject

@HiltViewModel
class ToolSelectionViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val toolRepository: ToolRepository,
    private val gson: Gson
) : ViewModel() {

    private val _uiState = MutableStateFlow(ToolSelectionUiState())
    val uiState: StateFlow<ToolSelectionUiState> = _uiState

    private val _uiEvent = MutableSharedFlow<ToolSelectionUiEvent>()
    val uiEvent = _uiEvent.asSharedFlow()

    private val selectedTools = mutableListOf<SelectedTool>()

    private val customerId: Long = checkNotNull(savedStateHandle["customerId"])
    private val customerName: String = checkNotNull(savedStateHandle["customerName"])

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
        selectedTools.removeAll { it.toolId == toolId }
        val toolName =
            _uiState.value.tools.find { it.tool.toolId == toolId }?.tool?.name ?: "Unknown"
        if (isChecked) {
            selectedTools.add(SelectedTool(toolId, toolName, quantity))
        }
        _uiState.value = _uiState.value.copy(selectedTools = selectedTools.toList())
    }

    fun onQuantityChanged(toolId: Long, quantity: Int) {
        val index = selectedTools.indexOfFirst { it.toolId == toolId }
        val toolName =
            _uiState.value.tools.find { it.tool.toolId == toolId }?.tool?.name ?: "Unknown"
        if (index != -1) {
            selectedTools[index] = SelectedTool(toolId,toolName, quantity)
            _uiState.value = _uiState.value.copy(selectedTools = selectedTools.toList())
        }
    }

    fun onDateClicked() =
        sendUiEvent(ToolSelectionUiEvent.ShowDatePicker(_uiState.value.selectedDate))

    fun onDateSelected(newDate: LocalDate) {
        _uiState.value = _uiState.value.copy(selectedDate = newDate)
    }

    fun onSearchChanged(query: String) {
        loadTools(query)
    }

    fun onSubmitClicked() {
        val state = _uiState.value

        when {
            state.selectedDate == null -> sendUiEvent(ToolSelectionUiEvent.ShowToast("Please select a date"))
            selectedTools.isEmpty() -> sendUiEvent(ToolSelectionUiEvent.ShowToast("Select at least one tool"))
            else -> {
                val selectedToolsJson = gson.toJson(selectedTools)
                sendUiEvent(
                    ToolSelectionUiEvent.NavigateToConfirmation(
                        customerId,
                        customerName,
                        state.selectedDate.toString(),
                        selectedToolsJson
                    )
                )
            }
        }
    }

    private fun sendUiEvent(event: ToolSelectionUiEvent) {
        viewModelScope.launch {
            _uiEvent.emit(event)
        }
    }
}
