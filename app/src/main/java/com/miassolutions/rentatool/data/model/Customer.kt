package com.miassolutions.rentatool.data.model

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "customers",
    indices = [Index(value = ["contact"], unique = true)] // Prevent duplicate customers
)
data class Customer(
    @PrimaryKey(autoGenerate = true) val customerId: Long,
    val name: String,
    val contact: String
)

