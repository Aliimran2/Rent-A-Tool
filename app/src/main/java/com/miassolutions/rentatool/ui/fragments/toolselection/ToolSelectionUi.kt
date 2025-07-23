package com.miassolutions.rentatool.ui.fragments.toolselection

import com.miassolutions.rentatool.data.entities.ToolEntity
import java.time.LocalDate

data class ToolSelectionUiState(
    val selectedTools : List<RentedTool> = emptyList(),
    val estimatedReturnDate : LocalDate? = null,
    val tools : List<ToolEntity> = emptyList(),
    val searchQuery : String = "",
    val isLoading : Boolean = false,
    val customerId : Long = -1L

)

data class RentedTool(
    val toolId : Long,
    val toolName : String,
    val quantity : Int
)


sealed class ToolSelectionUiEvent {
    data class ShowToast(val message : String) : ToolSelectionUiEvent()
    data object NavigateBack : ToolSelectionUiEvent()
    data object SuccessSelection : ToolSelectionUiEvent()
}