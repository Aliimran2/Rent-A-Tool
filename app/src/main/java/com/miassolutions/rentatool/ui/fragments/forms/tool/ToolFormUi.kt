package com.miassolutions.rentatool.ui.fragments.forms.tool

data class ToolFormUiState(
    val toolName: String = "",
    val totalQuantity: String = "",
    val condition: ToolCondition = ToolCondition.NEW,
    val rentPricePerDay: String = "",
    val isValidForm: Boolean = false
)

enum class ToolCondition {
    NEW, OLD
}


sealed class ToolFormUiEvent {
    data class ShowToast(val message: String) : ToolFormUiEvent()
    data object NavigationBack : ToolFormUiEvent()
}
