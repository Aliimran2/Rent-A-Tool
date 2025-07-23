package com.miassolutions.rentatool.data.entities

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import java.time.LocalDate

@Entity(
    tableName = "returned_tools",
    foreignKeys = [
        ForeignKey(
            entity = RentedToolEntity::class,
            parentColumns = ["rentedToolId"],
            childColumns = ["rentedToolId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index("rentedToolId")]
)
data class ReturnedToolEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val rentedToolId: Long,
    val returnedQuantity: Int,
    val returnDate: LocalDate = LocalDate.now()
)


