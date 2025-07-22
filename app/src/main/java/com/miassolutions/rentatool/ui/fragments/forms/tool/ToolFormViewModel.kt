package com.miassolutions.rentatool.ui.fragments.forms.tool

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.miassolutions.rentatool.data.model.ToolEntity
import com.miassolutions.rentatool.data.repository.Repository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ToolFormViewModel @Inject constructor(private val repository: Repository) :
    ViewModel() {

    private val _uiState = MutableStateFlow(ToolFormUiState())
    val uiState = _uiState.asStateFlow()

    private val _uiEvent = MutableSharedFlow<ToolFormUiEvent>()
    val uiEvent = _uiEvent.asSharedFlow()


    fun onToolNameChange(value: String) {
        _uiState.update { it.copy(toolName = value) }
        validateForm()
    }

    fun onQuantityChanged(value: String) {
        _uiState.update { it.copy(noOfTools = value) }
        validateForm()
    }

    fun onConditionChanged(value: ToolCondition) {
        _uiState.update { it.copy(condition = value) }

    }

    fun onRentChanged(value: String) {
        _uiState.update { it.copy(rent = value) }
        validateForm()
    }

    fun onSubmitClick() {
        val state = _uiState.value
        if (state.toolName.isBlank() || state.noOfTools.isBlank() || state.rent.isBlank()) {
            viewModelScope.launch {
                _uiEvent.emit(ToolFormUiEvent.ShowToast("Please fill all the fields"))
            }

            return
        }

        viewModelScope.launch {
            repository.insertTool(toToolEntity(state))
            _uiEvent.emit(ToolFormUiEvent.ToolSaved)
            _uiEvent.emit(ToolFormUiEvent.ShowToast("Tool saved in database"))
        }

    }

    private fun toToolEntity(state: ToolFormUiState): ToolEntity {
        return ToolEntity(
            name = state.toolName,
            rentPerDay = state.rent.toDouble(),
            totalQuantity = state.noOfTools.toInt(),
            toolCondition = state.condition.name
        )
    }

    private fun validateForm() {
        val state = _uiState.value
        val isValid =
            state.toolName.isNotBlank() && state.noOfTools.isNotBlank() && state.rent.isNotBlank()
        _uiState.update { it.copy(isValidForm = isValid) }
    }

}