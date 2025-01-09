package com.miassolutions.rentatool.ui.viewmodels

import androidx.lifecycle.ViewModel
import com.miassolutions.rentatool.data.model.Customer
import com.miassolutions.rentatool.data.model.Rental
import com.miassolutions.rentatool.data.model.Tool
import com.miassolutions.rentatool.utils.mockdb.DataProvider


import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData


class RentalViewModel : ViewModel() {

    // LiveData for observing tools
    private val _tools = MutableLiveData<List<Tool>>()
    val tools: LiveData<List<Tool>> get() = _tools

    // LiveData for observing customers
    private val _customers = MutableLiveData<List<Customer>>()
    val customers: LiveData<List<Customer>> get() = _customers

    // LiveData for observing rentals
    private val _rentals = MutableLiveData<List<Rental>>()
    val rentals: LiveData<List<Rental>> get() = _rentals

    // Initialize with static data from DataProvider
    init {
        _tools.value = DataProvider.tools
        _customers.value = DataProvider.customers
        _rentals.value = DataProvider.rentals
    }

//    // Add a new tool
//    fun addTool(tool: Tool): Boolean {
//        val result = DataProvider.add(tool)
//        if (result) {
//            _tools.value = DataProvider.getAllTools()
//        }
//        return result
//    }
//
//    // Add a new customer
//    fun addCustomer(customer: Customer): Boolean {
//        val result = DataProvider.addCustomer(customer)
//        if (result) {
//            _customers.value = DataProvider.getAllCustomers()
//        }
//        return result
//    }
//
//    // Rent tools
//    fun rentTools(customerId: Long, toolId: Long, quantity: Int): Boolean {
//        val result = DataProvider.rentTools(customerId, toolId, quantity)
//        if (result) {
//            _tools.value = DataProvider.getAllTools()
//            _rentals.value = DataProvider.getAllRentals()
//        }
//        return result
//    }
//
//    // Return tools
//    fun returnTools(customerId: Long, toolId: Long, quantity: Int): Boolean {
//        val result = DataProvider.returnTools(customerId, toolId, quantity)
//        if (result) {
//            _tools.value = DataProvider.getAllTools()
//            _rentals.value = DataProvider.getAllRentals()
//        }
//        return result
//    }
//
//    // Get tool availability
//    fun isToolAvailable(toolId: Long, quantity: Int): Boolean {
//        return DataProvider.isToolAvailable(toolId, quantity)
//    }
//
//    // Get customer by ID
//    fun getCustomerById(customerId: Long): Customer? {
//        return DataProvider.getCustomerById(customerId)
//    }
//
//    // Get tool by ID
//    fun getToolById(toolId: Long): Tool? {
//        return DataProvider.getToolById(toolId)
//    }
}
