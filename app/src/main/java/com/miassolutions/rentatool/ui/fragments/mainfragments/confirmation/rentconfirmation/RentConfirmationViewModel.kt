package com.miassolutions.rentatool.ui.fragments.mainfragments.confirmation.rentconfirmation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.miassolutions.rentatool.data.repositoryimpl.RentalRepositoryImpl
import com.miassolutions.rentatool.ui.fragments.mainfragments.toolselection.SelectedTool
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.temporal.ChronoUnit
import javax.inject.Inject

@HiltViewModel
class ConfirmationViewModel @Inject constructor(
    private val repository: RentalRepositoryImpl
) : ViewModel() {

    private val _uiState = MutableStateFlow(ConfirmationUiState())
    val uiState: StateFlow<ConfirmationUiState> = _uiState.asStateFlow()

    private val _uiEvent = MutableSharedFlow<ConfirmationUiEvent>()
    val uiEvent: SharedFlow<ConfirmationUiEvent> = _uiEvent.asSharedFlow()

    fun initialize(selectedToolsJson: String, estReturnDate: LocalDate) {
        val tools: List<SelectedTool> = Gson().fromJson(
            selectedToolsJson,
            object : TypeToken<List<SelectedTool>>() {}.type
        )

        val days = ChronoUnit.DAYS.between(LocalDate.now(), estReturnDate)
            .toInt().coerceAtLeast(1)

        val total = tools.sumOf { it.quantity * it.rentPricePerDay * days }

        _uiState.update {
            it.copy(
                selectedTools = tools,
                days = days,
                totalAmount = total
            )
        }
    }

    fun confirmRental(customerId: Long) {
        val state = _uiState.value

        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }

            try {
                repository.performRentalTransaction(customerId, state.selectedTools)

                _uiEvent.emit(ConfirmationUiEvent.ShowToast("Rental confirmed!"))
                _uiEvent.emit(ConfirmationUiEvent.NavigateBack)

            } catch (e: Exception) {
                _uiEvent.emit(ConfirmationUiEvent.ShowToast("Error: ${e.message ?: "Unknown error"}"))
            } finally {
                _uiState.update { it.copy(isLoading = false) }
            }
        }
    }
}
