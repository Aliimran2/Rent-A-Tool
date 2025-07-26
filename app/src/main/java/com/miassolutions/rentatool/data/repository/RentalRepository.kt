package com.miassolutions.rentatool.data.repository

import com.miassolutions.rentatool.data.entities.RentalOrderEntity
import com.miassolutions.rentatool.data.entities.RentedToolEntity
import com.miassolutions.rentatool.data.entities.ReturnedToolEntity
import com.miassolutions.rentatool.ui.fragments.toolselection.SelectedTool

interface RentalRepository {

    suspend fun rentToolsToCustomer(customerId: Long, rentedTools: List<SelectedTool>)

    suspend fun returnTools(returnList: List<ReturnedToolEntity>)
}