package com.miassolutions.rentatool.ui.fragments.mainfragments.forms.tool

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.miassolutions.rentatool.data.entities.ToolEntity
import com.miassolutions.rentatool.data.repository.ToolRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ToolFormViewModel @Inject constructor(private val toolRepository: ToolRepository) :
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
        _uiState.update { it.copy(totalQuantity = value) }
        validateForm()
    }

    fun onConditionChanged(value: ToolCondition) {
        _uiState.update { it.copy(condition = value) }

    }

    fun onRentChanged(value: String) {
        _uiState.update { it.copy(rentPricePerDay = value) }
        validateForm()
    }


    private var saveAndExitClick = false

    fun onSubmitClick(isSaveAndExit: Boolean = false) {

        saveAndExitClick = isSaveAndExit

        val state = _uiState.value
        if (state.toolName.isBlank() || state.totalQuantity.isBlank() || state.rentPricePerDay.isBlank()) {
            viewModelScope.launch {
                _uiEvent.emit(ToolFormUiEvent.ShowToast("Please fill all the fields"))
            }
            return
        }

        viewModelScope.launch {
            toolRepository.insertTool(toToolEntity(state))

            _uiEvent.emit(ToolFormUiEvent.ShowToast("Tool saved in database"))
            if (isSaveAndExit) {
                _uiEvent.emit(ToolFormUiEvent.NavigationBack)
            }
        }

    }

    private fun toToolEntity(state: ToolFormUiState): ToolEntity {
        return ToolEntity(
            name = state.toolName,
            totalQuantity = state.totalQuantity.toInt(),
            rentPricePerDay = state.rentPricePerDay.toDouble(),
        )
    }

    private fun validateForm() {
        val state = _uiState.value
        val isValid =
            state.toolName.isNotBlank() && state.totalQuantity.isNotBlank() && state.rentPricePerDay.isNotBlank()
        _uiState.update { it.copy(isValidForm = isValid) }
    }

}