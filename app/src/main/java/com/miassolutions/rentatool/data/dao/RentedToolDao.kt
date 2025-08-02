package com.miassolutions.rentatool.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.miassolutions.rentatool.data.entities.RentedToolEntity

@Dao
interface RentedToolDao {

    @Insert
    suspend fun insertRentedTools(tools: List<RentedToolEntity>)

    @Update
    suspend fun updateRentedTool(tool: RentedToolEntity)

    @Query("SELECT * FROM rented_tools WHERE orderId = :orderId")
    suspend fun getRentedToolsForOrder(orderId: Long): List<RentedToolEntity>

    @Query("""
    SELECT * FROM rented_tools
    WHERE toolId = :toolId AND orderId IN (
        SELECT orderId FROM rental_orders
        WHERE customerId = :customerId AND isClosed = 0
    )
    LIMIT 1
""")
    suspend fun getRentedToolByCustomerAndTool(customerId: Long, toolId: Int): RentedToolEntity?

//    @Query("SELECT COUNT(*) FROM rented_tools WHERE orderId = :rentalOrderId AND remainingQuantity > 0")
//    suspend fun countRemainingTools(rentalOrderId: Long): Int


}