package com.miassolutions.rentatool.data.repository

import com.miassolutions.rentatool.data.common.CustomerResult
import com.miassolutions.rentatool.data.dao.CustomerDao
import com.miassolutions.rentatool.data.entities.CustomerEntity
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class CustomerRepository @Inject constructor(
    private val customerDao: CustomerDao
) {
    suspend fun insertCustomer(customer: CustomerEntity): CustomerResult {
        return try {

            val customerId = customerDao.insertCustomer(customer)
            CustomerResult.Success(customerId)

        } catch (e: Exception) {

            CustomerResult.Failure("Error : ${e.localizedMessage}")
        }

    }

    fun getAllCustomers(): Flow<List<CustomerEntity>> = customerDao.getAllCustomers()
    fun searchCustomers(query: String): Flow<List<CustomerEntity>> =
        customerDao.searchCustomers(query)
}




