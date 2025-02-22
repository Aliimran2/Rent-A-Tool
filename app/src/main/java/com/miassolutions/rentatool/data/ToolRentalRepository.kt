package com.miassolutions.rentatool.data

import androidx.lifecycle.LiveData
import com.miassolutions.rentatool.core.AppDatabase
import com.miassolutions.rentatool.data.model.Customer
import com.miassolutions.rentatool.data.model.Tool
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext

class ToolRentalRepository(
    private val db: AppDatabase
) {

    private val toolDao = db.toolDao()
    private val customerDao = db.customerDao()
    private val rentalDao = db.rentalDao()
    private val toolHistoryDao = db.toolHistoryDao()

    //these two functions will be deleted later todo()
    suspend fun insertCustomers(customers: List<Customer>) {
        customerDao.insertCustomers(customers)
    }
    suspend fun insertTools(tools : List<Tool>) = toolDao.insertAll(tools)

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

    // Update an existing customer
    suspend fun updateCustomer(customer: Customer) {
        withContext(Dispatchers.IO) {
            customerDao.updateCustomer(customer) // Update customer in the database
        }
    }

    // Fetch all customers
    fun getAllCustomers(): Flow<List<Customer>> = customerDao.getAllCustomers()

    // Fetch a customer by ID
    suspend fun getCustomerById(customerId: Long): Customer? =
        customerDao.getCustomerById(customerId)



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

    suspend fun searchCustomer(query: String): Flow<List<Customer>> = customerDao.searchCustomers(query)

    suspend fun isToolExists(toolName: String): Boolean {
        return toolDao.getToolByName(toolName) != null
    }

    // Fetch all tools
    fun getAllTools(): LiveData<List<Tool>> = toolDao.getAllTools()

    suspend fun getToolByIdDirect(toolId:Long) : Tool? {
        return toolDao.getToolByIdDirect(toolId)
    }

    // Fetch a tool by ID
    fun getToolById(toolId: Long): LiveData<Tool?> = toolDao.getToolById(toolId)

}
