package com.miassolutions.rentatool.ui.fragments.mainfragments.lists.customers

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.miassolutions.rentatool.data.entities.CustomerEntity
import com.miassolutions.rentatool.data.repository.MainRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
@OptIn(FlowPreview::class, ExperimentalCoroutinesApi::class)
class CustomerListViewModel @Inject constructor(private val repository: MainRepository) :
    ViewModel() {

    private val _uiState = MutableStateFlow(CustomerListUiState())
    val uiState = _uiState.asStateFlow()

    private val _uiEvent = MutableSharedFlow<CustomerListUiEvent>()
    val uiEvent = _uiEvent.asSharedFlow()

    fun onSearchQueryChanged(query: String) {
        _uiState.update { it.copy(searchQuery = query) }
    }

    init {
        observeCustomers()
    }


    private fun observeCustomers() {
        viewModelScope.launch {
            _uiState.map { it.searchQuery }
                .debounce(300)
                .distinctUntilChanged()
                .flatMapLatest { query ->
                    if (query.isBlank())
                        repository.getAllCustomers()
                    else
                        repository.searchCustomers(query)
                }.collect { customers ->
                    _uiState.update { it.copy(customerList = customers, isLoading = false) }

                }
        }
    }

    fun navToDetail(customer: CustomerEntity) {
        viewModelScope.launch {
            _uiEvent.emit(CustomerListUiEvent.NavToCustomerDetail(customer))

        }
    }

    fun navToRentTools(customerId: Long, customerName: String) {
        viewModelScope.launch {
            _uiEvent.emit(CustomerListUiEvent.NavToRentTools(customerId, customerName))
        }
    }

    fun navToRentals(customerId: Long, customerName: String) {
        viewModelScope.launch {
            _uiEvent.emit(CustomerListUiEvent.NavToCustomerRentals(customerId, customerName))
        }
    }


}