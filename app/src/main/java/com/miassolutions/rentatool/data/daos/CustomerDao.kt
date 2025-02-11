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

    @Query("SELECT * FROM customers WHERE customerName LIKE '%' || :query || '%' OR customerPhone LIKE '%' || :query || '%'")
    suspend fun searchCustomers(query: String): List<Customer>

    @Insert(onConflict = OnConflictStrategy.ABORT) // Prevent duplicate entries
    suspend fun insertCustomer(customer: Customer)

    // Update customer total rent (after calculating rent)
    @Query("UPDATE customers SET totalRent = totalRent + :rent WHERE customerId = :customerId")
    suspend fun updateCustomerTotalRent(customerId: Long, rent: Double)

    @Query("SELECT * FROM customers ORDER BY customerName")
    fun getAllCustomers(): LiveData<List<Customer>>

    @Query("SELECT * FROM customers WHERE customerId = :customerId LIMIT 1")
    suspend fun getCustomerById(customerId: Long): Customer?

//   return liveData
    /*
    * suspend function: Best for one-time data fetches when you don't need real-time updates. Use in repository for async data
        retrieval, then pass the result to ViewModel and Fragment.

    * LiveData: Best for real-time updates where data might change and needs to be observed. Use in repository to return data
      that automatically updates UI through ViewModel and Fragment.

    * In short: use suspend for single fetches, use LiveData for ongoing data updates.

    */

//    @Query("SELECT * FROM customers WHERE customerId = :customerId")
//    fun getCustomerById(customerId: Long): LiveData<Customer>

    @Delete
    suspend fun deleteCustomer(customer: Customer)

    @Update
    suspend fun updateCustomer(customer: Customer)
}