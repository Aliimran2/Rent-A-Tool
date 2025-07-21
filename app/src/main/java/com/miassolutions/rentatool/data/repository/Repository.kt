package com.miassolutions.rentatool.data.repository

import com.miassolutions.rentatool.Constants
import com.miassolutions.rentatool.data.dao.CustomerDao
import com.miassolutions.rentatool.data.dao.ToolDao
import com.miassolutions.rentatool.data.model.CustomerEntity
import com.miassolutions.rentatool.uimodels.CustomerFormResult
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class Repository @Inject constructor(
    private val customerDao: CustomerDao,
    private val toolDao: ToolDao
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

}