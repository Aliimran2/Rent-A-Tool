package com.miassolutions.rentatool.ui.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.miassolutions.rentatool.data.ToolRentalRepository
import com.miassolutions.rentatool.data.model.Customer
import com.miassolutions.rentatool.data.model.Rental
import com.miassolutions.rentatool.data.model.RentalDetail
import com.miassolutions.rentatool.data.model.Tool
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class SharedViewModel(private val repository: ToolRentalRepository) : ViewModel() {

    fun checkToExists(toolName: String): LiveData<Boolean> {
        val result = MutableLiveData<Boolean>()
        viewModelScope.launch {
            val exists = repository.isToolExists(toolName)
            result.postValue(exists)
        }
        return result
    }

    // Expose LiveData to the UI (Fragment/Activity)
    val allTools: LiveData<List<Tool>> = repository.getAllTools()
    val allCustomers: LiveData<List<Customer>> = repository.getAllCustomers()


    // Function to observe rentals by customerId
    fun searchRentalsByCustomer(customerId: Long): LiveData<List<Rental>> =
        repository.searchRentalsByCustomer(customerId)

    // Function to observe rental details by rentalId
    fun searchRentalDetailsByRental(rentalId: Long): LiveData<List<RentalDetail>> =
        repository.searchRentalDetailsByRental(rentalId)

    // Toast message for success or error
    private val _toastMessage = MutableLiveData<String?>()
    val toastMessage: LiveData<String?> get() = _toastMessage

    private val _customer = MutableLiveData<Customer?>()
    val customer: LiveData<Customer?> get() = _customer

    fun getCustomerById(customerId: Long) {
        viewModelScope.launch(Dispatchers.IO) {
            val customerData = repository.getCustomerById(customerId)
            withContext(Dispatchers.Main) {

                _customer.value = customerData
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

    fun setCustomer(customer: Customer?) {
        _customer.value = customer
    }

    // MutableLiveData for selected tools
    private val _selectedTools = MutableLiveData<List<Pair<Long, Int>>>()
    val selectedTools: LiveData<List<Pair<Long, Int>>> get() = _selectedTools

    fun setSelectedTools(tools: List<Pair<Long, Int>>) {
        _selectedTools.value = tools
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

    // Update tool
    fun updateTool(tool: Tool) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.updateTool(tool)
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

    // Add rental
    fun addRental(customerId: Long, toolRentals: List<Pair<Long, Int>>, rentalDate: Long) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                // Start a transaction
                val rentalId = repository.insertRental(
                    Rental(
                        customerId = customerId,
                        rentalDate = rentalDate,
                        returnDate = null
                    )
                )

                // Process each tool rental
                rentalId?.let { rentalId ->
                    toolRentals.forEach { (toolId, quantity) ->
                        val tool = repository.getToolById(toolId)
                        if (tool != null) {
                            if (tool.availableStock < quantity) {
                                throw IllegalArgumentException("Insufficient stock for tool: ${tool.name}")
                            }

                            // Update stock values
                            val newAvailableStock = tool.availableStock - quantity
                            val newRentedQuantity = tool.rentedQuantity + quantity

                            // Update stock in the database
                            repository.updateToolStock(toolId, newAvailableStock, newRentedQuantity)

                            // Add rental details
                            repository.insertRentalDetails(
                                RentalDetail(
                                    rentalId = rentalId,
                                    toolId = toolId,
                                    quantity = quantity,
                                    rentPerDay = tool.rentPerDay,
                                    rentalDate = rentalDate,
                                    returnDate = null
                                )
                            )
                        } else {
                            throw IllegalArgumentException("Tool not found with ID: $toolId")
                        }
                    }
                }

            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    // Return tools
    fun returnTool(rentalDetailId: Long, returnQuantity: Int, returnDate: Long): Double? {
        var rent: Double? = null
        viewModelScope.launch {
            try {
                val rentalDetail = repository.getRentalDetailById(rentalDetailId)
                if (rentalDetail != null) {
                    val tool = repository.getToolById(rentalDetail.toolId)
                    if (tool != null) {
                        rent =
                            (returnDate - rentalDetail.rentalDate) / (24 * 60 * 60 * 1000) * tool.rentPerDay * returnQuantity
                        rentalDetail.quantity -= returnQuantity
                        tool.availableStock += returnQuantity
                    }
                }
            } catch (e: Exception) {
                // Handle error if needed
            }
        }
        return rent
    }
}
