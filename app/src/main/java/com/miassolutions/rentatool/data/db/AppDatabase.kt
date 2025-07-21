package com.miassolutions.rentatool.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.miassolutions.rentatool.data.dao.CustomerDao
import com.miassolutions.rentatool.data.dao.RentalDao
import com.miassolutions.rentatool.data.dao.ToolDao
import com.miassolutions.rentatool.data.dao.ToolHistoryDao
import com.miassolutions.rentatool.data.model.CustomerEntity
import com.miassolutions.rentatool.data.model.RentalEntity
import com.miassolutions.rentatool.data.model.ToolEntity
import com.miassolutions.rentatool.data.model.ToolHistoryEntity

@Database(
    entities = [ToolEntity::class, CustomerEntity::class, RentalEntity::class, ToolHistoryEntity::class],
    version = 1,
    exportSchema = false
)

abstract class AppDatabase : RoomDatabase() {
    abstract fun toolDao(): ToolDao
    abstract fun customerDao(): CustomerDao
    abstract fun rentalDao(): RentalDao
    abstract fun toolHistoryDao(): ToolHistoryDao




}


