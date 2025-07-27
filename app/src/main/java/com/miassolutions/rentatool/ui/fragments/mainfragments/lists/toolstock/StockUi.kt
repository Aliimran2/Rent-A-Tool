package com.miassolutions.rentatool.ui.fragments.mainfragments.lists.toolstock

import com.miassolutions.rentatool.data.relationship.ToolWithAvailability

data class StockUiState(
    val isLoading: Boolean = false,
    val searchQuery : String = "",
    val stockList: List<ToolUiModel> = emptyList(),
    val errorMessage: String? = null,
    val isEmpty : Boolean = false
)


sealed class StockUiEvent {

    // showing which tools which customers has taken on rent todo()
    data object NavToDetails : StockUiEvent()
    data object NavToToolForm : StockUiEvent()
    data class ShowSnackbar(val message : String) : StockUiEvent()
}


data class ToolUiModel(
    val id: Long,
    val name: String,
    val totalQuantity: Int,
    val availability: Int,
    val rentPerDay: Double
)

fun ToolWithAvailability.toUiModel(): ToolUiModel {
    return ToolUiModel(
        id = tool.toolId,
        name = tool.name,
        totalQuantity = tool.totalQuantity,
        availability = availableQuantity,
        rentPerDay = tool.rentPricePerDay
    )
}