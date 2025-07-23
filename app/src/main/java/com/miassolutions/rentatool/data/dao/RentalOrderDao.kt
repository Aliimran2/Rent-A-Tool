package com.miassolutions.rentatool.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import com.miassolutions.rentatool.data.entities.RentalOrderEntity
import com.miassolutions.rentatool.data.relationship.RentalOrderWithRentedTools
import kotlinx.coroutines.flow.Flow

@Dao
interface RentalOrderDao {


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRentalOrder(order: RentalOrderEntity): Long

    @Transaction
    @Query("SELECT * FROM rental_orders WHERE customerId = :customerId")
    fun getRentalOrdersWithRentedToolsByCustomer(customerId: Long): Flow<List<RentalOrderWithRentedTools>>

    @Transaction
    @Query("SELECT * FROM rental_orders WHERE orderId = :orderId")
    suspend fun getRentalOrderWithRentedToolsById(orderId: Long): RentalOrderWithRentedTools?

    @Query("DELETE FROM rental_orders WHERE orderId = :orderId")
    suspend fun deleteRentalOrderById(orderId: Long)
}