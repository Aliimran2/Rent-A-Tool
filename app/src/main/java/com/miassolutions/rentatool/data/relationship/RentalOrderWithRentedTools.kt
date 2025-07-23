package com.miassolutions.rentatool.data.relationship

import androidx.room.Embedded
import androidx.room.Relation
import com.miassolutions.rentatool.data.entities.RentalOrderEntity
import com.miassolutions.rentatool.data.entities.RentedToolEntity

data class RentalOrderWithRentedTools(
    @Embedded val rentalOrder: RentalOrderEntity,
    @Relation(
        parentColumn = "orderId",
        entityColumn = "orderId"
    )

    val rentedTools: List<RentedToolEntity>
)
