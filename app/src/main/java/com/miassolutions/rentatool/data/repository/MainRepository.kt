package com.miassolutions.rentatool.data.repository

import com.miassolutions.rentatool.data.common.CustomerResult
import com.miassolutions.rentatool.data.dao.CustomerDao
import com.miassolutions.rentatool.data.dao.RentalOrderDao
import com.miassolutions.rentatool.data.dao.RentalRelationsDao
import com.miassolutions.rentatool.data.dao.RentalTransactionDao
import com.miassolutions.rentatool.data.dao.RentedToolDao
import com.miassolutions.rentatool.data.dao.ReturnedToolDao
import com.miassolutions.rentatool.data.dao.ToolDao
import com.miassolutions.rentatool.data.entities.CustomerEntity
import com.miassolutions.rentatool.data.entities.RentalOrderEntity
import com.miassolutions.rentatool.data.entities.RentedToolEntity
import com.miassolutions.rentatool.data.entities.ReturnedToolEntity
import com.miassolutions.rentatool.data.entities.ToolEntity
import com.miassolutions.rentatool.data.relationship.RentalOrderWithRentedTools
import com.miassolutions.rentatool.data.relationship.RentalWithTools
import com.miassolutions.rentatool.data.relationship.RentedToolWithTool
import com.miassolutions.rentatool.data.relationship.RentedToolWithToolName
import com.miassolutions.rentatool.data.relationship.ToolWithAvailability
import com.miassolutions.rentatool.ui.fragments.mainfragments.returntool.ReturnToolItem
import com.miassolutions.rentatool.ui.fragments.mainfragments.toolselection.SelectedTool
import kotlinx.coroutines.flow.Flow
import java.time.LocalDate
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MainRepository @Inject constructor(
    private val customerDao: CustomerDao,
    private val rentalOrderDao: RentalOrderDao,
    private val rentalRelationsDao: RentalRelationsDao,
    private val rentalTransactionDao: RentalTransactionDao,
    private val rentedToolDao: RentedToolDao,
    private val returnedToolDao: ReturnedToolDao,
    private val toolDao: ToolDao
) {

    // ------------------- Customer -------------------

    suspend fun insertCustomer(customer: CustomerEntity): CustomerResult {
        return try {

            val customerId = customerDao.insertCustomer(customer)
            CustomerResult.Success(customerId)

        } catch (e: Exception) {

            CustomerResult.Failure("Error : ${e.localizedMessage}")
        }
    }

    suspend fun updateCustomer(customer: CustomerEntity) =
        customerDao.updateCustomer(customer)

    suspend fun deleteCustomer(customer: CustomerEntity) =
        customerDao.deleteCustomer(customer)

    suspend fun getCustomerById(customerId: Long): CustomerEntity? =
        customerDao.getCustomerById(customerId)

    suspend fun getCustomerByCNIC(cnic: String): CustomerEntity? =
        customerDao.getCustomerByCNIC(cnic)

    fun getAllCustomers(): Flow<List<CustomerEntity>> =
        customerDao.getAllCustomers()


    // ------------------- Tool -------------------

    suspend fun insertTool(tool: ToolEntity): Long =
        toolDao.insertTool(tool)

    suspend fun updateTool(tool: ToolEntity) =
        toolDao.updateTool(tool)

//    suspend fun deleteTool(tool: ToolEntity) =
//        toolDao.deleteTool(tool)

    suspend fun getToolById(toolId: Long): ToolEntity? =
        toolDao.getToolById(toolId)

    suspend fun getToolByName(name: String): ToolEntity? =
        toolDao.getToolByName(name)


    fun getToolsWithAvailability(): Flow<List<ToolWithAvailability>> =
        toolDao.getToolsWithAvailability()


    // ------------------- Rental Orders -------------------

    suspend fun insertRentalOrder(order: RentalOrderEntity): Long =
        rentalOrderDao.insertRentalOrder(order)

    suspend fun getRentalOrderWithRentedToolsById(orderId: Long): RentalOrderWithRentedTools? =
        rentalOrderDao.getRentalOrderWithRentedToolsById(orderId)

    suspend fun getRentedToolsWithReturns(orderId: Long): List<RentedToolWithToolName> =
        rentalTransactionDao.getRentedToolWithReturns(orderId)

    fun getOrdersWithRentedTools(customerId: Long): Flow<List<RentalOrderWithRentedTools>> {
        return rentalOrderDao.getRentalOrdersWithRentedToolsByCustomer(customerId)
    }

    fun getRentalOrdersWithRentedToolsByCustomer(customerId: Long): Flow<List<RentalOrderWithRentedTools>> =
        rentalOrderDao.getRentalOrdersWithRentedToolsByCustomer(customerId)

    suspend fun deleteRentalOrder(orderId: Long) =
        rentalOrderDao.deleteRentalOrderById(orderId)

    // ------------------- Rental Transactions -------------------

    suspend fun getActiveRentalWithTools(customerId: Long): List<RentalWithTools> {
        return rentalRelationsDao.getActiveRentalWithTools(customerId)
    }


//    suspend fun returnTools(customerId: Long, tools: List<ReturnToolItem>) {
//        rentalTransactionDao.runInTransaction {
//            val now = java.time.LocalDate.now()
//
//            tools.forEach { item ->
//                val rentedTool = rentedToolDao.getRentedToolByCustomerAndTool(customerId, item.toolId)
//                    ?: throw IllegalStateException("No active rental found for tool ${item.toolId}")
//
//                // 1. Insert a return entry using rentedToolId
//                returnedToolDao.insertReturnedTool(
//                    ReturnedToolEntity(
//                        rentedToolId = rentedTool.rentedToolId,
//                        returnedQuantity = item.returnQuantity,
//                        returnDate = now
//                    )
//                )
//
//                // 2. Update remaining quantity
//                val newRemaining = (rentedTool.remainingQuantity - item.returnQuantity).coerceAtLeast(0)
//                rentedToolDao.updateRentedTool(
//                    rentedTool.copy(remainingQuantity = newRemaining)
//                )
//            }
//
//            // 3. Close rental orders if all tools returned
//            val activeOrders = rentalOrderDao.getRentalOrdersByCustomer(customerId)
//            activeOrders.forEach { order ->
//                val remaining = rentedToolDao.countRemainingTools(order.rentalOrderId)
//                if (remaining == 0) {
//                    rentalOrderDao.updateRentalOrder(order.copy(isClosed = true))
//                }
//            }
//        }
//    }


    suspend fun performRentalTransaction(
        customerId: Long,
        estimatedReturnDate: LocalDate,
        tools: List<SelectedTool>
    ) =
        rentalTransactionDao.performRentalTransaction(customerId, estimatedReturnDate, tools)

    suspend fun performReturnTransaction(returns: List<ReturnedToolEntity>) =
        rentalTransactionDao.performReturnTransaction(returns)

    // ------------------- Rental Relations -------------------

    fun getRentalsWithToolsForCustomer(customerId: Long): Flow<List<RentalWithTools>> =
        rentalRelationsDao.getRentalsWithToolsForCustomer(customerId)

    suspend fun getRentalWithToolsByOrderId(orderId: Long): RentalWithTools =
        rentalRelationsDao.getRentalWithToolsByOrderId(orderId)

    // ------------------- Rented Tools -------------------

    suspend fun getRentedToolsForOrder(orderId: Long): List<RentedToolEntity> =
        rentedToolDao.getRentedToolsForOrder(orderId)

    suspend fun updateRentedTool(tool: RentedToolEntity) =
        rentedToolDao.updateRentedTool(tool)

    // ------------------- Returned Tools -------------------

    suspend fun insertReturnedTool(tool: ReturnedToolEntity) =
        returnedToolDao.insertReturnedTool(tool)

    suspend fun getReturnsForRentedTool(rentedToolId: Long): List<ReturnedToolEntity> =
        returnedToolDao.getReturnsForRentedTool(rentedToolId)
}
