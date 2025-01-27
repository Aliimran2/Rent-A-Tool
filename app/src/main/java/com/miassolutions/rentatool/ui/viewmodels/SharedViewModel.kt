package com.miassolutions.rentatool.ui.viewmodels

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.miassolutions.rentatool.core.utils.mockdb.getMockCustomers
import com.miassolutions.rentatool.core.utils.mockdb.getMockTools
import com.miassolutions.rentatool.data.ToolRentalRepository
import com.miassolutions.rentatool.data.model.Customer
import com.miassolutions.rentatool.data.model.Rental
import com.miassolutions.rentatool.data.model.RentalDetail
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


    fun getAllRentals(): LiveData<List<Rental>> = repository.getAllRentals()

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


    // Function to observe rentals by customerId
    fun rentalsByCustomer(customerId: Long): LiveData<List<Rental>> =
        repository.rentalsByCustomer(customerId)

    // Function to observe rental details by rentalId
    fun rentalDetailsByRental(rentalId: Long): LiveData<List<RentalDetail>> =
        repository.rentalDetailsByRental(rentalId)

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


    // Add rental
//    fun addRental(customerId: Long, toolRentals: List<Pair<Long, Int>>, rentalDate: Long, estReturnDate: Long) {
//        viewModelScope.launch(Dispatchers.IO) {
//            try {
//                // Start a transaction
//                val rentalById = repository.insertRental(
//                    Rental(
//                        customerId = customerId,
//                        rentalDate = rentalDate,
//                        estReturnDate = estReturnDate,
//                        returnDate = null
//                    )
//                )
//
//                // Process each tool rental
//                rentalById.let { rentalId ->
//                    toolRentals.forEach { (toolId, quantity) ->
//                        val tool = repository.getToolByIdDirect(toolId)
//                        if (tool != null) {
//                            if (tool.availableStock < quantity) {
//                                throw IllegalArgumentException("Insufficient stock for tool: ${tool.name}")
//                            }
//
//                            // Update stock values
//                            val newAvailableStock = tool.availableStock - quantity
//                            val newRentedQuantity = tool.rentedQuantity + quantity
//
//                            // Update stock in the database
//                            repository.updateToolStock(toolId, newAvailableStock, newRentedQuantity)
//
//                            // Add rental details
//                            repository.insertRentalDetails(
//                                RentalDetail(
//                                    rentalId = rentalId,
//                                    toolId = toolId,
//                                    quantity = quantity,
//                                    rentPerDay = tool.rentPerDay,
//                                    rentalDate = rentalDate,
//                                    returnDate = null
//                                )
//                            )
//                        } else {
//                            throw IllegalArgumentException("Tool not found with ID: $toolId")
//                        }
//                    }
//                }
//
//            } catch (e: Exception) {
//                e.printStackTrace()
//            }
//        }
//    }


    private val _rentResult = MutableLiveData<Double?>()
    val rentResult: LiveData<Double?> get() = _rentResult

    private val _returnStatus = MutableLiveData<String>()
    val returnStatus: LiveData<String> get() = _returnStatus

    // Call the repository method to handle the tool return process
    fun returnTool(rentalDetailId: Long, returnQuantity: Int, returnDate: Long) {
        viewModelScope.launch {
            try {
                repository.returnTool(rentalDetailId, returnQuantity, returnDate)
                _returnStatus.postValue("Tool returned successfully.")
            } catch (e: Exception) {
                _returnStatus.postValue("Error: ${e.message}")
            }
        }
    }




    // Return tools
//    fun returnTool(rentalDetailId: Long, returnQuantity: Int, returnDate: Long) {
//        viewModelScope.launch {
//            try {
//                // Fetch rental detail by ID
//                val rentalDetail = repository.getRentalDetailById(rentalDetailId)
//                Log.d("ReturnTool", "Rental Detail: $rentalDetail")
//
//                if (rentalDetail != null) {
//                    // Get the associated tool
//                    val tool = repository.getToolByIdDirect(rentalDetail.toolId)
//                    Log.d("ReturnTool", "Tool: $tool")
//
//                    if (tool != null) {
//                        // Calculate the rent for the returned quantity
//                        val rent =
//                            (returnDate - rentalDetail.rentalDate) / (24 * 60 * 60 * 1000) * tool.rentPerDay * returnQuantity
//                        Log.d("ReturnTool", "Calculated Rent: $rent")
//
//                        // Update the rental and tool data
//                        rentalDetail.quantity -= returnQuantity
//                        tool.availableStock += returnQuantity
//
//                        // Fetch the customer and update their total rent
//                        val customerId = rentalDetail.rentalId // Assuming you have rentalId linked to Customer
//                        val customer = repository.getCustomerById(customerId)  // Assuming you can fetch customer by ID
//                        if (customer != null) {
//                            customer.totalRent += rent  // Add to the customer's accumulated rent
//                            repository.updateCustomer(customer)  // Update the customer in the database
//                        }
//
//                        // Update the rental and tool in the database
//                        repository.updateRentalDetail(rentalDetail)
//                        repository.updateTool(tool)
//
//                        // Post the result
//                        _rentResult.postValue(rent)
//                    } else {
//                        Log.e("ReturnTool", "Tool not found for toolId: ${rentalDetail.toolId}")
//                    }
//                } else {
//                    Log.e("ReturnTool", "Rental detail not found for rentalDetailId: $rentalDetailId")
//                }
//            } catch (e: Exception) {
//                Log.e("ReturnTool", "Exception: ${e.message}", e)
//                _rentResult.postValue(null) // Handle failure case
//            }
//        }
//    }

}
