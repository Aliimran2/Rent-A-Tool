package com.miassolutions.rentatool.ui.fragments.toolselection

import com.miassolutions.rentatool.data.entities.ToolEntity
import com.miassolutions.rentatool.data.relationship.ToolWithAvailability
import java.time.LocalDate

data class ToolSelectionUiState(
    val tools: List<ToolWithAvailability> = emptyList(),
    val searchQuery: String = "",
    val selectedTools: Map<Long, Int> = emptyMap(), // toolId to quantity
    val selectedDate: LocalDate = LocalDate.now(),
    val isLoading: Boolean = true,
    val errorMessage: String? = null
)




sealed class ToolSelectionUiEvent {
    data class ShowToast(val message: String) : ToolSelectionUiEvent()
    data object NavigateToConfirmation : ToolSelectionUiEvent()
    data class ShowDatePicker(val currentDate: LocalDate) : ToolSelectionUiEvent()
}
