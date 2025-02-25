package com.miassolutions.rentatool.ui.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.miassolutions.rentatool.core.utils.mockdb.getMockCustomers
import com.miassolutions.rentatool.core.utils.mockdb.getMockTools
import com.miassolutions.rentatool.data.ToolRentalRepository
import com.miassolutions.rentatool.data.model.Customer
import com.miassolutions.rentatool.data.model.Tool
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

class SharedViewModel(private val repository: ToolRentalRepository) : ViewModel() {

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

    // Expose LiveData to the UI (Fragment/Activity)
    val getAllTools: Flow<List<Tool>> = repository.getAllTools()


    private val _searchCustomerQuery = MutableStateFlow("")
    private val searchCustomerQuery = _searchCustomerQuery.asStateFlow()


    @OptIn(FlowPreview::class, ExperimentalCoroutinesApi::class)
    val customerSearchResult: Flow<List<Customer>> = _searchCustomerQuery
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
    val toolSearchResult: Flow<List<Tool>> = _searchToolQuery
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

    private val _customer = MutableLiveData<Customer?>()
    val customer: LiveData<Customer?> get() = _customer


    fun getCustomerById(customerId: Long) {
        viewModelScope.launch {
            try {
                val result = repository.getCustomerById(customerId)
                _customer.value = result
            } catch (e: Exception) {
                _customer.value = null
            }

        }
    }

    private val _tool = MutableLiveData<Tool?>()
    val tool: LiveData<Tool?> get() = _tool

    private val _estimatedReturnDate = MutableLiveData<Long>()
    val estimatedReturnDate: LiveData<Long> = _estimatedReturnDate

    fun setEstimatedReturnDate(date: Long) {
        _estimatedReturnDate.value = date
    }


    // Add tool
    fun addTool(tool: Tool) {
        viewModelScope.launch(Dispatchers.IO) {
            val result = repository.insertTool(tool)

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
    fun addCustomer(customer: Customer) {
        viewModelScope.launch(Dispatchers.IO) {
            val result = repository.insertCustomer(customer)

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
    fun updateCustomer(updatedCustomer: Customer) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.updateCustomer(updatedCustomer)
        }
    }

}
