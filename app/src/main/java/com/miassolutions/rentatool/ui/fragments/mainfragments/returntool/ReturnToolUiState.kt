package com.miassolutions.rentatool.ui.fragments.mainfragments.returntool

data class ReturnToolsUiState(
    val tools: List<ReturnToolItem> = emptyList(),

)


sealed class ReturnToolsUiEvent {
    data class ShowMessage(val message: String) : ReturnToolsUiEvent()
    data object ReturnCompleted : ReturnToolsUiEvent()
}

data class ReturnToolItem(
    val rentedToolId : Long,
    val toolId: Long,
    val toolName: String,
    val rentedQuantity: Int,
    val remainingQuantity: Int,
    val rentPricePerDay : Double,
    val returnQuantity : Int = 0,
    val isSelected : Boolean = false

)
