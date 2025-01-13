package com.miassolutions.rentatool.data.daos

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.miassolutions.rentatool.data.model.RentalDetail

@Dao
interface RentalDetailDao {
    @Query("SELECT * FROM rental_details WHERE rentalDetailId = :rentalDetailId")
    fun getRentalDetailById(rentalDetailId: Long): RentalDetail?

    @Query("SELECT * FROM rental_details WHERE rentalId = :rentalId")
    fun searchRentalDetailsByRental(rentalId: Long): List<RentalDetail>

    @Insert
    suspend fun addRentalDetail(rentalDetail: RentalDetail): Long
}