package com.miassolutions.rentatool.data.repository

import com.miassolutions.rentatool.data.common.CustomerResult
import com.miassolutions.rentatool.data.entities.CustomerEntity
import kotlinx.coroutines.flow.Flow

interface CustomerRepository {
    fun getAllCustomers(): Flow<List<CustomerEntity>>
    suspend fun getCustomerById(id: Long): CustomerEntity?
    suspend fun insertCustomer(customer: CustomerEntity): CustomerResult
    suspend fun updateCustomer(customer: CustomerEntity)
    suspend fun deleteCustomer(customer: CustomerEntity)
    suspend fun searchCustomers(query : String) : Flow<List<CustomerEntity>>
}
