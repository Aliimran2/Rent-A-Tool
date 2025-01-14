package com.miassolutions.rentatool.data

import androidx.lifecycle.LiveData
import com.miassolutions.rentatool.core.AppDatabase
import com.miassolutions.rentatool.data.model.Customer
import com.miassolutions.rentatool.data.model.Rental
import com.miassolutions.rentatool.data.model.RentalDetail
import com.miassolutions.rentatool.data.model.Tool
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class ToolRentalRepository(
    private val db: AppDatabase
) {

    private val toolDao = db.toolDao()
    private val customerDao = db.customerDao()
    private val rentalDao = db.rentalDao()
    private val rentalDetailDao = db.rentalDetailDao()

    // Fetch all tools
     fun getAllTools(): LiveData<List<Tool>> = toolDao.getAllTools()

    // Fetch all customers
    fun getAllCustomers(): LiveData<List<Customer>> = customerDao.getAllCustomers()

    // Fetch all rentals by customerId
    fun searchRentalsByCustomer(customerId: Long): LiveData<List<Rental>> = rentalDao.searchRentalsByCustomer(customerId)

    // Fetch all rental details by rentalId
    fun searchRentalDetailsByRental(rentalId: Long): LiveData<List<RentalDetail>> = rentalDetailDao.searchRentalDetailsByRental(rentalId)

    suspend fun addToolIfNotExists(tool: Tool): Boolean {
        val existingTool = toolDao.getToolByName(tool.name)
        return if (existingTool == null) {
            // Tool does not exist, proceed with insertion
            toolDao.insertTool(tool)
            true
        } else {
            // Tool already exists, return false
            false
        }
    }
    // Search tools by name
    suspend fun searchToolsByName(name: String): List<Tool> {
        return withContext(Dispatchers.IO) {
            toolDao.searchTools(name) // Execute the DAO method on the IO thread
        }
    }

    // Search customer by name
    suspend fun searchCustomerByName(name: String): List<Customer> {
        return withContext(Dispatchers.IO) {
            customerDao.searchCustomers(name) // Execute the DAO method on the IO thread
        }
    }

    // Add a new tool
    suspend fun insertTool(tool: Tool): Result<Unit> {
        return withContext(Dispatchers.IO) {
            try {
                toolDao.insertTool(tool) // Use insert for adding a new tool
                Result.success(Unit) // Return success with no additional data
            } catch (e: Exception) {
                Result.failure(e) // Return failure in case of an exception
            }
        }
    }

    // Update an existing tool
    suspend fun updateTool(tool: Tool) {
        withContext(Dispatchers.IO) {
            toolDao.updateTool(tool) // Update tool in the database
        }
    }

    // Add a new customer
    suspend fun insertCustomer(customer: Customer): Result<Unit> {
        return withContext(Dispatchers.IO) {
            try {
                customerDao.insertCustomer(customer) // Insert customer into the database
                Result.success(Unit) // Return success with no additional data
            } catch (e: Exception) {
                Result.failure(e) // Return failure in case of an exception
            }
        }
    }

    suspend fun updateToolStock(toolId: Long, availableStock: Int, rentedQuantity: Int) {
        toolDao.updateStock(toolId, availableStock, rentedQuantity)
    }

    // Update an existing customer
    suspend fun updateCustomer(customer: Customer) {
        withContext(Dispatchers.IO) {
            customerDao.updateCustomer(customer) // Update customer in the database
        }
    }

    // Add a new rental
    suspend fun insertRental(rental: Rental): Long {
        return withContext(Dispatchers.IO) {
            rentalDao.addRental(rental) // Insert rental into the database
        }
    }

    // Add a rental detail
    suspend fun insertRentalDetails(rentalDetail: RentalDetail) {
        withContext(Dispatchers.IO) {
            rentalDetailDao.addRentalDetail(rentalDetail) // Insert rental detail into the database
        }
    }

    // Fetch a tool by ID
    suspend fun getToolById(toolId: Long): Tool? {
        return withContext(Dispatchers.IO) {
            toolDao.getToolById(toolId) // Fetch tool by ID from the database
        }
    }

    // Fetch a customer by ID
    suspend fun getCustomerById(customerId: Long): Customer? {
        return withContext(Dispatchers.IO) {
            customerDao.getCustomerById(customerId) // Fetch customer by ID from the database
        }
    }

    // Fetch a rental detail by ID
    suspend fun getRentalDetailById(rentalDetailId: Long): RentalDetail? {
        return withContext(Dispatchers.IO) {
            rentalDetailDao.getRentalDetailById(rentalDetailId) // Fetch rental detail by ID from the database
        }
    }
}
