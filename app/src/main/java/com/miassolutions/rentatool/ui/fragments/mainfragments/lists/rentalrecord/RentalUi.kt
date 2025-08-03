package com.miassolutions.rentatool.ui.fragments.mainfragments.lists.rentalrecord

import com.miassolutions.rentatool.data.relationship.RentalOrderWithRentedTools

data class RentalsUiState(
    val totalRent: Double = 0.0,
    val activeOrdersCount: Int = 0,
    val returnedOrderCount: Int = 0,
    val rentalOrders: List<RentalOrderWithRentedTools> = emptyList(),
    val isLoading: Boolean = false,
    val errorMessage: String? = null
)

sealed class RentalsUiEvent {

    data class NavigationToReturnTools(val orderId: Long) : RentalsUiEvent()
    data class NavigationToRentalDetail(val orderId: Long) : RentalsUiEvent()
    data class ShowSnackbar(val message: String) : RentalsUiEvent()
}