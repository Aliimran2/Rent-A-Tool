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

    @Query("UPDATE rented_tools SET remainingQuantity = remainingQuantity - :qty WHERE rentedToolId = :rentedToolId")
    suspend fun updateRemainingQty(rentedToolId: Long, qty: Int)

    /**
     * Save order and rented tools in a single transaction
     */
    @Transaction
    suspend fun performRentalTransaction(
        order: RentalOrderEntity,
        tools: List<RentedToolEntity>
    ) {
        val orderId = insertOrder(order)
        val updatedTools = tools.map { it.copy(orderId = orderId) }
        insertRentedTools(updatedTools)
        // ToolEntity stock is no longer updated here, it's computed at runtime
    }

    /**
     * Save returned tools and update remaining quantity
     */
    @Transaction
    suspend fun performReturnTransaction(
        returnList: List<ReturnedToolEntity>
    ) {
        insertReturnedTools(returnList)
        returnList.forEach {
            updateRemainingQty(it.rentedToolId, it.returnedQuantity)
            // ToolEntity availableQuantity is not updated directly
        }
    }
}
