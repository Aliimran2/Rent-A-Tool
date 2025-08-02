package com.miassolutions.rentatool.data.entities

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "rented_tools",
    foreignKeys = [
        ForeignKey(
            entity = RentalOrderEntity::class,
            parentColumns = ["orderId"],
            childColumns = ["orderId"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = ToolEntity::class,
            parentColumns = ["toolId"],
            childColumns = ["toolId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index("orderId"), Index("toolId")]
)
data class RentedToolEntity(
    @PrimaryKey(autoGenerate = true)
    val rentedToolId: Long = 0,
    val orderId: Long,
    val toolId: Long,
    val rentedQuantity: Int,
    val rentPricePerDay: Double ,
    val daysRented: Int = 0,
)


