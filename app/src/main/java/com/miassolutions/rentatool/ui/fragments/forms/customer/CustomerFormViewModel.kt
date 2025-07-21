package com.miassolutions.rentatool.ui.fragments.forms.customer

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.miassolutions.rentatool.Constants
import com.miassolutions.rentatool.data.model.CustomerEntity
import com.miassolutions.rentatool.data.repository.Repository
import com.miassolutions.rentatool.uimodels.CustomerFormResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CustomerFormViewModel @Inject constructor(private val repository: Repository) :
    ViewModel() {

    private val _uiState = MutableStateFlow(CustomerFormUiState())
    val uiState = _uiState.asStateFlow()

    private val _uiEvent = MutableSharedFlow<CustomerUiEvent>()
    val uiEvent = _uiEvent.asSharedFlow()


    fun onCustomerNameChange(customerName: String) {
        _uiState.update { it.copy(customerName = customerName) }
    }

    fun onCnicChange(cnic: String) {
        _uiState.update { it.copy(customerCnic = cnic) }
    }

    fun onContractorNameChange(contractorName: String) {
        _uiState.update { it.copy(contractorName = contractorName) }
    }

    fun onOwnerNameChange(ownerName: String) {
        _uiState.update { it.copy(ownerName = ownerName) }
    }

    fun onCustomerPhoneChange(customerPhone: String) {
        _uiState.update { it.copy(customerPhone = customerPhone) }
    }

    fun onContractorPhoneChange(contractorPhone: String) {
        _uiState.update { it.copy(contractorPhone = contractorPhone) }
    }

    fun onOwnerPhoneChange(ownerPhone: String) {
        _uiState.update { it.copy(ownerPhone = ownerPhone) }
    }

    fun onConstructionPlaceChange(constructionPlace: String) {
        _uiState.update { it.copy(constructionPlace = constructionPlace) }
    }


    fun onSaveClicked() {
        val state = _uiState.value

        val customerEntity = CustomerEntity(
            customerName = state.customerName,
            cnicNumber = state.customerCnic,
            customerPhone = state.customerPhone,
            constructionPlace = state.constructionPlace,
            contractorName = state.contractorName,
            contractorPhone = state.contractorPhone,
            ownerName = state.ownerName,
            ownerPhone = state.ownerPhone
        )

        viewModelScope.launch {
            val result = repository.insertCustomer(customerEntity)
            when (result) {
                is CustomerFormResult.Failure -> {
                    _uiEvent.emit(CustomerUiEvent.DuplicateCNIC(Constants.DUPLICATE_CNIC))
                }
                CustomerFormResult.Success -> _uiEvent.emit(CustomerUiEvent.CustomerAdded(1003))
            }

        }
    }

}