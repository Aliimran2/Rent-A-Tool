package com.miassolutions.rentatool.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.miassolutions.rentatool.data.model.CustomerEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface CustomerDao {


    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertCustomers(customerEntities: List<CustomerEntity>) //will be deleted later todo()

    @Query("SELECT * FROM customers ORDER BY customerName")
    fun getAllCustomers(): Flow<List<CustomerEntity>>

    @Query("SELECT * FROM customers WHERE customerName LIKE '%' || :query || '%' OR customerPhone LIKE '%' || :query || '%'")
    fun searchCustomers(query: String): Flow<List<CustomerEntity>>


    @Insert(onConflict = OnConflictStrategy.ABORT) // Prevent duplicate entries
    suspend fun insertCustomer(customerEntity: CustomerEntity)


    @Query("SELECT * FROM customers WHERE customerId = :customerId LIMIT 1")
    suspend fun getCustomerById(customerId: Long): CustomerEntity?

    @Query("SELECT * FROM customers WHERE cnicNumber = :cnicNumber LIMIT 1")
    suspend fun getCustomerByCNIC(cnicNumber: String): CustomerEntity?

    // Update customer total rent (after calculating rent)
    @Query("UPDATE customers SET totalRent = totalRent + :rent WHERE customerId = :customerId")
    suspend fun updateCustomerTotalRent(customerId: Long, rent: Double)


    @Delete
    suspend fun deleteCustomer(customerEntity: CustomerEntity)

    @Update
    suspend fun updateCustomer(customerEntity: CustomerEntity)
}