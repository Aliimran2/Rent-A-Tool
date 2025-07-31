package com.miassolutions.rentatool.data.relationship

import androidx.room.Embedded
import com.miassolutions.rentatool.data.entities.ToolEntity

data class ToolWithAvailability(
    @Embedded val tool: ToolEntity,
    val availableQuantity: Int
){

}


