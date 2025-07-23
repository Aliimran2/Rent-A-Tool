package com.miassolutions.rentatool.data.repository

import com.miassolutions.rentatool.data.dao.CustomerDao
import com.miassolutions.rentatool.data.dao.RentalOrderDao
import com.miassolutions.rentatool.data.dao.RentedToolDao
import com.miassolutions.rentatool.data.dao.ReturnToolDao
import com.miassolutions.rentatool.data.dao.ToolDao
import com.miassolutions.rentatool.data.entities.CustomerEntity
import com.miassolutions.rentatool.data.entities.RentalOrderEntity
import com.miassolutions.rentatool.data.entities.ToolEntity
import com.miassolutions.rentatool.uimodels.CustomerFormResult
import com.miassolutions.rentatool.utils.Constants
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class Repository @Inject constructor(
    private val customerDao: CustomerDao,
    private val toolDao: ToolDao,
    private val rentalOrderDao: RentalOrderDao,
    private val rentedToolDao : RentedToolDao,
    private val returnToolDao : ReturnToolDao

) {

    suspend fun insertCustomer(customerEntity: CustomerEntity): CustomerFormResult {
        val duplicateCustomer = customerDao.getCustomerByCNIC(customerEntity.cnicNumber)
        return if (duplicateCustomer != null) {
            CustomerFormResult.Failure(Constants.DUPLICATE_CNIC)

        } else {
            try {
                customerDao.insertCustomer(customerEntity)
                CustomerFormResult.Success
            } catch (e: Exception) {
                CustomerFormResult.Failure(message = "Unknown error")
            }
        }


    }

    fun getAllCustomers(): Flow<List<CustomerEntity>> = customerDao.getAllCustomers()

    suspend fun deleteCustomer(customer: CustomerEntity) {
        customerDao.deleteCustomer(customer)
    }

    suspend fun deleteAllCustomers() = customerDao.deleteAllCustomers() //todo()

    fun searchCustomers(query: String): Flow<List<CustomerEntity>> =
        customerDao.searchCustomers(query)

    /*End Region of CustomerEntity*/


    suspend fun insertTool(tool: ToolEntity) {
        toolDao.insertTool(tool)
    }

    fun getAllTools(): Flow<List<ToolEntity>> = toolDao.getAllTools()
    fun searchTool(query: String): Flow<List<ToolEntity>> = toolDao.searchTools(query)

    /*end region of tools functions*/

    fun getAllRentalOrders(customerId: Long): Flow<List<RentalOrderEntity>> =
        rentalOrderDao.getOrdersForCustomer(customerId)

    suspend fun getRentalOrderById(orderId: Long): RentalOrderEntity? =
        rentalOrderDao.getOrderById(orderId)




}