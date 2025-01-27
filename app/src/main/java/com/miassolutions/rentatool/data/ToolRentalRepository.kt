package com.miassolutions.rentatool.data

import androidx.lifecycle.LiveData
import com.miassolutions.rentatool.core.AppDatabase
import com.miassolutions.rentatool.data.model.Customer
import com.miassolutions.rentatool.data.model.Rental
import com.miassolutions.rentatool.data.model.RentalDetail
import com.miassolutions.rentatool.data.model.Tool
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class ToolRentalRepository(
    private val db: AppDatabase
) {

    private val toolDao = db.toolDao()
    private val customerDao = db.customerDao()
    private val rentalDao = db.rentalDao()
    private val rentalDetailDao = db.rentalDetailDao()


    // Calculate current rent based on returned tools so far
    suspend fun calculateCurrentRent(details: List<RentalDetail>, returnDate: Long): Double {
        return details.sumOf { detail ->
            if (detail.returnedQuantity > 0) { // Rent only for returned tools
                val duration = ((returnDate - detail.returnDate!!) / (1000 * 60 * 60 * 24)).toInt() // Days
                detail.returnedQuantity * detail.rentPerDay * duration
            } else {
                0.0
            }
        }
    }

    // Calculate total rent when all tools are returned
    suspend fun calculateTotalRent(details: List<RentalDetail>, returnDate: Long): Double {
        return details.sumOf { detail ->
            val duration = (returnDate - detail.returnDate!!) / (1000 * 60 * 60 * 24) // Days
            detail.returnedQuantity * detail.rentPerDay * duration
        }
    }


    // Calculate total rent based on the rental details
//    private fun calculateTotalRent(rentalDetails: List<RentalDetail>, returnDate: Long): Double {
//        var totalRent = 0.0
//        for (detail in rentalDetails) {
//            val daysRented = ((returnDate - detail.returnDate!!) / (1000 * 60 * 60 * 24)).toInt()
//            totalRent += daysRented * detail.rentPerDay * detail.returnedQuantity
//        }
//        return totalRent
//    }

    // Handle tool return and rental finalization
    suspend fun returnTool(rentalDetailId: Long, returnQuantity: Int, returnDate: Long) {
        val detail = rentalDetailDao.getRentalDetailById(rentalDetailId)
        val newQuantity = detail.returnedQuantity + returnQuantity
        val isFullyReturned = newQuantity == detail.rentedQuantity

        // Update the return details in the RentalDetail table
        rentalDetailDao.updateReturnDetails(
            rentalDetailId = rentalDetailId,
            returnedQuantity = newQuantity,
            isReturned = isFullyReturned,
            returnDate = returnDate
        )

        // Check if all tools in the rental are returned
        val rentalDetails = rentalDetailDao.getRentalDetailsByRentalIdDirect(detail.rentalId)
        if (rentalDetails.all { it.isReturned }) {
            val totalRent = calculateTotalRent(rentalDetails, returnDate)

            // Finalize the rental
            rentalDao.finalizeRental(detail.rentalId, totalRent, returnDate, true)

            // Update customer's total rent
            val rental = rentalDao.getRentalById(detail.rentalId)
            customerDao.updateCustomerTotalRent(rental.customerId, totalRent)
        }
    }




    suspend fun updateRentalDetail(rentalDetail: RentalDetail) {
        rentalDetailDao.updateRentalDetail(rentalDetail)
    }

    //will be deleted later todo()
    suspend fun insertCustomers(customers: List<Customer>) {
        customerDao.insertCustomers(customers)
    }

    suspend fun insertTools(tools : List<Tool>) = toolDao.insertAll(tools)

    // Fetch all tools
    fun getAllTools(): LiveData<List<Tool>> = toolDao.getAllTools()

    suspend fun getToolByIdDirect(toolId:Long) : Tool? {
        return toolDao.getToolByIdDirect(toolId)
    }

    // Fetch all customers
    fun getAllCustomers(): LiveData<List<Customer>> = customerDao.getAllCustomers()

//    suspend fun deleteCustomer(customer: Customer) = customerDao.deleteCustomer(customer)

    // Fetch all rentals by customerId
    fun rentalsByCustomer(customerId: Long): LiveData<List<Rental>> =
        rentalDao.rentalsByCustomer(customerId)

    // Fetch all rental details by rentalId
    fun rentalDetailsByRental(rentalId: Long): LiveData<List<RentalDetail>> =
        rentalDetailDao.getRentalDetailsByRentalId(rentalId)

    suspend fun isToolExists(toolName: String): Boolean {
        return toolDao.getToolByName(toolName) != null
    }



    // Add a new tool
    suspend fun insertTool(tool: Tool): Result<Unit> {
        return withContext(Dispatchers.IO) {
            try {
                toolDao.insertTool(tool) // Use insert for adding a new tool
                Result.success(Unit) // Return success with no additional data
            } catch (e: Exception) {
                Result.failure(e) // Return failure in case of an exception
            }
        }
    }

    // Add a new customer
    suspend fun insertCustomer(customer: Customer): Result<Unit> {
        return withContext(Dispatchers.IO) {
            try {
                customerDao.insertCustomer(customer) // Insert customer into the database
                Result.success(Unit) // Return success with no additional data
            } catch (e: Exception) {
                Result.failure(e) // Return failure in case of an exception
            }
        }
    }

    suspend fun updateToolStock(toolId: Long, availableStock: Int, rentedQuantity: Int) {
        toolDao.updateStock(toolId, availableStock, rentedQuantity)
    }

    // Update an existing customer
    suspend fun updateCustomer(customer: Customer) {
        withContext(Dispatchers.IO) {
            customerDao.updateCustomer(customer) // Update customer in the database
        }
    }

    // Add a new rental
    suspend fun insertRental(rental: Rental): Long {
        return withContext(Dispatchers.IO) {
            rentalDao.addRental(rental) // Insert rental into the database
        }
    }

    // Add a rental detail
    suspend fun insertRentalDetails(rentalDetail: RentalDetail) {
        withContext(Dispatchers.IO) {
            rentalDetailDao.addRentalDetail(rentalDetail) // Insert rental detail into the database
        }
    }

    // Fetch a tool by ID
    fun getToolById(toolId: Long): LiveData<Tool?> = toolDao.getToolById(toolId)


    // Fetch a customer by ID
    suspend fun getCustomerById(customerId: Long): Customer? =
        customerDao.getCustomerById(customerId)

    // Fetch a rental detail by ID
    suspend fun getRentalDetailById(rentalDetailId: Long): RentalDetail {
        return withContext(Dispatchers.IO) {
            rentalDetailDao.getRentalDetailById(rentalDetailId) // Fetch rental detail by ID from the database
        }
    }

    fun getAllRentals(): LiveData<List<Rental>> = rentalDao.getAllRentals()
}
