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
    val rentalDetailId: Long,
    val rentalId: Long,
    val toolId : Long,
    var quantity: Int,
    val rentPerDay: Double,
    val rentalDate: Long,
    var returnDate: Long?
)
