package com.miassolutions.rentatool

import android.app.Application
import com.miassolutions.rentatool.core.AppDatabase
import com.miassolutions.rentatool.data.ToolRentalRepository

class MyApplication : Application(){
        val database by lazy { AppDatabase.getDatabase(this) }
    // Initialize repository with the correct DAOs
    val repository by lazy {
        ToolRentalRepository(
            database
        )
    }


}