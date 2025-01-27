package com.miassolutions.rentatool.data.daos

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.miassolutions.rentatool.data.model.Rental

@Dao
interface RentalDao {

    // Insert a new rental record (renting a tool)
    @Insert
    suspend fun insertRental(rental: Rental)

//    get all rentals for a specific customer

    @Query("SELECT * FROM rentals WHERE customerId = :customerId ORDER BY rentStartDate")
    fun getRentalForCustomer(customerId: Long):LiveData<List<Rental>>


    @Query("SELECT * FROM rentals WHERE toolId = :toolId ORDER BY rentStartDate")
    fun getRentalsForTool(toolId:Long):LiveData<List<Rental>>

    @Query("""
        UPDATE rentals
        SET rentEndDate = :rentEndDate, rentedQuantity =:returnedQuantity
        WHERE rentalId =:rentalId
    """)
    suspend fun updateRentalOnReturn(rentalId : Long, rentEndDate: Long, returnedQuantity : Int)

    @Query("""
        UPDATE rentals
        SET rentedQuantity = rentedQuantity +:additionalQuantity
        WHERE rentalId = :rentalId
    """)
    suspend fun updateRentalQuantity(rentalId: Long, additionalQuantity : Int)


    @Query("SELECT * FROM rentals WHERE rentalId = :rentalId")
    fun getRentalById(rentalId: Long): Rental

    @Query("SELECT * FROM rentals WHERE customerId = :customerId")
    fun rentalsByCustomer(customerId: Long): LiveData<List<Rental>>

    @Query("UPDATE rentals SET totalRent = :totalRent, returnDate = :returnDate, isFinalized = :isFinalized WHERE rentalId = :rentalId")
    suspend fun finalizeRental(rentalId: Long, totalRent: Double, returnDate: Long, isFinalized: Boolean)


    @Query("SELECT * FROM rentals")
    fun getAllRentals(): LiveData<List<Rental>>

    @Insert
    suspend fun addRental(rental: Rental): Long
}