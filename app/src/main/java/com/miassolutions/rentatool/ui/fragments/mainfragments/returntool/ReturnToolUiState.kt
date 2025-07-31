package com.miassolutions.rentatool.ui.fragments.mainfragments.returntool

data class ReturnToolsUiState(
    val tools: List<ReturnToolItem> = emptyList(),
    val customerName: String = "",
    val customerCnic: String = "",
    val totalRent: Int = 0,
    val isReturnSuccessful: Boolean = false
)


sealed class ReturnToolsUiEvent {
    data class ShowMessage(val message: String) : ReturnToolsUiEvent()
    data object ReturnCompleted : ReturnToolsUiEvent()
}

data class ReturnToolItem(
    val toolId: Long,
    val toolName: String,
    val rentedQuantity: Int,
    val remainingQuantity: Int,
    var returnQuantity: Int = 0,
    var isSelected: Boolean = true // optional for checkbox
)
