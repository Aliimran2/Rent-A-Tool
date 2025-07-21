package com.miassolutions.rentatool.data.model

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "customers",
    indices = [Index(value = ["cnicNumber"], unique = true)] // Prevent duplicate customers
)
data class CustomerEntity(
    @PrimaryKey(autoGenerate = true)
    val customerId: Long = 0L,
    val customerPic: String = "",
    val customerName: String,
    val cnicNumber: String, //TODO()
    val customerPhone: String = "",
    val constructionPlace: String = "",//TODO()
    val contractorName: String = "", //TODO()
    val contractorPhone: String = "", //TODO()
    val ownerName: String = "", //TODO()
    val ownerPhone: String = "",
    val totalRent: Double = 0.0
)

