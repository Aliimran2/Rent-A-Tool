package com.miassolutions.rentatool.data.repository

import com.miassolutions.rentatool.data.entities.ReturnedToolEntity
import com.miassolutions.rentatool.ui.fragments.mainfragments.toolselection.SelectedTool

interface RentalRepository {

    suspend fun rentToolsToCustomer(customerId: Long, rentedTools: List<SelectedTool>)
    suspend fun returnTools(returnList: List<ReturnedToolEntity>)
    suspend fun performRentalTransaction(customerId : Long, selectedTools: List<SelectedTool> )
}