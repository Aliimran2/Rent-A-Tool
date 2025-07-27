package com.miassolutions.rentatool.data.repositoryimpl

import com.miassolutions.rentatool.data.dao.RentalTransactionDao
import com.miassolutions.rentatool.data.entities.ReturnedToolEntity
import com.miassolutions.rentatool.data.repository.RentalRepository
import com.miassolutions.rentatool.ui.fragments.mainfragments.toolselection.SelectedTool
import javax.inject.Inject

class RentalRepositoryImpl @Inject constructor(
    private val rentalTransactionDao: RentalTransactionDao
) : RentalRepository {
    override suspend fun rentToolsToCustomer(
        customerId: Long,
        rentedTools: List<SelectedTool>,

        ) {
        rentalTransactionDao.performRentalTransaction(customerId, rentedTools )
    }

    override suspend fun returnTools(returnList: List<ReturnedToolEntity>) {
        rentalTransactionDao.performReturnTransaction(returnList)
    }

    override suspend fun performRentalTransaction(
        customerId: Long,
        selectedTools: List<SelectedTool>
    ) {
        rentalTransactionDao.performRentalTransaction(customerId, selectedTools)
    }


}