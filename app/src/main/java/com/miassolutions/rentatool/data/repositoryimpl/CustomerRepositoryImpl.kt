package com.miassolutions.rentatool.data.repositoryimpl

import com.miassolutions.rentatool.data.common.CustomerResult
import com.miassolutions.rentatool.data.dao.CustomerDao
import com.miassolutions.rentatool.data.entities.CustomerEntity
import com.miassolutions.rentatool.data.repository.CustomerRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class CustomerRepositoryImpl @Inject constructor(private val customerDao: CustomerDao) :
    CustomerRepository {
    override fun getAllCustomers(): Flow<List<CustomerEntity>> = customerDao.getAllCustomers()

    override suspend fun getCustomerById(id: Long): CustomerEntity? =
        customerDao.getCustomerById(id)

    override suspend fun insertCustomer(customer: CustomerEntity): CustomerResult {
        return try {

            val customerId = customerDao.insertCustomer(customer)
            CustomerResult.Success(customerId)

        } catch (e: Exception) {

            CustomerResult.Failure("Error : ${e.localizedMessage}")
        }
    }


    override suspend fun updateCustomer(customer: CustomerEntity) =
        customerDao.updateCustomer(customer)

    override suspend fun deleteCustomer(customer: CustomerEntity) =
        customerDao.deleteCustomer(customer)

    override suspend fun searchCustomers(query: String): Flow<List<CustomerEntity>> =
        customerDao.searchCustomers(query)

}