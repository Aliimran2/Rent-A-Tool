package com.miassolutions.rentatool.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.miassolutions.rentatool.data.converter.LocalDateConverter
import com.miassolutions.rentatool.data.dao.CustomerDao
import com.miassolutions.rentatool.data.dao.RentalLineItemDao
import com.miassolutions.rentatool.data.dao.RentalOrderDao
import com.miassolutions.rentatool.data.dao.ToolDao
import com.miassolutions.rentatool.data.entities.CustomerEntity
import com.miassolutions.rentatool.data.entities.RentalLineItemEntity
import com.miassolutions.rentatool.data.entities.RentalOrderEntity
import com.miassolutions.rentatool.data.entities.ToolEntity

@Database(
    entities = [
        ToolEntity::class,
        CustomerEntity::class,
        RentalOrderEntity::class,
        RentalLineItemEntity::class,
    ],
    version = 1,
    exportSchema = false
)
@TypeConverters(LocalDateConverter::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun toolDao(): ToolDao
    abstract fun customerDao(): CustomerDao
    abstract fun rentalOrderDao(): RentalOrderDao
    abstract fun rentalLineItemDao(): RentalLineItemDao


}


