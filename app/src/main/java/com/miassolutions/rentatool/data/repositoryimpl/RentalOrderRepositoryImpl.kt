package com.miassolutions.rentatool.data.repositoryimpl

import com.miassolutions.rentatool.data.dao.RentalOrderDao
import com.miassolutions.rentatool.data.entities.RentalOrderEntity
import com.miassolutions.rentatool.data.relationship.RentalOrderWithRentedTools
import com.miassolutions.rentatool.data.repository.RentalOrderRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class RentalOrderRepositoryImpl @Inject constructor(private val rentalOrderDao: RentalOrderDao) : RentalOrderRepository{
    override suspend fun insertRentalOrder(order: RentalOrderEntity): Long {
        return rentalOrderDao.insertRentalOrder(order)
    }

    override fun getOrdersWithRentedTools(customerId: Long): Flow<List<RentalOrderWithRentedTools>> {
        return rentalOrderDao.getRentalOrdersWithRentedToolsByCustomer(customerId)
    }

    override suspend fun getOrderWithToolsById(orderId: Long): RentalOrderWithRentedTools? {
        return rentalOrderDao.getRentalOrderWithRentedToolsById(orderId)
    }

    override suspend fun deleteOrder(orderId: Long) {
        rentalOrderDao.deleteRentalOrderById(orderId)
    }

}