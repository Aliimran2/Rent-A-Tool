package com.miassolutions.rentatool.data.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.miassolutions.rentatool.data.model.RentalEntity

@Dao
interface RentalDao {

    // Insert a new rental record (renting a tool)
    @Insert
    suspend fun insertRental(rentalEntity: RentalEntity)

//    get all rentals for a specific customer

    @Query("SELECT * FROM rentals WHERE customerId = :customerId ORDER BY rentStartDate")
    fun getRentalForCustomer(customerId: Long):LiveData<List<RentalEntity>>


    @Query("SELECT * FROM rentals WHERE toolId = :toolId ORDER BY rentStartDate")
    fun getRentalsForTool(toolId:Long):LiveData<List<RentalEntity>>

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
    fun getRentalById(rentalId: Long): RentalEntity

    @Query("SELECT * FROM rentals WHERE customerId = :customerId")
    fun rentalsByCustomer(customerId: Long): LiveData<List<RentalEntity>>




    @Query("SELECT * FROM rentals")
    fun getAllRentals(): LiveData<List<RentalEntity>>

    @Insert
    suspend fun addRental(rentalEntity: RentalEntity): Long
}