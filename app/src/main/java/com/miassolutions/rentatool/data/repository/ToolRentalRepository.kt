package com.miassolutions.rentatool.data.repository

import com.miassolutions.rentatool.data.db.AppDatabase
import com.miassolutions.rentatool.data.model.CustomerEntity
import com.miassolutions.rentatool.data.model.ToolEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import javax.inject.Inject

class ToolRentalRepository @Inject constructor(
    private val db: AppDatabase
) {

    private val toolDao = db.toolDao()
    private val customerDao = db.customerDao()
    private val rentalDao = db.rentalDao()
    private val toolHistoryDao = db.toolHistoryDao()

    //these two functions will be deleted later todo()
    suspend fun insertCustomers(customerEntities: List<CustomerEntity>) {
        customerDao.insertCustomers(customerEntities)
    }

    suspend fun insertTools(toolEntities: List<ToolEntity>) = toolDao.insertAll(toolEntities)

    // Add a new customer
    suspend fun insertCustomer(customerEntity: CustomerEntity): Result<Unit> {
        return withContext(Dispatchers.IO) {
            try {
                customerDao.insertCustomer(customerEntity) // Insert customer into the database
                Result.success(Unit) // Return success with no additional data
            } catch (e: Exception) {
                Result.failure(e) // Return failure in case of an exception
            }
        }
    }

    // Update an existing customer
    suspend fun updateCustomer(customerEntity: CustomerEntity) {
        withContext(Dispatchers.IO) {
            customerDao.updateCustomer(customerEntity) // Update customer in the database
        }
    }

    // Fetch all customers
    fun getAllCustomers(): Flow<List<CustomerEntity>> = customerDao.getAllCustomers()

    // Fetch a customer by ID
    suspend fun getCustomerById(customerId: Long): CustomerEntity? =
        customerDao.getCustomerById(customerId)

    fun searchCustomer(query: String): Flow<List<CustomerEntity>> =
        customerDao.searchCustomers(query)


    // Add a new tool
    suspend fun insertTool(toolEntity: ToolEntity): Result<Unit> {
        return withContext(Dispatchers.IO) {
            try {
                toolDao.insertTool(toolEntity) // Use insert for adding a new tool
                Result.success(Unit) // Return success with no additional data
            } catch (e: Exception) {
                Result.failure(e) // Return failure in case of an exception
            }
        }
    }


    suspend fun isToolExists(toolName: String): Boolean {
        return toolDao.getToolByName(toolName) != null
    }

    // Fetch all tools
    fun getAllTools(): Flow<List<ToolEntity>> = toolDao.getAllTools()

    suspend fun rentTool(toolId: Long, rentedQuantity: Int) {
        val tool = toolDao.getToolById(toolId) ?: throw IllegalArgumentException("Tool not found")
        if (tool.availableStock >= rentedQuantity) {
            tool.availableStock -= rentedQuantity
            tool.rentedQuantity += rentedQuantity
            toolDao.updateTool(tool)
        } else {
            throw IllegalStateException("Not enough stock available")
        }
    }

    suspend fun returnedTool(toolId: Long, returnedQuantity: Int) {
        val tool = toolDao.getToolById(toolId) ?: throw IllegalArgumentException("Tool not found")
        if (tool.rentedQuantity >= returnedQuantity) {
            tool.availableStock += returnedQuantity
            tool.rentedQuantity -= returnedQuantity
            toolDao.updateTool(tool)
        } else {
            throw IllegalStateException("Returned quantity is greater than rented quantity")
        }
    }

    fun searchTool(query: String): Flow<List<ToolEntity>> {
        return toolDao.searchTools(query)
    }


}
