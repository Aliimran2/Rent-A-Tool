package com.miassolutions.rentatool.ui.fragments.toolselection

import com.miassolutions.rentatool.data.relationship.ToolWithAvailability
import java.time.LocalDate

data class SelectedTool(
    val toolId: Long,
    val quantity: Int
)

data class ToolSelectionUiState(
    val tools: List<ToolWithAvailability> = emptyList(),
    val searchQuery: String = "",
    val selectedTools: List<SelectedTool> = emptyList(),
    val selectedDate: LocalDate? = null,
    val isLoading: Boolean = true,
    val errorMessage: String? = null
)

sealed class ToolSelectionUiEvent {
    data class ShowToast(val message: String) : ToolSelectionUiEvent()
    data object NavigateToConfirmation : ToolSelectionUiEvent()
    data class ShowDatePicker(val currentDate: LocalDate?) : ToolSelectionUiEvent()
}
