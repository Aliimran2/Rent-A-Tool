package com.miassolutions.rentatool.ui.fragments.confirmation.rentconfirmation




import com.miassolutions.rentatool.ui.fragments.toolselection.SelectedTool
import java.time.LocalDate

data class ConfirmationUiState(
    val selectedTools: List<SelectedTool> = emptyList(),
    val estimatedReturnDate: LocalDate? = null,
    val totalAmount: Double = 0.0,
    val days: Int = 1,
    val isLoading: Boolean = false,
)



sealed class ConfirmationUiEvent {
    data class ShowToast(val message: String) : ConfirmationUiEvent()
    data object NavigateBack : ConfirmationUiEvent()
}
