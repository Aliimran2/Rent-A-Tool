package com.miassolutions.rentatool.ui.fragments.toolselection

import com.miassolutions.rentatool.data.entities.ToolEntity
import java.time.LocalDate

data class ToolSelectionUiState(
    val tools: List<ToolItemUiModel> = emptyList(),
    val estimatedReturnDate: String = "",
    val isSubmitting: Boolean = false,
    val errorMessage: String? = null
)

data class ToolItemUiModel(
    val toolId: Long,
    val name: String,
    val availableQuantity: Int,
    val isSelected: Boolean = false,
    val selectedQuantity: String = ""
)

sealed class ToolSelectionUiEvent {
    data class OnQuantityChanged(val toolId: Long, val quantity: String) : ToolSelectionUiEvent()
    data class OnToolChecked(val toolId: Long, val checked: Boolean) : ToolSelectionUiEvent()
    data class OnEstimatedDateChanged(val date: String) : ToolSelectionUiEvent()
    object OnSubmit : ToolSelectionUiEvent()
}