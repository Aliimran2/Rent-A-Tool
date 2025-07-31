package com.miassolutions.rentatool.data.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import com.miassolutions.rentatool.data.relationship.RentalOrderWithRentedTools
import com.miassolutions.rentatool.data.relationship.RentalWithTools
import kotlinx.coroutines.flow.Flow

@Dao
interface RentalRelationsDao {
    @Transaction
    @Query("SELECT * FROM rental_orders WHERE customerId = :customerId")
    fun getRentalsWithToolsForCustomer(customerId: Long): Flow<List<RentalWithTools>>

    @Transaction
    @Query("SELECT * FROM rental_orders WHERE orderId = :orderId")
    suspend fun getRentalWithToolsByOrderId(orderId: Long): RentalWithTools

    @Transaction
    @Query("SELECT * FROM rental_orders WHERE customerId = :customerId AND isClosed = 0")
    suspend fun getActiveRentalWithTools(customerId: Long): List<RentalWithTools>
}


