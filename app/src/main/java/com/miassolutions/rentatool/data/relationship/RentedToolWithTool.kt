package com.miassolutions.rentatool.data.relationship

import androidx.room.Embedded
import androidx.room.Relation
import com.miassolutions.rentatool.data.entities.RentedToolEntity
import com.miassolutions.rentatool.data.entities.ToolEntity

data class RentedToolWithToolDetails(
    @Embedded val rentedTool: RentedToolEntity,

    @Relation(
        parentColumn = "toolId",
        entityColumn = "toolId"
    )
    val tool: ToolEntity
)
