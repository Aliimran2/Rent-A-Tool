package com.miassolutions.rentatool.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.miassolutions.rentatool.data.entities.CustomerEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface CustomerDao {

    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun insertCustomer(customerEntity: CustomerEntity) : Long

    @Update
    suspend fun updateCustomer(customerEntity: CustomerEntity)

    @Delete
    suspend fun deleteCustomer(customerEntity: CustomerEntity)


    @Query("SELECT * FROM customers WHERE customerId = :customerId LIMIT 1")
    suspend fun getCustomerById(customerId: Long): CustomerEntity?

    @Query("SELECT * FROM customers WHERE cnicNumber = :cnicNumber LIMIT 1")
    suspend fun getCustomerByCNIC(cnicNumber: String): CustomerEntity?


    @Query("SELECT * FROM customers ORDER BY customerName")
    fun getAllCustomers(): Flow<List<CustomerEntity>>


}