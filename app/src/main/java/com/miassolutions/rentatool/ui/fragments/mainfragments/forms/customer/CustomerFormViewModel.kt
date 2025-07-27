package com.miassolutions.rentatool.ui.fragments.mainfragments.forms.customer

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.miassolutions.rentatool.data.common.CustomerResult
import com.miassolutions.rentatool.data.entities.CustomerEntity
import com.miassolutions.rentatool.data.repository.MainRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CustomerFormViewModel @Inject constructor(private val repository: MainRepository) :
    ViewModel() {

    private val _uiState = MutableStateFlow(CustomerFormUiState())
    val uiState = _uiState.asStateFlow()

    private val _uiEvent = MutableSharedFlow<CustomerUiEvent>()
    val uiEvent = _uiEvent.asSharedFlow()

    fun onCnicChange(cnic: String) {
        _uiState.update { it.copy(customerCnic = cnic) }
    }


    fun onCustomerNameChange(customerName: String) {
        _uiState.update { it.copy(customerName = customerName) }
    }

    fun onCustomerPhoneChange(customerPhone: String) {
        _uiState.update { it.copy(customerPhone = customerPhone) }
    }


    private var saveAndExitClicked = false


    fun onSaveClicked(isSaveAndExit: Boolean = false) {
        saveAndExitClicked = isSaveAndExit

        val state = _uiState.value

        val customerEntity = CustomerEntity(
            customerName = state.customerName,
            cnicNumber = state.customerCnic,
            customerPhone = state.customerPhone,

            )

        viewModelScope.launch {
            when (val result = repository.insertCustomer(customerEntity)) {

                is CustomerResult.Failure -> {
                    _uiEvent.emit(CustomerUiEvent.ShowToast(result.message))
                }

                is CustomerResult.Success -> {
                    _uiEvent.emit(CustomerUiEvent.CustomerAdded(result.customerId))
                    if (isSaveAndExit) {
                        _uiEvent.emit(CustomerUiEvent.NavigateBack)
                    }
                }
            }

        }
    }

}