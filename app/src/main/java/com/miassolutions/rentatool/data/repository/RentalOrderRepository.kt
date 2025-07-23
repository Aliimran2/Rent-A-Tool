package com.miassolutions.rentatool.data.repository

import com.miassolutions.rentatool.data.entities.RentalOrderEntity
import kotlinx.coroutines.flow.Flow

interface RentalOrderRepository {
    suspend fun insertRentalOrder(order: RentalOrderEntity): Long
    fun getOrdersByCustomer(customerId: Long): Flow<List<RentalOrderEntity>>
    suspend fun getOrderById(orderId: Long): RentalOrderEntity?
}