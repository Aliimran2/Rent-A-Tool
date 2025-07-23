package com.miassolutions.rentatool.data.repository

import com.miassolutions.rentatool.data.entities.RentalOrderEntity
import com.miassolutions.rentatool.data.entities.RentedToolEntity
import com.miassolutions.rentatool.data.entities.ReturnedToolEntity

interface RentalRepository {
    suspend fun rentToolsToCustomer(
        order: RentalOrderEntity,
        rentedTools: List<RentedToolEntity>
    )

    suspend fun returnTools(
        returnList: List<ReturnedToolEntity>
    )
}