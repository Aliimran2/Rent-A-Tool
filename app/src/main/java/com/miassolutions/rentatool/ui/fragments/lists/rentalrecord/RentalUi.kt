package com.miassolutions.rentatool.ui.fragments.lists.rentalrecord

import com.miassolutions.rentatool.data.entities.RentalOrderEntity

data class RentalUiState(
    val isLoading: Boolean = false,
    val customerId : Long = -1L,
    val rentalList: List<RentalOrderEntity> = emptyList()
)

sealed class RentalUiEvent {
    data class NavigationToRentTools(val customerId: Long) : RentalUiEvent()
    data class NavigationToUpdateRentals(val orderId: Long) : RentalUiEvent()
    data class ShowToast(val message :String) : RentalUiEvent()
}