package com.miassolutions.rentatool.ui.fragments.lists.rentalrecord

import com.miassolutions.rentatool.data.entities.RentalOrderEntity
import com.miassolutions.rentatool.data.relationship.RentalOrderWithRentedTools

data class RentalsUiState(
    val customerId: Long = -1L,
    val totalRent: Double = 0.0,
    val activeOrdersCount: Int = 0,
    val returnedOrderCount: Int = 0,
    val rentalOrders: List<RentalOrderWithRentedTools> = emptyList(),
    val isLoading: Boolean = false,
    val errorMessage: String? = null
)

sealed class RentalsUiEvent {
    data class NavigationToRentTools(val customerId: Long, val customerName: String) :
        RentalsUiEvent()

    data class NavigationToReturnTools(val orderId: Long, val customerName: String) :
        RentalsUiEvent()

    data class NavigationToRentalDetail(val orderId: Long) : RentalsUiEvent()
    data class ShowSnackbar(val message: String) : RentalsUiEvent()
}