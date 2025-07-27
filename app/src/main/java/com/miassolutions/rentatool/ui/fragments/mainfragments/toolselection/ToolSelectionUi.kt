package com.miassolutions.rentatool.ui.fragments.mainfragments.toolselection

import com.google.gson.Gson
import com.miassolutions.rentatool.data.relationship.ToolWithAvailability
import java.time.LocalDate

data class SelectedTool(
    val toolId: Long,
    val toolName : String,
    val rentPricePerDay : Double,
    val quantity: Int
)

data class ToolSelectionUiState(
    val tools: List<ToolWithAvailability> = emptyList(),
    val searchQuery: String = "",
    val selectedTools: List<SelectedTool> = emptyList(),
    val selectedDate: LocalDate? = null,
    val isLoading: Boolean = true,
    val errorMessage: String? = null
) {
    val selectedToolsJson: String
        get() = Gson().toJson(selectedTools)
}

sealed class ToolSelectionUiEvent {
    data class ShowToast(val message: String) : ToolSelectionUiEvent()
    data class NavigateToConfirmation(
        val customerId: Long,
        val customerName: String,
        val estReturnDate: String,
        val selectedToolsJson: String
    ) : ToolSelectionUiEvent()

    data class ShowDatePicker(val currentDate: LocalDate?) : ToolSelectionUiEvent()
}
