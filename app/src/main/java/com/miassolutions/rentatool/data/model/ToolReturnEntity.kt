package com.miassolutions.rentatool.data.model

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import java.time.LocalDate

@Entity(
    tableName = "tool_returns",
    foreignKeys = [
        ForeignKey(
            entity = RentalLineItemEntity::class,
            parentColumns = ["lineItemId"],
            childColumns = ["lineItemId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index("lineItemId")]
)
data class ToolReturnEntity(
    @PrimaryKey(autoGenerate = true)
    val returnId: Long = 0L,
    val lineItemId: Long,
    val returnDate: LocalDate,
    val quantityReturned: Int
)
