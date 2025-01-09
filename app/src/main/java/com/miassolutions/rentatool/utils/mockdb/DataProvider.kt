package com.miassolutions.rentatool.utils.mockdb

import com.miassolutions.rentatool.data.model.Customer
import com.miassolutions.rentatool.data.model.Rental
import com.miassolutions.rentatool.data.model.RentalDetail
import com.miassolutions.rentatool.data.model.Tool

object DataProvider {

    // Sample tools
    val tools = listOf(
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

    // Sample customers
    val customers = listOf(
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

    // Sample rentals
    val rentals = mutableListOf<Rental>()

    // Sample rental details
    val rentalDetails = mutableListOf<RentalDetail>()

    /**
     * Simulates renting tools by a customer.
     */
    fun rentTools(customerId: Long, toolRentals: List<Pair<Long, Int>>, rentalDate: Long): Rental {
        val rentalId = (rentals.size + 1).toLong()
        val rental = Rental(
            rentalId = rentalId,
            customerId = customerId,
            rentalDate = rentalDate,
            returnDate = null
        )
        rentals.add(rental)

        toolRentals.forEach { (toolId, quantity) ->
            val tool = tools.find { it.toolId == toolId }
                ?: throw IllegalArgumentException("Tool not found with ID: $toolId")
            if (tool.availableStock < quantity) {
                throw IllegalArgumentException("Insufficient stock for tool: ${tool.name}")
            }

            // Update tool stock
            tool.availableStock -= quantity
            tool.rentedQuantity += quantity

            // Add rental detail
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

    /**
     * Simulates returning tools.
     */
    fun returnTools(rentalDetailId: Long, returnQuantity: Int, returnDate: Long): Double {
        val rentalDetail = rentalDetails.find { it.rentalDetailId == rentalDetailId }
            ?: throw IllegalArgumentException("Rental detail not found with ID: $rentalDetailId")

        if (returnQuantity > rentalDetail.quantity) {
            throw IllegalArgumentException("Return quantity exceeds rented quantity")
        }

        val tool = tools.find { it.toolId == rentalDetail.toolId }
            ?: throw IllegalArgumentException("Tool not found with ID: ${rentalDetail.toolId}")

        // Update tool stock
        tool.availableStock += returnQuantity
        tool.rentedQuantity -= returnQuantity

        // Calculate rent
        val rent = calculateRent(
            rentalDetail.rentalDate,
            returnDate,
            rentalDetail.rentPerDay,
            returnQuantity
        )

        if (returnQuantity == rentalDetail.quantity) {
            // Mark rental detail as returned
            rentalDetail.returnDate = returnDate
        } else {
            // Split rental detail for partial return
            rentalDetail.quantity -= returnQuantity
            rentalDetails.add(
                RentalDetail(
                    rentalDetailId = (rentalDetails.size + 1).toLong(),
                    rentalId = rentalDetail.rentalId,
                    toolId = rentalDetail.toolId,
                    quantity = returnQuantity,
                    rentPerDay = rentalDetail.rentPerDay,
                    rentalDate = rentalDetail.rentalDate,
                    returnDate = returnDate
                )
            )
        }

        return rent
    }

    /**
     * Calculates rent based on rental duration and quantity.
     */
    private fun calculateRent(rentalDate: Long, returnDate: Long, rentPerDay: Double, quantity: Int): Double {
        val days = ((returnDate - rentalDate) / (1000 * 60 * 60 * 24)).coerceAtLeast(1)
        return days * rentPerDay * quantity
    }
}
