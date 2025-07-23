package com.miassolutions.rentatool.ui.fragments.toolselection

import com.miassolutions.rentatool.data.entities.ToolEntity
import java.time.LocalDate

data class ToolSelectionUiState(
    val customerId: Long? = null,
    val allTools: List<ToolEntity> = emptyList(),
    val filteredTools: List<ToolEntity> = emptyList(),
    val selectedTools: Map<Long, Int> = emptyMap(), // toolId -> quantity
    val estimatedReturnDate: LocalDate? = null,
    val isSubmitting: Boolean = false,
    val errorMessage: String? = null,
    val rentalSuccess: Boolean = false

)




sealed class ToolSelectionUiEvent {
    data class OnToolSelectionChanged(val tool: ToolEntity, val quantity: Int, val isChecked: Boolean) : ToolSelectionUiEvent()
    data class OnSearchQueryChanged(val query: String) : ToolSelectionUiEvent()
    data class OnEstimatedDateSelected(val date: LocalDate) : ToolSelectionUiEvent()
    data object OnSubmitRental : ToolSelectionUiEvent()
    data class ShowError(val message: String) : ToolSelectionUiEvent()
    data object RentalCompleted : ToolSelectionUiEvent()
}