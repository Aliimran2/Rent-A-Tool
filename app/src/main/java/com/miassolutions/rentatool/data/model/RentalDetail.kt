package com.miassolutions.rentatool.data.model

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "rental_details",
    foreignKeys = [
        ForeignKey(entity = Tool::class, parentColumns = ["toolId"], childColumns = ["toolId"]),
        ForeignKey(entity = Rental::class, parentColumns = ["rentalId"], childColumns = ["rentalId"])
    ],
    indices = [Index(value = ["toolId"]), Index(value = ["rentalId"])]
)

data class RentalDetail(
    @PrimaryKey(autoGenerate = true)
    val rentalDetailId: Long=0L,
    val rentalId: Long,
    val toolId : Long,
    val rentedQuantity: Int,
    var returnedQuantity: Int = 0,
    var isReturned: Boolean = false,
    var returnDate: Long? = null, // Timestamp
    val rentPerDay: Double
)
