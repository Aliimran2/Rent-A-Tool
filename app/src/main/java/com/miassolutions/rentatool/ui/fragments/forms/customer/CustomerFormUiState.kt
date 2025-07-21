package com.miassolutions.rentatool.ui.fragments.forms.customer

data class CustomerFormUiState(
    val customerId: Long? = null,
    val customerName: String = "",
    val customerCnic: String = "",
    val customerPhone: String = "",
    val contractorName: String = "",
    val contractorPhone: String = "",
    val ownerName: String = "",
    val ownerPhone: String = "",
    val constructionPlace: String = ""
)


sealed class CustomerUiEvent {
    data class DuplicateCNIC(val errorCode: Int) : CustomerUiEvent()
    data class CustomerAdded(val successCode : Int) : CustomerUiEvent()
    data class ShowToast(val message: String) : CustomerUiEvent()
    data object NavigateBack : CustomerUiEvent()
}