package com.miassolutions.rentatool.di

import android.content.Context
import androidx.room.Room
import com.miassolutions.rentatool.data.db.AppDatabase
import com.miassolutions.rentatool.data.dao.CustomerDao
import com.miassolutions.rentatool.data.dao.RentalDao
import com.miassolutions.rentatool.data.dao.ToolDao
import com.miassolutions.rentatool.data.dao.ToolHistoryDao
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
    fun providesRentalDao(db: AppDatabase): RentalDao = db.rentalDao()

    @Singleton
    @Provides
    fun providesToolHistoryDao(db: AppDatabase):     ToolHistoryDao = db.toolHistoryDao()



}