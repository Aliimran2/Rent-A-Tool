package com.miassolutions.rentatool.data.relationship

import androidx.room.Embedded
import androidx.room.Relation
import com.miassolutions.rentatool.data.entities.RentedToolEntity
import com.miassolutions.rentatool.data.entities.ReturnedToolEntity
import com.miassolutions.rentatool.data.entities.ToolEntity

data class RentedToolWithToolName(
    @Embedded val rentedTool: RentedToolEntity,

    @Relation(
        parentColumn = "toolId",
        entityColumn = "toolId"
    )
    val tool: ToolEntity,

    @Relation(
        entity = ReturnedToolEntity::class,
        parentColumn = "returnToolId",
        entityColumn = "returnToolId"
    )

    val returns: List<ReturnedToolEntity>
)
