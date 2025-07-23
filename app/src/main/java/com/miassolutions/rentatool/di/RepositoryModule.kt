package com.miassolutions.rentatool.di

import com.miassolutions.rentatool.data.repository.CustomerRepository
import com.miassolutions.rentatool.data.repository.RentalOrderRepository
import com.miassolutions.rentatool.data.repository.ToolRepository
import com.miassolutions.rentatool.data.repositoryimpl.CustomerRepositoryImpl
import com.miassolutions.rentatool.data.repositoryimpl.RentalOrderRepositoryImpl
import com.miassolutions.rentatool.data.repositoryimpl.ToolRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun bindCustomerRepository(
        customerRepositoryImpl: CustomerRepositoryImpl
    ): CustomerRepository

    @Binds
    @Singleton
    abstract fun bindToolRepository(
        toolRepositoryImpl: ToolRepositoryImpl
    ): ToolRepository

    @Binds
    @Singleton
    abstract fun bindRentalOrderRepository(
        rentalOrderRepositoryImpl: RentalOrderRepositoryImpl
    ): RentalOrderRepository
}