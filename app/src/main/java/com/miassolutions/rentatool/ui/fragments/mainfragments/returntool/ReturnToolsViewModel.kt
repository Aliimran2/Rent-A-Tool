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
    private val repository: MainRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(ReturnToolsUiState())
    val uiState: StateFlow<ReturnToolsUiState> = _uiState.asStateFlow()

    private val _event = MutableSharedFlow<ReturnToolsUiEvent>()
    val event = _event.asSharedFlow()

    private var orderId = 0L

    fun updateOrderId(id: Long) {
        orderId = id
    }


    fun loadRentedTools(orderId: Long) {
        viewModelScope.launch {
            val rentedWithReturns = repository.getRentedToolsWithReturns(orderId)
            val items = rentedWithReturns.map {
                val returned = it.returns.sumOf { r -> r.returnedQuantity }
                val remaining = it.rentedTool.rentedQuantity - returned

                ReturnToolItem(
                    rentedToolId = it.rentedTool.rentedToolId,
                    toolId = it.tool.toolId,
                    toolName = it.tool.name,
                    rentedQuantity = it.rentedTool.rentedQuantity,
                    remainingQuantity = remaining,
                    rentPricePerDay = it.rentedTool.rentPricePerDay
                )
            }
            _uiState.value = _uiState.value.copy(tools = items)
        }
    }

    fun updateSelection(id: Long, isChecked: Boolean, qty: Int) {
        _uiState.update { state ->
            val updated = state.tools.map {
                if (it.rentedToolId == id) {
                    it.copy(
                        isSelected = isChecked,
                        returnQuantity = if (isChecked) qty else 0
                    )
                } else it
            }
            state.copy(tools = updated)
        }
    }

    fun onReturnButtonClick() {
        val selectedItems = getSelectedItems()
        viewModelScope.launch {

            _event.emit(
                ReturnToolsUiEvent.NavToReturnConfirm(
                    orderId,
                    ReturnToolListWrapper(selectedItems)
                )
            )
        }
    }


    fun getSelectedItems(): List<ReturnToolItem> =
        _uiState.value.tools.filter { it.isSelected && it.returnQuantity > 0 }


}




