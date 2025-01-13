package com.miassolutions.rentatool.data.daos

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.miassolutions.rentatool.data.model.Rental

@Dao
interface RentalDao {
    @Query("SELECT * FROM rentals WHERE rentalId = :rentalId")
    fun getRentalById(rentalId: Long): Rental?

    @Query("SELECT * FROM rentals WHERE customerId = :customerId")
    fun searchRentalsByCustomer(customerId: Long): List<Rental>

    @Insert
    suspend fun addRental(rental: Rental): Long
}