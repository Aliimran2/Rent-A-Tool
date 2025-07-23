package com.miassolutions.rentatool.data.repository

import com.miassolutions.rentatool.data.dao.CustomerDao
import com.miassolutions.rentatool.data.dao.RentalOrderDao
import com.miassolutions.rentatool.data.dao.RentedToolDao
import com.miassolutions.rentatool.data.dao.ReturnedToolDao
import com.miassolutions.rentatool.data.dao.ToolDao
import com.miassolutions.rentatool.data.entities.CustomerEntity
import com.miassolutions.rentatool.data.entities.RentalOrderEntity
import com.miassolutions.rentatool.data.entities.ToolEntity
import com.miassolutions.rentatool.uimodels.CustomerFormResult
import com.miassolutions.rentatool.utils.Constants
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class Repository @Inject constructor(
    private val toolDao: ToolDao,
    private val rentalOrderDao: RentalOrderDao,

) {





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




}