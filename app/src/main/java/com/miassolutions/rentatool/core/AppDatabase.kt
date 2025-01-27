package com.miassolutions.rentatool.core

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.miassolutions.rentatool.data.daos.CustomerDao
import com.miassolutions.rentatool.data.daos.RentalDao
import com.miassolutions.rentatool.data.daos.ToolDao
import com.miassolutions.rentatool.data.daos.ToolHistoryDao
import com.miassolutions.rentatool.data.model.Customer
import com.miassolutions.rentatool.data.model.Rental
import com.miassolutions.rentatool.data.model.Tool
import com.miassolutions.rentatool.data.model.ToolHistory
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.concurrent.Executors

@Database(
    entities = [Tool::class, Customer::class, Rental::class, ToolHistory::class],
    version = 1,
    exportSchema = false
)

abstract class AppDatabase : RoomDatabase() {
    abstract fun toolDao(): ToolDao
    abstract fun customerDao(): CustomerDao
    abstract fun rentalDao(): RentalDao
    abstract fun toolHistoryDao(): ToolHistoryDao

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


