package com.miassolutions.rentatool.data

import com.miassolutions.rentatool.core.AppDatabase
import com.miassolutions.rentatool.data.model.Customer
import com.miassolutions.rentatool.data.model.Rental
import com.miassolutions.rentatool.data.model.RentalDetail
import com.miassolutions.rentatool.data.model.Tool

class ToolRentalRepository(private  val db : AppDatabase) {

    //tool functions
    fun getToolById(toolId:Long) = db.toolDao().getToolById(toolId)
    fun searchToolsByName(query: String) = db.toolDao().searchTools(query)
    suspend fun addTool(tool: Tool) : Result<Unit> {
        return try {
            db.toolDao().addTool(tool)
            Result.success(Unit)
        } catch (e : Exception){
            Result.failure(e)
        }
    }

    suspend fun updateTool(tool: Tool) = db.toolDao().updateTool(tool)
    fun getAllTools() = db.toolDao().getAllTools()

    // Customer functions
    fun getCustomerById(customerId: Long) = db.customerDao().getCustomerById(customerId)
    fun searchCustomerByName(query: String) = db.customerDao().searchCustomers(query)
    suspend fun addCustomer(customer: Customer): Result<Unit> {
        return try {
            db.customerDao().addCustomer(customer)
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    suspend fun updateCustomer(customer: Customer) = db.customerDao().updateCustomer(customer)
    fun getAllCustomers() = db.customerDao().getAllCustomers()


    // Rental functions
    fun getRentalById(rentalId: Long) = db.rentalDao().getRentalById(rentalId)
    fun searchRentalsByCustomer(customerId: Long) = db.rentalDao().searchRentalsByCustomer(customerId)
    suspend fun addRental(rental: Rental) = db.rentalDao().addRental(rental)

    // Rental Detail functions
    fun getRentalDetailById(rentalDetailId: Long) = db.rentalDetailDao().getRentalDetailById(rentalDetailId)
    fun searchRentalDetailsByRental(rentalId: Long) = db.rentalDetailDao().searchRentalDetailsByRental(rentalId)
    suspend fun addRentalDetail(rentalDetail: RentalDetail) = db.rentalDetailDao().addRentalDetail(rentalDetail)
}