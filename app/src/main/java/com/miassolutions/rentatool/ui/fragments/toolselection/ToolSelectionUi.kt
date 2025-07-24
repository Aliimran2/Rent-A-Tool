package com.miassolutions.rentatool.ui.fragments.toolselection

import com.miassolutions.rentatool.data.entities.ToolEntity
import java.time.LocalDate

data class ToolSelectionUiState(
    val customerId: Long = -1L,
    val customerName: String = "",
    val estimatedReturnDate: LocalDate? = null,
    val searchQuery: String = "",
    val tools: List<ToolSelectionItem> = emptyList(),
    val isSubmitting: Boolean = false,
    val errorMessage: String? = null,
)

data class ToolSelectionItem(
    val toolId : Long,
    val toolName : String,
    val availableQuantity : Int,
    val isSelected : Boolean = false,
    val selectedQuantity : String = "",
    val inputError : String? = null
)




sealed class ToolSelectionUiEvent {
    data class OnToolSelectionChanged(val tool: ToolEntity, val quantity: Int, val isChecked: Boolean) : ToolSelectionUiEvent()
    data class OnSearchQueryChanged(val query: String) : ToolSelectionUiEvent()
    data class OnEstimatedDateSelected(val date: LocalDate) : ToolSelectionUiEvent()
    data object OnSubmitRental : ToolSelectionUiEvent()
    data class ShowError(val message: String) : ToolSelectionUiEvent()
    data object RentalCompleted : ToolSelectionUiEvent()
}