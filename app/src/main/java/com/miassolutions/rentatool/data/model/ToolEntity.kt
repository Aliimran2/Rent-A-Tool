package com.miassolutions.rentatool.data.model

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "tools",
    indices = [Index(value = ["name"], unique = true)] // Prevent duplicate tool names
)
data class ToolEntity(
    @PrimaryKey(autoGenerate = true) val toolId: Long =0L,
    val name: String,
    val rentPerDay: Double,
    val totalStock: Int,
    var availableStock: Int =0,
    var rentedQuantity: Int=0,
    var toolCondition : String = "New"
)
