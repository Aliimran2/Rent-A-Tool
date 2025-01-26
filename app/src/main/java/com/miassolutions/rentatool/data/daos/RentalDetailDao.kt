package com.miassolutions.rentatool.data.daos

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.miassolutions.rentatool.data.model.RentalDetail

@Dao
interface RentalDetailDao {
    @Query("SELECT * FROM rental_details WHERE rentalDetailId = :rentalDetailId")
    fun getRentalDetailById(rentalDetailId: Long): RentalDetail?

    @Query("SELECT * FROM rental_details WHERE rentalId = :rentalId")
    fun rentalDetailsByRental(rentalId: Long): LiveData<List<RentalDetail>>

    @Update
    suspend fun updateRentalDetail(rentalDetail: RentalDetail)

    @Insert
    suspend fun addRentalDetail(rentalDetail: RentalDetail): Long
}
