package com.miassolutions.rentatool.ui.fragments.mainfragments.confirmation.rentconfirmation

import com.miassolutions.rentatool.ui.fragments.mainfragments.toolselection.SelectedTool

data class ConfirmationUiState(
    val selectedTools: List<SelectedTool> = emptyList(),
    val totalAmount: Double = 0.0,
    val days: Int = 1,
    val isLoading: Boolean = false,
)



sealed class ConfirmationUiEvent {
    data class ShowToast(val message: String) : ConfirmationUiEvent()
    data object NavigateBack : ConfirmationUiEvent()
}
