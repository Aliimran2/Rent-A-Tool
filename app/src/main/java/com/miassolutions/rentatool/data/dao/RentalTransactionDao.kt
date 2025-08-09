package com.miassolutions.rentatool.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import com.miassolutions.rentatool.data.entities.RentalOrderEntity
import com.miassolutions.rentatool.data.entities.RentedToolEntity
import com.miassolutions.rentatool.data.entities.ReturnedToolEntity
import com.miassolutions.rentatool.data.relationship.RentedToolWithToolName
import com.miassolutions.rentatool.ui.fragments.mainfragments.toolselection.SelectedTool
import java.time.LocalDate

@Dao
interface RentalTransactionDao {

    @Insert
    suspend fun insertOrder(order: RentalOrderEntity): Long

    @Insert
    suspend fun insertRentedTools(tools: List<RentedToolEntity>)

    @Insert
    suspend fun insertReturnedTools(tools: List<ReturnedToolEntity>)


    @Transaction
    @Query("SELECT * FROM rented_tools WHERE orderId = :orderId")
    suspend fun getRentedToolWithReturns(orderId: Long): List<RentedToolWithToolName>

    @Transaction
    suspend fun performRentalTransaction(
        customerId: Long,
        estimatedReturnDate: LocalDate,
        selectedTools: List<SelectedTool>
    ) {
        if (selectedTools.isEmpty()) return

        val totalAmount = selectedTools.sumOf { it.rentedQuantity * it.rentPricePerDay }

        val rentalOrder = RentalOrderEntity(
            orderId = 0L,
            customerId = customerId,
            rentDate = LocalDate.now(),
            totalAmount = totalAmount,
            estimatedReturnDate = estimatedReturnDate,
            isClosed = false
        )

        val orderId = insertOrder(rentalOrder)

        val rentedTools = selectedTools.map {
            RentedToolEntity(
                rentedToolId = 0L,
                orderId = orderId,
                toolId = it.toolId,
                rentedQuantity = it.rentedQuantity,
//                remainingQuantity = it.remainingQuantity,
                rentPricePerDay = it.rentPricePerDay
            )
        }

        insertRentedTools(rentedTools)
    }

    @Query("SELECT * FROM rented_tools WHERE rentedToolId = :id")
    suspend fun getRentedToolById(id: Long): RentedToolEntity?


    @Transaction
    suspend fun performReturnTransaction(
        orderId: Long,
        returns: List<ReturnedToolEntity>
    ) {
        if (returns.isEmpty()) return

        insertReturnedTools(returns)

        returns.forEach { returned ->
            val rentedTool = getRentedToolById(returned.rentedToolId) ?: return@forEach
            val newQuantity = rentedTool.rentedQuantity - returned.returnedQuantity
            updateRentedTool(rentedTool.copy(rentedQuantity = newQuantity))

        }

        val allReturned = getRentedToolWithReturns(orderId).all { rentedToolWithToolName ->
            val totalReturned = rentedToolWithToolName.returns.sumOf { it.returnedQuantity }
            totalReturned >= rentedToolWithToolName.rentedTool.rentedQuantity
        }

        if (allReturned) {
            closeRentalOrder(orderId)
        }
    }

    @Update
    suspend fun updateRentedTool(rentedTool: RentedToolEntity)

    @Query("UPDATE rental_orders SET isClosed = 1 WHERE orderId = :orderId")
    suspend fun closeRentalOrder(orderId: Long)

}

