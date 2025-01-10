package com.miassolutions.rentatool.data.model

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "tools",
    indices = [Index(value = ["name"], unique = true)] // Prevent duplicate tool names
)
data class Tool(
    @PrimaryKey(autoGenerate = true) val toolId: Long,
    val name: String,
    val rentPerDay: Double,
    val totalStock: Int,
    var availableStock: Int,
    var rentedQuantity: Int,
    var toolCondition : String = "New"
)
