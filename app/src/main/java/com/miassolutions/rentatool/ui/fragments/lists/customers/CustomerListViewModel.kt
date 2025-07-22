package com.miassolutions.rentatool.ui.fragments.lists.customers

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.miassolutions.rentatool.data.entities.CustomerEntity
import com.miassolutions.rentatool.data.repository.Repository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
@OptIn(FlowPreview::class, ExperimentalCoroutinesApi::class)
class CustomerListViewModel @Inject constructor(private val repository: Repository) : ViewModel() {

    private val _uiState = MutableStateFlow(CustomerListUiState())
    val uiState = _uiState.asStateFlow()

    private val _uiEvent = Channel<CustomerListUiEvent>()
    val uiEvent = _uiEvent.receiveAsFlow()

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

    fun onEditClick(customer: CustomerEntity) {
        viewModelScope.launch {
            _uiEvent.send(CustomerListUiEvent.NavToEditCustomer(customer))

        }
    }

    fun navToRentals(customerId : Long, customerName : String) {
        viewModelScope.launch {
            _uiEvent.send(CustomerListUiEvent.NavToCustomerRentals(customerId, customerName))
        }
    }

    fun deleteCustomer(customer: CustomerEntity) {
        viewModelScope.launch {
            repository.deleteCustomer(customer)
            _uiEvent.send(CustomerListUiEvent.ShowToast("Customer deleted"))
        }
    }

    fun deleteAllCustomers() {
        viewModelScope.launch {
            repository.deleteAllCustomers()
            _uiEvent.send(CustomerListUiEvent.ShowToast("All Customers deleted"))
        }
    }


}