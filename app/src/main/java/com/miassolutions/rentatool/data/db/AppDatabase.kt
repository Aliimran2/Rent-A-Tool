package com.miassolutions.rentatool.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.miassolutions.rentatool.data.converter.LocalDateConverter
import com.miassolutions.rentatool.data.dao.CustomerDao
import com.miassolutions.rentatool.data.dao.RentalOrderDao
import com.miassolutions.rentatool.data.dao.RentalRelationsDao
import com.miassolutions.rentatool.data.dao.RentalTransactionDao
import com.miassolutions.rentatool.data.dao.RentedToolDao
import com.miassolutions.rentatool.data.dao.ReturnedToolDao
import com.miassolutions.rentatool.data.dao.ToolDao
import com.miassolutions.rentatool.data.entities.CustomerEntity
import com.miassolutions.rentatool.data.entities.RentalOrderEntity
import com.miassolutions.rentatool.data.entities.RentedToolEntity
import com.miassolutions.rentatool.data.entities.ReturnedToolEntity
import com.miassolutions.rentatool.data.entities.ToolEntity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Database(
    entities = [
        ToolEntity::class,
        CustomerEntity::class,
        RentalOrderEntity::class,
        RentedToolEntity::class,
        ReturnedToolEntity::class
    ],
    version = 1,
    exportSchema = false
)
@TypeConverters(LocalDateConverter::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun toolDao(): ToolDao
    abstract fun customerDao(): CustomerDao
    abstract fun rentalOrderDao(): RentalOrderDao
    abstract fun rentedToolDao(): RentedToolDao
    abstract fun returnToolDao(): ReturnedToolDao
    abstract fun rentalTransactionDao() : RentalTransactionDao
    abstract fun rentalRelationsDao() : RentalRelationsDao


    fun clearAllTablesAndReset() {
        CoroutineScope(Dispatchers.IO).launch {
            clearAllTables()
        }
    }


}


