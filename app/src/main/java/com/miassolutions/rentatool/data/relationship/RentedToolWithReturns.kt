package com.miassolutions.rentatool.data.relationship

import androidx.room.Embedded
import androidx.room.Relation
import com.miassolutions.rentatool.data.entities.RentedToolEntity

data class RentedToolWithReturns(
    @Embedded val rentedTool: RentedToolEntity,
    @Relation(
        parentColumn = "rentedToolId",
        entityColumn = "rentedToolId"
    )

    val returnedTools: List<RentedToolEntity>
)
