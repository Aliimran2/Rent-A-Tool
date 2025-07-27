package com.miassolutions.rentatool.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import com.miassolutions.rentatool.data.entities.RentalOrderEntity
import com.miassolutions.rentatool.data.entities.RentedToolEntity
import com.miassolutions.rentatool.data.entities.ReturnedToolEntity
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

    @Query("UPDATE rented_tools SET remainingQuantity = remainingQuantity - :qty WHERE rentedToolId = :rentedToolId")
    suspend fun updateRemainingQty(rentedToolId: Long, qty: Int)


    @Transaction
    suspend fun performRentalTransaction(
        customerId: Long,
        selectedTools: List<SelectedTool>
    ) {
        if (selectedTools.isEmpty()) return

        val totalAmount = selectedTools.sumOf { it.quantity * it.rentPricePerDay }

        val rentalOrder = RentalOrderEntity(
            orderId = 0L,
            customerId = customerId,
            rentDate = LocalDate.now(),
            totalAmount = totalAmount,
            isClosed = false
        )

        val orderId = insertOrder(rentalOrder)

        val rentedTools = selectedTools.map {
            RentedToolEntity(
                rentedToolId = 0L,
                orderId = orderId,
                toolId = it.toolId,
                rentedQuantity = it.quantity,
                remainingQuantity = it.quantity,
                rentPricePerDay = it.rentPricePerDay
            )
        }

        insertRentedTools(rentedTools)
    }


    @Transaction
    suspend fun performReturnTransaction(
        returnList: List<ReturnedToolEntity>
    ) {
        insertReturnedTools(returnList)
        returnList.forEach {
            updateRemainingQty(it.rentedToolId, it.returnedQuantity)
        }
    }
}
