package com.miassolutions.rentatool.data.model

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index


@Entity(
    tableName = "customer_ledger",
    foreignKeys = [
        ForeignKey(entity = Customer::class, parentColumns = ["customerId"], childColumns = ["customerId"])
    ],
    indices = [Index(value = ["customerId"])]

)
data class CustomerLedger(
    val ledgerId : Long,
    val customerId : Long,
    val rentalId : Long,
    val paymentDate : Long,
    var totalBill : Double = 0.0,
)
