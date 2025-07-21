package com.miassolutions.rentatool.ui.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.miassolutions.rentatool.core.utils.mockdb.getMockCustomers
import com.miassolutions.rentatool.core.utils.mockdb.getMockTools
import com.miassolutions.rentatool.data.repository.ToolRentalRepository
import com.miassolutions.rentatool.data.model.CustomerEntity
import com.miassolutions.rentatool.data.model.ToolEntity
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(private val repository: ToolRentalRepository) : ViewModel() {

    private val _selectedTools = MutableLiveData<Map<Long, Int>>()
    val selectedTools: LiveData<Map<Long, Int>> = _selectedTools

    fun updatedSelectedTools(selected: Map<Long, Int>) {
        _selectedTools.value = selected


    }

    fun checkToExists(toolName: String): LiveData<Boolean> {
        val result = MutableLiveData<Boolean>()
        viewModelScope.launch {
            val exists = repository.isToolExists(toolName)
            result.postValue(exists)
        }
        return result
    }

    //will be deleted later todo()
    fun insertMockCustomers() {
        viewModelScope.launch {
            val mockCustomers = getMockCustomers()
            repository.insertCustomers(mockCustomers)
        }
    }

    fun insertMockTools() {
        viewModelScope.launch {
            val mockTools = getMockTools()
            repository.insertTools(mockTools)
        }
    }




    private val _searchCustomerQuery = MutableStateFlow("")
    private val searchCustomerQuery = _searchCustomerQuery.asStateFlow()


    @OptIn(FlowPreview::class, ExperimentalCoroutinesApi::class)
    val customerEntitySearchResult: Flow<List<CustomerEntity>> = _searchCustomerQuery
        .debounce(300)
        .distinctUntilChanged()
        .flatMapLatest { query ->
            if (query.isBlank()) {
                repository.getAllCustomers()
            } else {
                repository.searchCustomer(query)
            }
        }.stateIn(viewModelScope, SharingStarted.Lazily, emptyList())


    fun searchCustomer(query: String) {
        _searchCustomerQuery.value = query
    }

    private val _searchToolQuery = MutableStateFlow("")
    val searchToolQuery = _searchToolQuery.asStateFlow()

    @OptIn(FlowPreview::class, ExperimentalCoroutinesApi::class)
    val toolEntitySearchResult: Flow<List<ToolEntity>> = _searchToolQuery
        .debounce(300)
        .distinctUntilChanged()
        .flatMapLatest { query ->
            if (query.isEmpty()) {
                repository.getAllTools()
            } else {
                repository.searchTool(query)
            }
        }.stateIn(viewModelScope, SharingStarted.Lazily, emptyList())

    fun searchTool(query: String){
        _searchToolQuery.value = query
    }


    // Toast message for success or error
    private val _toastMessage = MutableLiveData<String?>()
    val toastMessage: LiveData<String?> get() = _toastMessage

    private val _customerEntity = MutableLiveData<CustomerEntity?>()
    val customerEntity: LiveData<CustomerEntity?> get() = _customerEntity


    fun getCustomerById(customerId: Long) {
        viewModelScope.launch {
            try {
                val result = repository.getCustomerById(customerId)
                _customerEntity.value = result
            } catch (e: Exception) {
                _customerEntity.value = null
            }

        }
    }

    private val _toolEntity = MutableLiveData<ToolEntity?>()
    val toolEntity: LiveData<ToolEntity?> get() = _toolEntity

    private val _estimatedReturnDate = MutableLiveData<Long>()
    val estimatedReturnDate: LiveData<Long> = _estimatedReturnDate

    fun setEstimatedReturnDate(date: Long) {
        _estimatedReturnDate.value = date
    }


    // Add tool
    fun addTool(toolEntity: ToolEntity) {
        viewModelScope.launch(Dispatchers.IO) {
            val result = repository.insertTool(toolEntity)

            withContext(Dispatchers.Main) {
                result.onSuccess {
                    _toastMessage.value = "Tool added successfully!"
                }

                result.onFailure { exception ->
                    _toastMessage.value = "Error adding tool: ${exception.message}"
                }
            }
        }
    }


    // Add customer
    fun addCustomer(customerEntity: CustomerEntity) {
        viewModelScope.launch(Dispatchers.IO) {
            val result = repository.insertCustomer(customerEntity)

            withContext(Dispatchers.Main) {
                result.onSuccess {
                    _toastMessage.value = "Customer Added Successfully"
                }
                result.onFailure {
                    _toastMessage.value = "Error adding customer : ${it.message}"
                }
            }
        }
    }


    // Update customer
    fun updateCustomer(updatedCustomerEntity: CustomerEntity) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.updateCustomer(updatedCustomerEntity)
        }
    }

}
