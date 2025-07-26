package com.miassolutions.rentatool.ui.fragments.confirmation.rentconfirmation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.miassolutions.rentatool.data.entities.RentalOrderEntity
import com.miassolutions.rentatool.data.entities.RentedToolEntity
import com.miassolutions.rentatool.data.repository.RentalRepository
import com.miassolutions.rentatool.data.repository.RentedToolRepository
import com.miassolutions.rentatool.data.repositoryimpl.RentalOrderRepositoryImpl
import com.miassolutions.rentatool.data.repositoryimpl.RentalRepositoryImpl
import com.miassolutions.rentatool.ui.fragments.toolselection.SelectedTool
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
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
        val days = ChronoUnit.DAYS.between(LocalDate.now(), estReturnDate).toInt().coerceAtLeast(1)
        val total = tools.sumOf { it.quantity * it.rentPricePerDay * days }

        _uiState.update {
            it.copy(
                selectedTools = tools,
                estimatedReturnDate = estReturnDate,
                days = days,
                totalAmount = total
            )
        }
    }

    fun confirmRental(customerId: Long) {
        val state = _uiState.value
        val estDate = state.estimatedReturnDate ?: return

        viewModelScope.launch {
            try {
                _uiState.update { it.copy(isLoading = true) }

                val order = RentalOrderEntity(
                    customerId = customerId,
                    totalAmount = state.totalAmount
                )

                val rentedTools = state.selectedTools.map {
                    SelectedTool(
                        toolId = it.toolId,
                        rentPricePerDay = it.rentPricePerDay,
                        toolName = it.toolName,
                        quantity = it.quantity
                    )
                }

                repository.rentToolsToCustomer(customerId, rentedTools)

                _uiEvent.emit(ConfirmationUiEvent.ShowToast("Rental confirmed!"))
                _uiEvent.emit(ConfirmationUiEvent.NavigateBack)

            } catch (e: Exception) {
                _uiEvent.emit(ConfirmationUiEvent.ShowToast("Error: ${e.message}"))
            } finally {
                _uiState.update { it.copy(isLoading = false) }
            }
        }
    }
}