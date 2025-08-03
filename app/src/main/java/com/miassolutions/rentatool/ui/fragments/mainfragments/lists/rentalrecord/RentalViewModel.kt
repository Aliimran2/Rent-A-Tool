package com.miassolutions.rentatool.ui.fragments.mainfragments.lists.rentalrecord

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.miassolutions.rentatool.data.repository.MainRepository
import com.miassolutions.rentatool.utils.Constants
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.temporal.ChronoUnit
import javax.inject.Inject

@HiltViewModel
class RentalsViewModel @Inject constructor(
    private val repository: MainRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(RentalsUiState())
    val uiState: StateFlow<RentalsUiState> = _uiState

    private val _uiEvent = MutableSharedFlow<RentalsUiEvent>()
    val uiEvent = _uiEvent.asSharedFlow()

    fun loadData(customerId: Long) {
        viewModelScope.launch {
            _uiState.update {
                it.copy(
                    isLoading = true,
                )
            }

            try {
                repository.getOrdersWithRentedTools(customerId).collect { orders ->
                    val totalRent = orders.sumOf { order ->
                        val rentDays = ChronoUnit.DAYS.between(order.rentalOrder.rentDate, LocalDate.now()).coerceAtLeast(1)
                        order.rentedTools.sumOf { tool ->
                            Log.d(Constants.TAG, "${tool.rentPricePerDay} - ${tool.rentedQuantity}")
                            tool.rentPricePerDay * tool.rentedQuantity * rentDays

                        }
                    }

                    val activeCount =
                        orders.count { it.rentedTools.any { tool -> tool.rentedQuantity > 0 } } //todo
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



    fun onReturnClick(customerId: Long) {
        val state = _uiState.value
        viewModelScope.launch {
            _uiEvent.emit(RentalsUiEvent.NavigationToReturnTools(customerId))
        }
    }


}
