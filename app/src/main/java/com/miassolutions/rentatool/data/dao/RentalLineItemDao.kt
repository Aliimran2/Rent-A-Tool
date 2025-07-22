package com.miassolutions.rentatool.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.miassolutions.rentatool.data.entities.RentalLineItemEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface RentalLineItemDao {
    @Insert
    suspend fun insert(lineItem: RentalLineItemEntity): Long

    @Update
    suspend fun update(lineItem: RentalLineItemEntity)

    @Query("SELECT * FROM rental_line_items WHERE orderId =:orderId")
    fun getLineItemsForOrder(orderId : Long) : Flow<List<RentalLineItemEntity>>

    @Query("SELECT * FROM rental_line_items WHERE lineItemId =:lineItemId")
    suspend fun getLineItemById(lineItemId : Long) : RentalLineItemEntity?
}