package com.miassolutions.rentatool.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.miassolutions.rentatool.data.entities.RentalOrderEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface RentalOrderDao {

    @Insert
    suspend fun insertOrder(order: RentalOrderEntity): Long

    @Update
    suspend fun updateOrder(order: RentalOrderEntity)

    @Query("SELECT * FROM rental_orders WHERE customerId =:customerId ORDER BY orderDate DESC")
    fun getOrdersForCustomer(customerId : Long) : Flow<List<RentalOrderEntity>>

    @Query("SELECT * FROM rental_orders WHERE orderId = :orderId")
    suspend fun getOrderById(orderId : Long): RentalOrderEntity?
}