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
    val getAllTools: LiveData<List<Tool>> = repository.getAllTools()
    val getAllCustomers: LiveData<List<Customer>> = repository.getAllCustomers()

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
