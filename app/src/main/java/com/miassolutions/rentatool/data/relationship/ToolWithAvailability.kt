package com.miassolutions.rentatool.data.relationship

import androidx.room.Embedded
import com.miassolutions.rentatool.data.entities.ToolEntity

data class ToolWithAvailability(
    @Embedded val tool: ToolEntity,
    val rentedQuantity: Int
) {
    val availableQuantity: Int
        get() = tool.totalQuantity - rentedQuantity

    val displayQuantity: String
        get() = "$availableQuantity / ${tool.totalQuantity}"
}


