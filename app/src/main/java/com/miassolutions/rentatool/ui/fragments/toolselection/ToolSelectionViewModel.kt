package com.miassolutions.rentatool.ui.fragments.toolselection

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.miassolutions.rentatool.data.entities.ToolEntity
import com.miassolutions.rentatool.data.repository.Repository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.LocalDate
import javax.inject.Inject

@HiltViewModel
class ToolSelectionViewModel @Inject constructor(
    private val repository: Repository
) : ViewModel() {

    private val _uiState = MutableStateFlow(ToolSelectionUiState())
    val uiState: StateFlow<ToolSelectionUiState> = _uiState.asStateFlow()

    private val _uiEvent = MutableSharedFlow<ToolSelectionUiEvent>()
    val uiEvent = _uiEvent.asSharedFlow()

    fun onSearchQueryChanged(query: String) {
        _uiState.update { it.copy(searchQuery = query) }
        searchTools(query)
    }

    private fun searchTools(query: String) {
        viewModelScope.launch {
            repository.searchTool(query).collect { tools ->
                _uiState.update { it.copy(tools = tools) }

            }
        }
    }

    private fun loadAllTools() {
        viewModelScope.launch {
            repository.getAllTools().collect { tools ->
                _uiState.update { it.copy(tools = tools) }
            }
        }
    }


    fun toggleToolSelection(tool: ToolEntity, quantity: Int, isChecked: Boolean) {
        val current = _uiState.value.selectedTools.toMutableList()
        if (isChecked) {
            current.removeAll { it.toolId == tool.toolId }
            current.add(RentedTool(tool.toolId, tool.name, quantity))
        } else {
            current.removeAll { it.toolId == tool.toolId }
        }
        _uiState.update { it.copy(selectedTools = current) }
    }

    fun onEstimatedReturnSelected(date: LocalDate) {
        _uiState.update { it.copy(estimatedReturnDate = date) }
    }

    fun rentTools(customerId : Long){
        viewModelScope.launch {
            val state = _uiState.value
            if (state.selectedTools.isEmpty() || state.estimatedReturnDate == null){
                _uiEvent.emit(ToolSelectionUiEvent.ShowToast("Please select tools and return date"))
                return@launch
            }


        }
    }
}