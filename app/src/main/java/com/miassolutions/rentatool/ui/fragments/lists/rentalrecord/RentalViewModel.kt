package com.miassolutions.rentatool.ui.fragments.lists.rentalrecord

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
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
class RentalViewModel @Inject constructor(private val repository: Repository) : ViewModel() {

    private val _uiState = MutableStateFlow(RentalUiState())
    val uiState = _uiState.asStateFlow()

    private val _uiEvent = MutableSharedFlow<RentalUiEvent>()
    val uiEvent = _uiEvent.asSharedFlow()

    fun loadRentals(customerId: Long) {
        _uiState.update { it.copy(isLoading = true, customerId = customerId) }
        viewModelScope.launch {
            repository.getAllRentalOrders(customerId)
                .collect{orders ->
                    _uiState.update { it.copy(isLoading = false, rentalList = orders) }

                }
        }
    }

    fun onRentToolsClick(){
        viewModelScope.launch {
            _uiEvent.emit(RentalUiEvent.NavigationToRentTools(_uiState.value.customerId))
        }
    }

    fun onUpdateRentalClick(orderId : Long){
        viewModelScope.launch {
            _uiEvent.emit(RentalUiEvent.NavigationToUpdateRentals(orderId))
        }
    }

    fun showToast(message: String) {
        viewModelScope.launch {
            _uiEvent.emit(RentalUiEvent.ShowToast(message))
        }
    }


}