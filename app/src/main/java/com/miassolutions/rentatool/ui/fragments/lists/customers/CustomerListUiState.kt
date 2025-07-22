package com.miassolutions.rentatool.ui.fragments.lists.customers

import com.miassolutions.rentatool.data.entities.CustomerEntity

data class CustomerListUiState(
    val isLoading: Boolean = false,
    val customerList: List<CustomerEntity> = emptyList(),
    val searchQuery: String = ""
)


sealed class CustomerListUiEvent {
    data class NavToEditCustomer(val customer: CustomerEntity) : CustomerListUiEvent()
    data class NavToDetail(val customer: CustomerEntity) : CustomerListUiEvent()
    data class NavToCustomerRentals(val customerId : Long, val customerName : String) : CustomerListUiEvent()
    data class ShowToast(val message: String) : CustomerListUiEvent()
}