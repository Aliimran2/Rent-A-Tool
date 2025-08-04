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

//    @Query("UPDATE rented_tools SET remainingQuantity = remainingQuantity - :qty WHERE rentedToolId = :rentedToolId")
//    suspend fun updateRemainingQty(rentedToolId: Long, qty: Int)


    @Transaction
    suspend fun performRentalTransaction(
        customerId: Long,
        estimatedReturnDate : LocalDate,
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

//    @Query("""
//    SELECT
//        rt.rentedToolId,
//        rt.toolId,
//        t.name AS toolName,
//        rt.rentedQuantity,
//        IFNULL(SUM(rtd.returnedQuantity), 0) AS returnedQuantity,
//        (rt.rentedQuantity - IFNULL(SUM(rtd.returnedQuantity), 0)) AS remainingQuantity
//    FROM rented_tools rt
//    INNER JOIN tools t ON t.toolId = rt.toolId
//    LEFT JOIN returned_tools rtd ON rtd.rentedToolId = rt.rentedToolId
//    WHERE rt.orderId = :orderId
//    GROUP BY rt.rentedToolId, rt.toolId, t.name, rt.rentedQuantity
//""")
//    suspend fun getRentedToolsWithReturnStatus(orderId: Long): List<RentedToolWithReturnStatus>



    @Transaction
    suspend fun performReturnTransaction(
        returnList: List<ReturnedToolEntity>
    ) {
        insertReturnedTools(returnList)
//        returnList.forEach {
//            updateRemainingQty(it.rentedToolId, it.returnedQuantity)
//        }
    }
}
