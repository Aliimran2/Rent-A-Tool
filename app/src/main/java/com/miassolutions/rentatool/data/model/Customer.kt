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
    var customerPic : String = "",
    var customerName: String,
    var cnicNumber: String = "12345", //TODO()
    var customerPhone: String,
    var constructionPlace: String = "Chiniot",//TODO()
    var contractorName: String = "Ali Hussain", //TODO()
    var contractorPhone : String = "12345", //TODO()
    var ownerName : String = "Afnan Mumtaz", //TODO()
    var ownerPhone : String = "123456"
)

