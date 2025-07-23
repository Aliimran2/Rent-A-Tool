package com.miassolutions.rentatool.data.repository

import com.miassolutions.rentatool.utils.Constants
import com.miassolutions.rentatool.data.dao.CustomerDao
import com.miassolutions.rentatool.data.dao.RentalLineItemDao
import com.miassolutions.rentatool.data.dao.RentalOrderDao
import com.miassolutions.rentatool.data.dao.ToolDao
import com.miassolutions.rentatool.data.entities.CustomerEntity
import com.miassolutions.rentatool.data.entities.RentalLineItemEntity
import com.miassolutions.rentatool.data.entities.RentalOrderEntity
import com.miassolutions.rentatool.data.entities.ToolEntity
import com.miassolutions.rentatool.ui.fragments.toolselection.RentedTool
import com.miassolutions.rentatool.uimodels.CustomerFormResult
import kotlinx.coroutines.flow.Flow
import java.time.LocalDate
import javax.inject.Inject

class Repository @Inject constructor(
    private val customerDao: CustomerDao,
    private val toolDao: ToolDao,
    private val rentalOrderDao: RentalOrderDao,
    private val rentalLineItemDao: RentalLineItemDao
) {

    suspend fun insertCustomer(customerEntity: CustomerEntity): CustomerFormResult {
        val duplicateCustomer = customerDao.getCustomerByCNIC(customerEntity.cnicNumber)
        return if (duplicateCustomer != null) {
            CustomerFormResult.Failure(Constants.DUPLICATE_CNIC)

        } else {
            try {
                customerDao.insertCustomer(customerEntity)
                CustomerFormResult.Success
            } catch (e: Exception) {
                CustomerFormResult.Failure(message = "Unknown error")
            }
        }


    }

    fun getAllCustomers(): Flow<List<CustomerEntity>> = customerDao.getAllCustomers()

    suspend fun deleteCustomer(customer: CustomerEntity) {
        customerDao.deleteCustomer(customer)
    }

    suspend fun deleteAllCustomers() = customerDao.deleteAllCustomers() //todo()

    fun searchCustomers(query: String): Flow<List<CustomerEntity>> =
        customerDao.searchCustomers(query)

    /*End Region of CustomerEntity*/


    suspend fun insertTool(tool: ToolEntity) {
        toolDao.insertTool(tool)
    }

    fun getAllTools(): Flow<List<ToolEntity>> = toolDao.getAllTools()
    fun searchTool(query: String): Flow<List<ToolEntity>> = toolDao.searchTools(query)

    /*end region of tools functions*/

    fun getAllRentalOrders(customerId: Long): Flow<List<RentalOrderEntity>> =
        rentalOrderDao.getOrdersForCustomer(customerId)

    suspend fun getRentalOrderById(orderId: Long): RentalOrderEntity? =
        rentalOrderDao.getOrderById(orderId)

    suspend fun rentTools(
        customerId: Long,
        rentedTools: List<RentedTool>,
        returnDate: LocalDate
    ) {
        val orderId = rentalOrderDao.insertOrder(
            RentalOrderEntity(
                customerId = customerId,
                orderDate = LocalDate.now(),
                promisedReturnDate = returnDate,
            )
        )

        rentedTools.forEach {
            rentalLineItemDao.insertItem(
                RentalLineItemEntity(
                    orderId = orderId,
                    toolId = it.toolId,
                    quantityRented = it.quantity,
                    rentalStartDate = LocalDate.now(),
                )
            )
            toolDao.deductToolQuantity(toolId = it.toolId, rented = it.quantity)
        }
    }


}