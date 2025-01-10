package com.miassolutions.rentatool.utils.mockdb

import com.miassolutions.rentatool.data.model.Customer
import com.miassolutions.rentatool.data.model.Rental
import com.miassolutions.rentatool.data.model.RentalDetail
import com.miassolutions.rentatool.data.model.Tool

object DataProvider {

    private val _tools = mutableListOf(
        Tool(
            toolId = 1,
            name = "Hammer",
            rentPerDay = 10.0,
            totalStock = 50,
            availableStock = 50,
            rentedQuantity = 0
        ),
        Tool(
            toolId = 2,
            name = "Drill",
            rentPerDay = 15.0,
            totalStock = 30,
            availableStock = 30,
            rentedQuantity = 0
        ),
        Tool(
            toolId = 3,
            name = "Saw",
            rentPerDay = 20.0,
            totalStock = 20,
            availableStock = 20,
            rentedQuantity = 0
        )
    )
    val tools: List<Tool> get() = _tools

    private val _customers = mutableListOf(
        Customer(
            customerId = 1,
            name = "John Doe",
            contact = "1234567890"
        ),
        Customer(
            customerId = 2,
            name = "Jane Smith",
            contact = "9876543210"
        ),
        Customer(
            customerId = 3,
            name = "Alice Johnson",
            contact = "5678901234"
        )
    )
    val customers: List<Customer> get() = _customers

    fun addTool(tool: Tool) {
        _tools.add(tool)
    }

    fun updateTool(updatedTool: Tool) {

    }

    fun addCustomer(customer: Customer) {
        _customers.add(customer)
    }

    fun updateCustomer(updatedCustomer: Customer) {

    }

    val rentals = mutableListOf<Rental>()
    val rentalDetails = mutableListOf<RentalDetail>()

    fun rentTools(customerId: Long, toolRentals: List<Pair<Long, Int>>, rentalDate: Long): Rental {
        val rentalId = (rentals.size + 1).toLong() //for mock id

        val rental = Rental(
            rentalId = rentalId,
            customerId = customerId,
            rentalDate = rentalDate,
            returnDate = null
        )
        rentals.add(rental)

        toolRentals.forEach { (toolId, quantity) ->
            val tool = tools.find { it.toolId == toolId }
                ?: throw IllegalArgumentException("Tool not found with ID : $toolId")
            if (tool.availableStock < quantity) {
                throw IllegalArgumentException("Insufficient stock for tool : ${tool.name}")
            }

            tool.availableStock -= quantity
            tool.rentedQuantity += quantity

            rentalDetails.add(
                RentalDetail(
                    rentalDetailId = (rentalDetails.size + 1).toLong(),
                    rentalId = rentalId,
                    toolId = toolId,
                    quantity = quantity,
                    rentPerDay = tool.rentPerDay,
                    rentalDate = rentalDate,
                    returnDate = null
                )
            )
        }
        return rental
    }

    fun returnTools(rentalDetailId: Long, returnQuantity: Int, returnDate: Long): Double {
        val rentalDetail = rentalDetails.find { it.rentalDetailId == rentalDetailId }
            ?: throw IllegalArgumentException("Not Found with ID : $rentalDetailId")

        if (returnQuantity > rentalDetail.quantity) {
            throw IllegalArgumentException("Return exceeds than rented")
        }

        val tool = tools.find { it.toolId == rentalDetail.toolId }
            ?: throw IllegalArgumentException("Tool not found")

        tool.availableStock += returnQuantity
        tool.rentedQuantity -= returnQuantity

        val rent = calculateRent(
            rentalDetail.rentalDate,
            returnDate,
            rentalDetail.rentPerDay,
            returnQuantity
        )

        if (returnQuantity == rentalDetail.quantity) {
            rentalDetail.returnDate = returnDate
        } else {
            rentalDetail.quantity -= returnQuantity
            rentalDetails.add(
                RentalDetail(
                    rentalDetailId = (rentalDetails.size + 1).toLong(),
                    rentalId = rentalDetail.rentalId,
                    toolId = tool.toolId,
                    quantity = returnQuantity,
                    rentPerDay = rentalDetail.rentPerDay,
                    rentalDate = rentalDetail.rentalDate,
                    returnDate = returnDate
                )
            )
        }

        return rent

    }

    private fun calculateRent(
        rentalDate: Long,
        returnDate: Long,
        rentPerDay: Double,
        returnQuantity: Int
    ): Double {
        val day = ((returnDate - rentalDate) / 86_400_000).coerceAtLeast(1)
        return day * rentPerDay * returnQuantity

    }
}

