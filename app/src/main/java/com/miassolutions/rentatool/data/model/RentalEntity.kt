package com.miassolutions.rentatool.data.model

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.ForeignKey.Companion.CASCADE
import androidx.room.Index
import androidx.room.PrimaryKey


@Entity(
    tableName = "rentals",
    foreignKeys = [
        ForeignKey(entity = CustomerEntity::class, parentColumns = ["customerId"], childColumns = ["customerId"], onDelete = CASCADE),
        ForeignKey(entity = ToolEntity::class, parentColumns = ["toolId"], childColumns = ["toolId"], onDelete = CASCADE)
    ],
    indices = [Index(value = ["customerId"]), Index(value = ["toolId"])]
)
data class RentalEntity(
    @PrimaryKey(autoGenerate = true) val rentalId: Long = 0L,
    val customerId: Long, // FK to customers table
    val toolId: Long, // FK to tools table
    val rentedQuantity: Int, // Quantity of the tool rented
    val rentPerDay: Double, // Rent per day for this tool
    val rentStartDate: Long, // Start date of the rental (timestamp)
    val estimatedReturnDate: Long, // Estimated return date (timestamp)
    val rentEndDate: Long? = null, // End date (null until returned)
    var totalRent: Double = 0.0 // Total rent for this rental entry
)

