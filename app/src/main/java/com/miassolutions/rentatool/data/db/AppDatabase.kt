package com.miassolutions.rentatool.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.miassolutions.rentatool.data.converter.LocalDateConverter
import com.miassolutions.rentatool.data.dao.CustomerDao
import com.miassolutions.rentatool.data.dao.ToolDao
import com.miassolutions.rentatool.data.model.CustomerEntity
import com.miassolutions.rentatool.data.model.ToolEntity

@Database(
    entities = [ToolEntity::class, CustomerEntity::class],
    version = 1,
    exportSchema = false
)
@TypeConverters(LocalDateConverter::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun toolDao(): ToolDao
    abstract fun customerDao(): CustomerDao



}


