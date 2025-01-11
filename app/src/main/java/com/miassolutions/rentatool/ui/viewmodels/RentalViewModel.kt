package com.miassolutions.rentatool.ui.viewmodels


import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.miassolutions.rentatool.data.model.Customer
import com.miassolutions.rentatool.data.model.Rental
import com.miassolutions.rentatool.data.model.RentalDetail
import com.miassolutions.rentatool.data.model.Tool
import com.miassolutions.rentatool.utils.mockdb.DataProvider


class RentalViewModel : ViewModel() {

    // MutableLiveData for tools, customers, rentals, and rental details
    private val _tools = MutableLiveData<List<Tool>>(DataProvider.tools)
    val tools: LiveData<List<Tool>> get() = _tools

    private val _customers = MutableLiveData<List<Customer>>(DataProvider.customers)
    val customers: LiveData<List<Customer>> get() = _customers

    // LiveData to store single customer or tool data
    private val _customer = MutableLiveData<Customer?>()
    val customer: LiveData<Customer?> get() = _customer

    private val _tool = MutableLiveData<Tool?>()
    val tool: LiveData<Tool?> get() = _tool

    // Function to get customer by ID
    fun getCustomerById(customerId: Long) {
        _customer.value = DataProvider.getCustomerById(customerId)
    }

    // Function to get tool by ID
    fun getToolById(toolId: Long) {
        _tool.value = DataProvider.getToolById(toolId)
    }



    private val _rentals = MutableLiveData<List<Rental>>(DataProvider.rentals)
    val rentals: LiveData<List<Rental>> get() = _rentals

    private val _rentalDetails = MutableLiveData<List<RentalDetail>>(DataProvider.rentalDetails)
    val rentalDetails: LiveData<List<RentalDetail>> get() = _rentalDetails

    fun addRental(customerId: Long, toolRentals: List<Pair<Long, Int>>, rentalDate: Long): Rental {
        val rental = DataProvider.rentTools(customerId, toolRentals, rentalDate)
        _rentals.value = DataProvider.rentals
        _rentalDetails.value = DataProvider.rentalDetails
        _tools.value = DataProvider.tools
        _customers.value = DataProvider.customers

        return rental
    }

    fun returnTool(rentalDetailId: Long, returnQuantity: Int, returnDate: Long) : Double {
        val rent = DataProvider.returnTools(rentalDetailId, returnQuantity, returnDate)
        _rentalDetails.value = DataProvider.rentalDetails
        _tools.value = DataProvider.tools
        return rent
    }


    /**
     * Adds a new tool.
     */
    fun addTool(tool: Tool) {
        DataProvider.addTool(tool)
        _tools.value = DataProvider.tools
    }

    /**
     * Updates an existing tool.
     */
    fun updateTool(updatedTool: Tool) {
        DataProvider.updateTool(updatedTool)
        _tools.value = DataProvider.tools
    }

    /**
     * Adds a new customer.
     */
    fun addCustomer(customer: Customer) {
        DataProvider.addCustomer(customer)
        _customers.value = DataProvider.customers
    }

    /**
     * Updates an existing customer.
     */
    fun updateCustomer(updatedCustomer: Customer) {
        DataProvider.updateCustomer(updatedCustomer)
        _customers.value = DataProvider.customers
    }


}
