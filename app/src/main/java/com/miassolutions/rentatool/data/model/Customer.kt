package com.miassolutions.rentatool.data.model

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "customers",
    indices = [Index(value = ["customerPhone"], unique = true)] // Prevent duplicate customers
)
data class Customer(
    @PrimaryKey(autoGenerate = true) val customerId: Long = 0L,
    var customerPic : String ,
    var customerName: String,
    var cnicNumber: String , //TODO()
    var customerPhone: String,
    var constructionPlace: String ,//TODO()
    var contractorName: String , //TODO()
    var contractorPhone : String , //TODO()
    var ownerName : String , //TODO()
    var ownerPhone : String
)

