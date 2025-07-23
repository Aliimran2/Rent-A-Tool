package com.miassolutions.rentatool.data.relationship

import androidx.room.Embedded
import androidx.room.Relation
import com.miassolutions.rentatool.data.entities.CustomerEntity
import com.miassolutions.rentatool.data.entities.RentalOrderEntity

data class CustomerWithOrders(
    @Embedded val customer: CustomerEntity,
    @Relation(
        parentColumn = "customerId",
        entityColumn = "customerId"
    )
    val rentalOrders: List<RentalOrderEntity>
)
