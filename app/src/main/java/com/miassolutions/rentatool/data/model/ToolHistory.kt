package com.miassolutions.rentatool.data.model

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.ForeignKey.Companion.CASCADE
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "tool_history",
    foreignKeys = [
        ForeignKey(entity = Customer::class, parentColumns = ["customerId"], childColumns = ["customerId"], onDelete = CASCADE),
        ForeignKey(entity = Tool::class, parentColumns = ["toolId"], childColumns = ["toolId"], onDelete = CASCADE)
    ],
    indices = [Index(value = ["customerId"]), Index(value = ["toolId"])]
)
data class ToolHistory(
    @PrimaryKey(autoGenerate = true) val historyId: Long = 0L,
    val customerId: Long, // FK to customers table
    val toolId: Long, // FK to tools table
    val rentedQuantity: Int, // Quantity rented
    val transactionType: String, // e.g., "Rent", "Return"
    val timestamp: Long, // Timestamp of the action
    val rentPerDay: Double, // Rent per day at the time of the action
    var totalRent: Double = 0.0 // Total rent for this action
)
