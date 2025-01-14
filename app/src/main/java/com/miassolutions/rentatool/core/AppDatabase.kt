package com.miassolutions.rentatool.core

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.miassolutions.rentatool.data.daos.CustomerDao
import com.miassolutions.rentatool.data.daos.RentalDao
import com.miassolutions.rentatool.data.daos.RentalDetailDao
import com.miassolutions.rentatool.data.daos.ToolDao
import com.miassolutions.rentatool.data.model.Customer
import com.miassolutions.rentatool.data.model.Rental
import com.miassolutions.rentatool.data.model.RentalDetail
import com.miassolutions.rentatool.data.model.Tool
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.concurrent.Executors

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
                )
                    .fallbackToDestructiveMigration() // Reset database on schema changes
                    .addCallback(DatabaseCallback(context))
                    .build()
                INSTANCE = instance
                instance
            }
        }

        private class DatabaseCallback(private val context: Context) : Callback() {
            override fun onCreate(db: SupportSQLiteDatabase) {
                super.onCreate(db)
                insertInitialData(context)
            }
        }
        

        // Method to insert initial data
        private fun insertInitialData(context: Context) {
            val toolDao = getDatabase(context).toolDao()
            val customerDao = getDatabase(context).customerDao()

            // List of tools to be inserted
            val tools = listOf(
                Tool(name = "Hammer", rentPerDay = 5.0, totalStock = 10, availableStock = 10, rentedQuantity = 0, toolCondition = "New"),
                Tool(name = "Drill", rentPerDay = 10.0, totalStock = 8, availableStock = 8, rentedQuantity = 0, toolCondition = "Used"),
                Tool(name = "Saw", rentPerDay = 7.0, totalStock = 12, availableStock = 12, rentedQuantity = 0, toolCondition = "New"),
                Tool(name = "Wrench", rentPerDay = 3.0, totalStock = 15, availableStock = 15, rentedQuantity = 0, toolCondition = "Good"),
                Tool(name = "Screwdriver", rentPerDay = 2.0, totalStock = 20, availableStock = 20, rentedQuantity = 0, toolCondition = "Good")
            )

            // List of customers to be inserted
            val customers = listOf(
                Customer(customerPic = "pic1.jpg", customerName = "M Akram", cnicNumber = "123456789", customerPhone = "555-1234", constructionPlace = "Site A", contractorName = "Alice", contractorPhone = "555-9876", ownerName = "Bob", ownerPhone = "555-1122"),
                Customer(customerPic = "pic2.jpg", customerName = "Amjid Khan", cnicNumber = "234567890", customerPhone = "555-2345", constructionPlace = "Site B", contractorName = "Charlie", contractorPhone = "555-8765", ownerName = "Dave", ownerPhone = "555-2233"),
                Customer(customerPic = "pic3.jpg", customerName = "Hafiz Kareem", cnicNumber = "345678901", customerPhone = "555-3456", constructionPlace = "Site C", contractorName = "Eve", contractorPhone = "555-7654", ownerName = "Frank", ownerPhone = "555-3344"),
                Customer(customerPic = "pic4.jpg", customerName = "Boota", cnicNumber = "456789012", customerPhone = "555-4567", constructionPlace = "Site D", contractorName = "George", contractorPhone = "555-6543", ownerName = "Hannah", ownerPhone = "555-4455"),
                Customer(customerPic = "pic5.jpg", customerName = "Latif", cnicNumber = "567890123", customerPhone = "555-5678", constructionPlace = "Site E", contractorName = "Ivy", contractorPhone = "555-5432", ownerName = "Jack", ownerPhone = "555-5566")
            )

            CoroutineScope(Dispatchers.IO).launch {
                val db = getDatabase(context)
                db.toolDao().insertAll(tools)
                db.customerDao().addCustomers(customers)
            }

        }

    }


}


