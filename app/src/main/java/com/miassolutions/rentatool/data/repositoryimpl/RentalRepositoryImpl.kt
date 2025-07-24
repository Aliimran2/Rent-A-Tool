package com.miassolutions.rentatool.data.repositoryimpl

import com.miassolutions.rentatool.data.dao.RentalOrderDao
import com.miassolutions.rentatool.data.dao.RentalTransactionDao
import com.miassolutions.rentatool.data.dao.RentedToolDao
import com.miassolutions.rentatool.data.dao.ReturnedToolDao
import com.miassolutions.rentatool.data.dao.ToolDao
import com.miassolutions.rentatool.data.entities.RentalOrderEntity
import com.miassolutions.rentatool.data.entities.RentedToolEntity
import com.miassolutions.rentatool.data.entities.ReturnedToolEntity
import com.miassolutions.rentatool.data.repository.RentalRepository
import javax.inject.Inject

class RentalRepositoryImpl @Inject constructor(
    private val rentalTransactionDao: RentalTransactionDao
) : RentalRepository {
    override suspend fun rentToolsToCustomer(
        order: RentalOrderEntity,
        rentedTools: List<RentedToolEntity>
    ) {
        rentalTransactionDao.performRentalTransaction(order, rentedTools)
    }

    override suspend fun returnTools(returnList: List<ReturnedToolEntity>) {
        rentalTransactionDao.performReturnTransaction(returnList)
    }


}