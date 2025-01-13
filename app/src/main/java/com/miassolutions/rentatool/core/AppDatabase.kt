package com.miassolutions.rentatool.core

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.miassolutions.rentatool.data.daos.CustomerDao
import com.miassolutions.rentatool.data.daos.RentalDao
import com.miassolutions.rentatool.data.daos.RentalDetailDao
import com.miassolutions.rentatool.data.daos.ToolDao
import com.miassolutions.rentatool.data.model.Customer
import com.miassolutions.rentatool.data.model.Rental
import com.miassolutions.rentatool.data.model.RentalDetail
import com.miassolutions.rentatool.data.model.Tool

@Database(
    entities = [Tool::class, Customer::class, Rental::class, RentalDetail::class],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun toolDao(): ToolDao
    abstract fun customerDao(): CustomerDao
    abstract fun rentalDao(): RentalDao
    abstract fun rentalDetailDao(): RentalDetailDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "tool_rental_database"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}
