package com.miassolutions.rentatool.data.relationship

import androidx.room.Embedded
import androidx.room.Relation
import com.miassolutions.rentatool.data.entities.RentalOrderEntity
import com.miassolutions.rentatool.data.entities.RentedToolEntity
import com.miassolutions.rentatool.data.entities.ToolEntity

data class RentalWithTools(
    @Embedded val rentalOrder: RentalOrderEntity,

    @Relation(
        parentColumn = "orderId",
        entityColumn = "orderId",
        entity = RentedToolEntity::class
    )
    val rentedTools: List<RentedToolWithTool>
)


