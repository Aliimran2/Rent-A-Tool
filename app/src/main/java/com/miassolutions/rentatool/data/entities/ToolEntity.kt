package com.miassolutions.rentatool.data.entities

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "tools",
    indices = [Index(value = ["name"], unique = true)] // Prevent duplicate tool names
)
data class ToolEntity(
    @PrimaryKey(autoGenerate = true)
    val toolId: Long =0L,
    val name: String,
    val rentPerDay: Double,
    val totalQuantity: Int =0,
    val toolCondition : String = "New"
)
