package com.miassolutions.rentatool.ui.fragments.mainfragments.returntool

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.miassolutions.rentatool.data.repository.MainRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ReturnToolsViewModel @Inject constructor(
    private val repository: MainRepository // or relevant sub-repositories
) : ViewModel() {

    private val _uiState = MutableStateFlow(ReturnToolsUiState())
    val uiState: StateFlow<ReturnToolsUiState> = _uiState.asStateFlow()

    private val _event = MutableSharedFlow<ReturnToolsUiEvent>()
    val event = _event.asSharedFlow()

    private var internalToolMap = mutableMapOf<Long, ReturnToolItem>()



    fun loadCustomerAndTools(customerId: Long) {
        viewModelScope.launch {
            val customer = repository.getCustomerById(customerId)
            val rentalWithTools = repository.getActiveRentalWithTools(customerId)

            val toolItems = rentalWithTools.flatMap { rental ->
                rental.rentedTools.map { rentedTool ->
                    val tool = rentedTool.tool
                    val rented = rentedTool.rentedTool.rentedQuantity
//                    val remaining = rentedTool.rentedTool.remainingQuantity
                    ReturnToolItem(
                        toolId = tool.toolId,
                        toolName = tool.name,
                        rentedQuantity = rented,
                        remainingQuantity = rented,
                        returnQuantity = 0,
                        isSelected = true
                    ).also { internalToolMap[it.toolId] = it }
                }
            }

            if (customer != null) {
                _uiState.update {
                    it.copy(
                        tools = toolItems,
                        totalRent = rentalWithTools.sumOf { it.rentalOrder.totalAmount }.toInt()
                    )
                }
            }
        }
    }

//    fun onReturnQuantityChanged(toolId: Int, quantity: Int) {
//        val updated = internalToolMap[toolId]?.copy(returnQuantity = quantity) ?: return
//        internalToolMap[toolId] = updated
//        _uiState.update { it.copy(tools = internalToolMap.values.toList()) }
//    }
//
//    fun onCheckboxChanged(toolId: Int, isChecked: Boolean) {
//        val updated = internalToolMap[toolId]?.copy(isSelected = isChecked) ?: return
//        internalToolMap[toolId] = updated
//        _uiState.update { it.copy(tools = internalToolMap.values.toList()) }
//    }
//
//    fun confirmReturn(customerId: Int) {
//        viewModelScope.launch {
//            val toReturn = internalToolMap.values
//                .filter { it.isSelected && it.returnQuantity > 0 }
//
//            if (toReturn.isEmpty()) {
//                _event.emit(ReturnToolsUiEvent.ShowMessage("No tools selected for return"))
//                return@launch
//            }
//
//            try {
//                repository.returnTools(customerId, toReturn)
//                _event.emit(ReturnToolsUiEvent.ShowMessage("Tools returned successfully"))
//                _event.emit(ReturnToolsUiEvent.ReturnCompleted)
//                _uiState.update { it.copy(isReturnSuccessful = true) }
//            } catch (e: Exception) {
//                _event.emit(ReturnToolsUiEvent.ShowMessage("Failed: ${e.message}"))
//            }
//        }
//    }
}
