package com.miassolutions.rentatool.ui.fragments.lists.tools

import com.miassolutions.rentatool.data.entities.ToolEntity

data class StockUiState(
    val isLoading: Boolean = false,
    val stockList: List<ToolEntity> = emptyList(),
    val searchQuery: String = "",
    val errorMessage : String? = null
)


sealed class StockUiEvent {

    // showing which tools which customers has taken on rent todo()
    data object NavToDetails : StockUiEvent()
    data object NavToToolForm : StockUiEvent()
}