package com.miassolutions.rentatool.data.repository

import com.miassolutions.rentatool.data.entities.RentalOrderEntity
import com.miassolutions.rentatool.data.relationship.RentalOrderWithRentedTools
import kotlinx.coroutines.flow.Flow

interface RentalOrderRepository {
    suspend fun insertRentalOrder(order: RentalOrderEntity): Long
    fun getOrdersWithRentedTools(customerId: Long): Flow<List<RentalOrderWithRentedTools>>
    suspend fun getOrderWithToolsById(orderId: Long): RentalOrderWithRentedTools?
    suspend fun deleteOrder(orderId: Long)
}