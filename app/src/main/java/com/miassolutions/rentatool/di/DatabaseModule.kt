package com.miassolutions.rentatool.di

import android.content.Context
import androidx.room.Room
import com.google.gson.Gson
import com.miassolutions.rentatool.data.dao.CustomerDao
import com.miassolutions.rentatool.data.dao.RentalOrderDao
import com.miassolutions.rentatool.data.dao.RentalTransactionDao
import com.miassolutions.rentatool.data.dao.RentedToolDao
import com.miassolutions.rentatool.data.dao.ReturnedToolDao
import com.miassolutions.rentatool.data.dao.ToolDao
import com.miassolutions.rentatool.data.db.AppDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Singleton
    @Provides
    fun providesAppDatabase(@ApplicationContext context: Context): AppDatabase {
        return Room.databaseBuilder(
            context,
            AppDatabase::class.java,
            "RentAToolDB"
        ).build()
    }

    @Singleton
    @Provides
    fun providesCustomerDao(db: AppDatabase): CustomerDao = db.customerDao()

    @Singleton
    @Provides
    fun providesToolDao(db: AppDatabase): ToolDao = db.toolDao()


    @Singleton
    @Provides
    fun providesRentalOrderDao(db: AppDatabase): RentalOrderDao = db.rentalOrderDao()

    @Singleton
    @Provides
    fun providesRentedToolDao(db: AppDatabase): RentedToolDao = db.rentedToolDao()

    @Singleton
    @Provides
    fun providesReturnToolDao(db: AppDatabase): ReturnedToolDao = db.returnToolDao()

    @Singleton
    @Provides
    fun providesTransactionDao(db: AppDatabase): RentalTransactionDao = db.rentalTransactionDao()

    @Provides
    fun providesGson() : Gson = Gson()


}