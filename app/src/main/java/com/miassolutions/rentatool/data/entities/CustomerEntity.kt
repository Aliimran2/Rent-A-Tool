package com.miassolutions.rentatool.data.entities

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import java.time.LocalDate

@Entity(tableName = "customers", indices = [Index(value = ["cnicNumber"], unique = true)])
data class CustomerEntity(
    @PrimaryKey(autoGenerate = true)
    val customerId: Long = 0L,
    val cnicNumber: String,
    val customerName: String,
    val customerPhone: String = "",
    val createdAt: LocalDate = LocalDate.now()
)







//data class CustomerEntity(
//    @PrimaryKey(autoGenerate = true)
//    val customerId: Long = 0L,
//    val cnicNumber: String, //TODO()
//    val customerName: String,
//    val customerPhone: String = "",
//    val constructionPlace: String = "",//TODO()
//    val contractorName: String = "", //TODO()
//    val contractorPhone: String = "", //TODO()
//    val ownerName: String = "", //TODO()
//    val ownerPhone: String = "",
//    val customerPic: String = ""
//)

