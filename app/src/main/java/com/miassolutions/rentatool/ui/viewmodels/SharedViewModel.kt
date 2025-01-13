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
import kotlinx.coroutines.launch

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

    fun setSelectedTools(tools : List<Pair<Long, Int>>) {
        _selectedTools.value = tools
    }

    fun fetchAllTools() {
        viewModelScope.launch {
            _tools.value = repository.getAllTools()
        }
    }

    fun fetchAllCustomers() {
        viewModelScope.launch {
            _customers.value = repository.getAllCustomers()
        }
    }

    // Fetch all rentals
    fun fetchAllRentals() {
        viewModelScope.launch {
            _rentals.value = repository.searchRentalsByCustomer(0) // Replace 0 with a valid customerId if needed
        }
    }

    // Fetch all rental details
    fun fetchAllRentalDetails() {
        viewModelScope.launch {
            _rentalDetails.value = repository.searchRentalDetailsByRental(0) // Replace 0 with a valid rentalId if needed
        }
    }

    // search
    fun searchToolByName(name:String){
        viewModelScope.launch {
            _tools.value = repository.searchToolsByName(name)
        }
    }

    fun searchCustomerByName(name: String){
        viewModelScope.launch {
            _customers.value = repository.searchCustomerByName(name)
        }
    }

    fun searchRentalsByCustomer(customerId : Long){
        viewModelScope.launch {
            _rentals.value = repository.searchRentalsByCustomer(customerId)
        }
    }

    fun searchRentalDetailsByRental(rentalId : Long){
        viewModelScope.launch {
            _rentalDetails.value = repository.searchRentalDetailsByRental(rentalId)
        }
    }


    fun getCustomerById(customerId: Long){
        viewModelScope.launch {
            _customer.value = repository.getCustomerById(customerId)
        }
    }

    fun getToolById(toolId : Long){
        viewModelScope.launch {
            _tool.value = repository.getToolById(toolId)
        }
    }

    private val _toastMessage = MutableLiveData<String?>()
    val toastMessage: LiveData<String?> get() = _toastMessage

    fun addTool(tool: Tool) {
        viewModelScope.launch {
            val result = repository.addTool(tool)

            result.onSuccess {
                _toastMessage.postValue("Tool added successfully!")
            }

            result.onFailure { exception ->
                _toastMessage.postValue("Error adding tool: ${exception.message}")
            }

            // Refresh tool list
            fetchAllTools()
        }
    }

    fun updateTool(tool: Tool){
        viewModelScope.launch {
            repository.updateTool(tool)
            fetchAllTools()
        }
    }

    fun addCustomer(customer: Customer){
        viewModelScope.launch {
            val result = repository.addCustomer(customer)
            result.onSuccess {
                _toastMessage.postValue("Customer Added Successfully")
            }
            result.onFailure {
                _toastMessage.postValue("Error adding customer : ${it.message}")
            }
        }
    }

    fun updateCustomer(updatedCustomer:Customer){
        viewModelScope.launch {
            repository.updateCustomer(updatedCustomer)
            fetchAllCustomers() // Refresh customers
        }
    }

    // Add a rental
    fun addRental(customerId: Long, toolRentals: List<Pair<Long, Int>>, rentalDate: Long) {
        viewModelScope.launch {
            try {
                // Add rental and get the generated Rental object
                val rental = repository.addRental(
                    Rental(
                        rentalId = 0L, // Auto-generate ID
                        customerId = customerId,
                        rentalDate = rentalDate,
                        returnDate = null
                    )
                )

                rental?.let {
                    toolRentals.forEach { (toolId, quantity) ->
                        val tool = repository.getToolById(toolId)
                        if (tool != null) {
                            repository.addRentalDetail(
                                RentalDetail(
                                    rentalDetailId = 0, // Auto-generate ID
                                    rentalId = it,
                                    toolId = toolId,
                                    quantity = quantity,
                                    rentPerDay = tool.rentPerDay,
                                    rentalDate = rentalDate,
                                    returnDate = null
                                )
                            )
                        }
                    }
                }

                // Refresh data
                fetchAllRentals()
                fetchAllRentalDetails()
            } catch (e: Exception) {
                // Handle the exception (e.g., log it or show an error message)
                e.printStackTrace()
            }
        }
    }

    // Return tools
    fun returnTool(rentalDetailId: Long, returnQuantity: Int, returnDate: Long): Double? {
        return try {
            val rentalDetail = repository.getRentalDetailById(rentalDetailId)
            if (rentalDetail != null) {
                val tool = repository.getToolById(rentalDetail.toolId)
                if (tool != null) {
                    val rent = (returnDate - rentalDetail.rentalDate) / (24 * 60 * 60 * 1000) * tool.rentPerDay * returnQuantity
                    rentalDetail.quantity -= returnQuantity
                    tool.availableStock += returnQuantity
                    fetchAllRentalDetails() // Refresh rental details
                    fetchAllTools() // Refresh tools
                    rent
                } else null
            } else null
        } catch (e: Exception) {
            null
        }
    }




}