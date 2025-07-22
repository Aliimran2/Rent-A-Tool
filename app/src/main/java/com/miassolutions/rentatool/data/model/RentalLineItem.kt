package com.miassolutions.rentatool.data.model

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import java.time.LocalDate

@Entity(
    tableName = "rental_line_items",
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
            onDelete = ForeignKey.RESTRICT
        )
    ],
    indices = [Index("orderId"), Index("toolId")]
)
data class RentalLineItemEntity(
    @PrimaryKey(autoGenerate = true)
    val lineItemId: Long = 0L,
    val orderId: Long,
    val toolId: Long,
    val quantityRented: Int,
    val quantityReturned: Int = 0,
    val rentalStartDate: LocalDate,
    val lastReturnDate: LocalDate? = null // updates on each partial return
)

