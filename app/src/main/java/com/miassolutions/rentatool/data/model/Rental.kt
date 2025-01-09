package com.miassolutions.rentatool.data.model

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey


@Entity(
    tableName = "rentals",
    foreignKeys = [
        ForeignKey(entity = Customer::class, parentColumns = ["customerId"], childColumns = ["customerId"])
    ],
    indices = [Index(value = ["customerId"])]
)
data class Rental(
    @PrimaryKey(autoGenerate = true)
    val rentalId : Long,
    val customerId: Long,
    val rentalDate : Long,
    val returnDate : Long?
)
