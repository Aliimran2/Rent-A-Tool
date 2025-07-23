package com.miassolutions.rentatool.data.entities

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import java.time.LocalDate

@Entity(tableName = "tools")
data class ToolEntity(
    @PrimaryKey(autoGenerate = true)
    val toolId: Long =0L,
    val name: String,
    val totalQuantity: Int,
    val availableQuantity: Int,
    val rentPricePerDay: Double,
    val condition: String = "New",
    val createdAt: LocalDate = LocalDate.now()
)







//data class ToolEntity(
//    @PrimaryKey(autoGenerate = true)
//    val toolId: Long =0L,
//    val name: String,
//    val rentPerDay: Double,
//    val totalQuantity: Int =0,
//    val toolCondition : String = "New"
//)
