package com.miassolutions.rentatool.ui.viewmodels

import android.util.Log
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

    private val _customer = MutableLiveData<Customer?>()
    val customer: LiveData<Customer?> get() = _customer

    private val _tool = MutableLiveData<Tool?>()
    val tool: LiveData<Tool?> get() = _tool

    private val _estimatedReturnDate = MutableLiveData<Long>()
    val estimatedReturnDate: LiveData<Long> = _estimatedReturnDate

    fun setEstimatedReturnDate(date: Long) {
        _estimatedReturnDate.value = date
    }

    //lists
    private val _tools = MutableLiveData<List<Tool>>()
    val tools: LiveData<List<Tool>> = _tools

    private val _customers = MutableLiveData<List<Customer>>()
    val customers: LiveData<List<Customer>> get() = _customers

    private val _rentals = MutableLiveData<List<Rental>>()
    val rentals: LiveData<List<Rental>> get() = _rentals

    private val _rentalDetails = MutableLiveData<List<RentalDetail>>()
    val rentalDetails: LiveData<List<RentalDetail>> get() = _rentalDetails

    private val _selectedTools = MutableLiveData<List<Pair<Long, Int>>>()
    val selectedTools: LiveData<List<Pair<Long, Int>>> get() = _selectedTools

    fun setSelectedTools(tools: List<Pair<Long, Int>>) {
        _selectedTools.value = tools
    }

    fun fetchAllTools() {
        viewModelScope.launch(Dispatchers.IO) {
            val toolsList = repository.getAllTools()
            withContext(Dispatchers.Main) {
                _tools.value = toolsList
            }
        }
    }

    init {
        fetchAllCustomers()
        fetchAllTools()
        fetchAllRentals()
        fetchAllRentalDetails()
    }

    fun fetchAllCustomers() {
        viewModelScope.launch(Dispatchers.IO) {
            val customersList = repository.getAllCustomers()
            withContext(Dispatchers.Main) {
                _customers.value = customersList
            }
        }
    }

    // Fetch all rentals
    fun fetchAllRentals() {
        viewModelScope.launch(Dispatchers.IO) {
            val rentalsList = repository.searchRentalsByCustomer(0) // Replace 0 with a valid customerId if needed
            withContext(Dispatchers.Main) {
                _rentals.value = rentalsList
            }
        }
    }

    // Fetch all rental details
    fun fetchAllRentalDetails() {
        viewModelScope.launch(Dispatchers.IO) {
            val rentalDetailsList = repository.searchRentalDetailsByRental(0) // Replace 0 with a valid rentalId if needed
            withContext(Dispatchers.Main) {
                _rentalDetails.value = rentalDetailsList
            }
        }
    }

    // search
    fun searchToolByName(name: String) {
        viewModelScope.launch(Dispatchers.IO) {
            val toolsList = repository.searchToolsByName(name)
            withContext(Dispatchers.Main) {
                _tools.value = toolsList
            }
        }
    }

    fun searchCustomerByName(name: String) {
        viewModelScope.launch(Dispatchers.IO) {
            val customersList = repository.searchCustomerByName(name)
            withContext(Dispatchers.Main) {
                _customers.value = customersList
            }
        }
    }

    fun searchRentalsByCustomer(customerId: Long) {
        viewModelScope.launch(Dispatchers.IO) {
            val rentalsList = repository.searchRentalsByCustomer(customerId)
            withContext(Dispatchers.Main) {
                _rentals.value = rentalsList
            }
        }
    }

    fun searchRentalDetailsByRental(rentalId: Long) {
        viewModelScope.launch(Dispatchers.IO) {
            val rentalDetailsList = repository.searchRentalDetailsByRental(rentalId)
            withContext(Dispatchers.Main) {
                _rentalDetails.value = rentalDetailsList
            }
        }
    }

    fun setCustomer(customer: Customer) {
        _customer.value = customer
    }

    fun getCustomerById(customerId: Long) {
        viewModelScope.launch(Dispatchers.IO) {
            val customerResult = repository.getCustomerById(customerId)
            withContext(Dispatchers.Main) {
                _customer.value = customerResult
            }
        }
    }

    fun getToolById(toolId: Long) {
        viewModelScope.launch(Dispatchers.IO) {
            val toolResult = repository.getToolById(toolId)
            withContext(Dispatchers.Main) {
                _tool.value = toolResult
            }
        }
    }

    private val _toastMessage = MutableLiveData<String?>()
    val toastMessage: LiveData<String?> get() = _toastMessage

    fun addTool(tool: Tool) {
        viewModelScope.launch(Dispatchers.IO) {
            val result = repository.addTool(tool)

            withContext(Dispatchers.Main) {
                result.onSuccess {
                    _toastMessage.value = "Tool added successfully!"
                }

                result.onFailure { exception ->
                    _toastMessage.value = "Error adding tool: ${exception.message}"
                }

                // Refresh tool list
                fetchAllTools()
            }
        }
    }

    fun updateTool(tool: Tool) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.updateTool(tool)
            fetchAllTools()
        }
    }

    fun addCustomer(customer: Customer) {
        viewModelScope.launch(Dispatchers.IO) {
            val result = repository.addCustomer(customer)

            withContext(Dispatchers.Main) {
                result.onSuccess {
                    _toastMessage.value = "Customer Added Successfully"
                }
                result.onFailure {
                    _toastMessage.value = "Error adding customer : ${it.message}"
                }
            }
            fetchAllCustomers()
        }
    }

    fun updateCustomer(updatedCustomer: Customer) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.updateCustomer(updatedCustomer)
            fetchAllCustomers() // Refresh customers
        }
    }

    // Add a rental
    fun addRental(customerId: Long, toolRentals: List<Pair<Long, Int>>, rentalDate: Long) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                // Start a transaction
                val rentalId = repository.addRental(
                    Rental(
                        customerId = customerId,
                        rentalDate = rentalDate,
                        returnDate = null
                    )
                )

                // Check if the rental was successfully added
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

                            // Log values before updating
                            Log.d("MyViewModel","Updating tool ID: $toolId, New Available Stock: $newAvailableStock, New Rented Quantity: $newRentedQuantity")

                            // Update stock in the database
                            repository.updateToolStock(toolId, newAvailableStock, newRentedQuantity)

                            // Verify changes by re-querying the tool (optional for debugging)
                            val updatedTool = repository.getToolById(toolId)
                            println("Updated Tool: $updatedTool")

                            // Add rental details
                            repository.addRentalDetail(
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

                    // Refresh data
                    fetchAllTools()
                    fetchAllRentals()
                    fetchAllRentalDetails()
                } ?: throw IllegalArgumentException("Failed to create rental")

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
                        rent = (returnDate - rentalDetail.rentalDate) / (24 * 60 * 60 * 1000) * tool.rentPerDay * returnQuantity
                        rentalDetail.quantity -= returnQuantity
                        tool.availableStock += returnQuantity
                        fetchAllRentalDetails() // Refresh rental details
                        fetchAllTools() // Refresh tools
                    }
                }
            } catch (e: Exception) {
                // Handle error if needed
            }
        }
        return rent
    }

}
