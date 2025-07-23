package com.miassolutions.rentatool.data.entities

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import java.time.LocalDate
@Entity(
    tableName = "rental_orders",
    foreignKeys = [ForeignKey(
        entity = CustomerEntity::class,
        parentColumns = ["customerId"],
        childColumns = ["customerId"],
        onDelete = ForeignKey.CASCADE
    )],
    indices = [Index("customerId")]
)
data class RentalOrderEntity(
    @PrimaryKey(autoGenerate = true)
    val orderId: Long = 0,
    val customerId: Long,
    val rentDate: LocalDate = LocalDate.now(),
    val estimatedReturnDate : LocalDate,
    val totalAmount: Double = 0.0,
    val isClosed: Boolean = false
)


//@Entity(
//    tableName = "rental_orders",
//    foreignKeys = [
//        ForeignKey(
//            entity = CustomerEntity::class,
//            parentColumns = ["customerId"],
//            childColumns = ["customerId"],
//            onDelete = ForeignKey.CASCADE
//        )
//    ],
//    indices = [Index("customerId")]
//
//)
//data class RentalOrderEntity(
//    @PrimaryKey(autoGenerate = true)
//    val orderId: Long = 0L,
//    val customerId: Long,
//    val orderDate: LocalDate,
//    val promisedReturnDate: LocalDate,
//    val totalRentalAmount: Double = 0.0
//)


