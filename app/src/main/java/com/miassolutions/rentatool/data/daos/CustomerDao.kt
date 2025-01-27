package com.miassolutions.rentatool.data.daos

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.miassolutions.rentatool.data.model.Customer

@Dao
interface CustomerDao {



    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertCustomers(customers: List<Customer>) //will be deleted later todo()

    @Query("SELECT * FROM customers WHERE customerId = :customerId LIMIT 1")
    suspend fun getCustomerById(customerId: Long): Customer?

    @Query("SELECT * FROM customers WHERE customerName LIKE '%' || :query || '%' OR customerPhone LIKE '%' || :query || '%'")
    fun searchCustomers(query: String): List<Customer>

    @Insert(onConflict = OnConflictStrategy.ABORT) // Prevent duplicate entries
    suspend fun insertCustomer(customer: Customer)

    @Update
    suspend fun updateCustomer(customer: Customer)

    @Query("UPDATE customers SET totalRent = totalRent + :rent WHERE customerId = :customerId")
    suspend fun updateCustomerTotalRent(customerId: Long, rent: Double)


    @Delete
    suspend fun deleteCustomer(customer: Customer)

    @Query("SELECT * FROM customers")
    fun getAllCustomers(): LiveData<List<Customer>>
}