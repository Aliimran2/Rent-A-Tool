package com.miassolutions.rentatool.ui.fragments.lists.rentalrecord

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.miassolutions.rentatool.data.dao.RentalOrderDao
import com.miassolutions.rentatool.data.dao.RentalRelationsDao
import com.miassolutions.rentatool.data.relationship.RentalWithTools
import com.miassolutions.rentatool.data.repository.CustomerRepository
import com.miassolutions.rentatool.data.repository.RentalOrderRepository
import com.miassolutions.rentatool.data.repositoryimpl.RentalOrderRepositoryImpl
import com.miassolutions.rentatool.data.repositoryimpl.ToolRepositoryImpl
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
class RentalsViewModel @Inject constructor(
    private val rentalOrderRepository: RentalOrderRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(RentalsUiState())
    val uiState: StateFlow<RentalsUiState> = _uiState

    private val _uiEvent = MutableSharedFlow<RentalsUiEvent>()
    val uiEvent = _uiEvent.asSharedFlow()

    fun loadData(customerId: Long, customerName: String) {
        viewModelScope.launch {
            _uiState.update {
                it.copy(
                    isLoading = true,
                    customerId = customerId,
                    customerName = customerName
                )
            }

            try {
                rentalOrderRepository.getOrdersWithRentedTools(customerId).collect { orders ->
                    val totalRent = orders.sumOf { order ->
                        order.rentedTools.sumOf { it.rentPricePerDay * it.rentedQuantity * it.daysRented }
                    }

                    val activeCount = orders.count { it.rentedTools.any { tool -> tool.remainingQuantity > 0 } }
                    val returnedCount = orders.size - activeCount

                    _uiState.update {
                        it.copy(
                            rentalOrders = orders,
                            totalRent = totalRent,
                            activeOrdersCount = activeCount,
                            returnedOrderCount = returnedCount,
                            isLoading = false
                        )
                    }
                }
            } catch (e: Exception) {
                _uiState.update { it.copy(isLoading = false, errorMessage = e.message) }
                _uiEvent.emit(RentalsUiEvent.ShowSnackbar("Error loading rentals: ${e.message}"))
            }
        }
    }

//    fun onRentToolsClick() {
//        val state = _uiState.value
//        viewModelScope.launch {
//            _uiEvent.emit(RentalsUiEvent.NavigateToRentTools(state.customerId, state.customerName))
//        }
//    }
//
//    fun onReturnClick(orderId: Long) {
//        val state = _uiState.value
//        viewModelScope.launch {
//            _uiEvent.emit(RentalsUiEvent.NavigateToReturnTools(orderId, state.customerName))
//        }
//    }
//
//    fun onRentalClick(orderId: Long) {
//        viewModelScope.launch {
//            _uiEvent.emit(RentalsUiEvent.NavigateToRentalDetail(orderId))
//        }
//    }
}
