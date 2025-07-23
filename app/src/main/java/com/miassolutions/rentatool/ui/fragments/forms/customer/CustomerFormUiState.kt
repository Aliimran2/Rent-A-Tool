package com.miassolutions.rentatool.ui.fragments.forms.customer

data class CustomerFormUiState(
    val customerId: Long? = null,
    val customerName: String = "",
    val customerCnic: String = "",
    val customerPhone: String = "",

)


sealed class CustomerUiEvent {
    data class CustomerAdded(val customerId: Long) : CustomerUiEvent()
    data class ShowToast(val message: String) : CustomerUiEvent()
    data object NavigateBack : CustomerUiEvent()
}