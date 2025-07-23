package com.miassolutions.rentatool.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import com.miassolutions.rentatool.data.entities.RentalOrderEntity
import com.miassolutions.rentatool.data.entities.RentedToolEntity
import com.miassolutions.rentatool.data.entities.ReturnedToolEntity

@Dao
interface RentalTransactionDao {
    @Insert
    suspend fun insertOrder(order: RentalOrderEntity): Long

    @Insert
    suspend fun insertRentedTools(tools: List<RentedToolEntity>)

    @Insert
    suspend fun insertReturnedTools(tools: List<ReturnedToolEntity>)

    @Query("UPDATE tools SET availableQuantity = availableQuantity - :qty WHERE toolId = :toolId")
    suspend fun decreaseToolQty(toolId: Long, qty: Int)

    @Query("UPDATE tools SET availableQuantity = availableQuantity + :qty WHERE toolId = :toolId")
    suspend fun increaseToolQty(toolId: Long, qty: Int)

    @Query("UPDATE rented_tools SET remainingQuantity = remainingQuantity - :qty WHERE rentedToolId = :rentedToolId")
    suspend fun updateRemainingQty(rentedToolId: Long, qty: Int)

    @Transaction
    suspend fun performRentalTransaction(
        order: RentalOrderEntity,
        tools: List<RentedToolEntity>
    ) {
        val orderId = insertOrder(order)
        val updateTools = tools.map { it.copy(orderId = orderId) }
        insertRentedTools(updateTools)

        updateTools.forEach {
            decreaseToolQty(it.toolId, it.rentedQuantity)
        }
    }

    @Transaction
    suspend fun performReturnTransaction(
        returnList: List<ReturnedToolEntity>
    ) {
        insertReturnedTools(returnList)

        returnList.forEach {
            increaseToolQty(it.rentedToolId, it.returnedQuantity)
            updateRemainingQty(it.rentedToolId, it.returnedQuantity)
        }
    }
}