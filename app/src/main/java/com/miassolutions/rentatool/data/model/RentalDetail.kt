package com.miassolutions.rentatool.data.model

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index

@Entity(
    tableName = "rental_details",
    foreignKeys = [
        ForeignKey(entity = Tool::class, parentColumns = ["toolId"], childColumns = ["toolId"]),
        ForeignKey(entity = Rental::class, parentColumns = ["rentalId"], childColumns = ["rentalId"])
    ],
    indices = [Index(value = ["toolId"]), Index(value = ["rentalId"])]
)

data class RentalDetail(
    val rentalDetailId: Long,
    val rentalId: Long,
    val quantity: Int,
    val rentPerDay: Double,
    val rentalDate: Long,
    val returnDate: Long?
)
