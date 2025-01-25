package com.miassolutions.rentatool.core.utils.mockdb

import com.miassolutions.rentatool.data.model.Customer
import com.miassolutions.rentatool.data.model.Tool

fun getMockCustomers(): List<Customer> {
    return listOf(
        Customer(
            customerName = "John Doe",
            cnicNumber = "12345-6789012-3",
            customerPhone = "1234567890",
            constructionPlace = "Downtown Building A",
            contractorName = "Alice",
            contractorPhone = "9876543210",
            ownerName = "Bob",
            ownerPhone = "5432167890"
        ),
        Customer(
            customerName = "Jane Smith",
            cnicNumber = "98765-4321098-7",
            customerPhone = "0987654321",
            constructionPlace = "Uptown Villa",
            contractorName = "Charlie",
            contractorPhone = "1231231234",
            ownerName = "Dave",
            ownerPhone = "5678901234"
        ),
        Customer(
            customerName = "Mark Johnson",
            cnicNumber = "13579-2468013-5",
            customerPhone = "5551234567",
            constructionPlace = "City Center Complex",
            contractorName = "Eve",
            contractorPhone = "6667778889",
            ownerName = "Sam",
            ownerPhone = "4445556666"
        ),
        Customer(
            customerName = "Emily Davis",
            cnicNumber = "19283-7465820-9",
            customerPhone = "7778889990",
            constructionPlace = "Greenfield Apartments",
            contractorName = "Tom",
            contractorPhone = "2223334444",
            ownerName = "Chris",
            ownerPhone = "1112223333"
        ),
        Customer(
            customerName = "Paul Anderson",
            cnicNumber = "54879-1234567-2",
            customerPhone = "8889990001",
            constructionPlace = "Blue Lagoon Estates",
            contractorName = "Sophia",
            contractorPhone = "9998887776",
            ownerName = "Nick",
            ownerPhone = "6665554443"
        )
    )
}

fun getMockTools(): List<Tool> {
    return listOf(
        Tool(
            toolId = 1L,
            name = "Hammer",
            rentPerDay = 10.0,
            totalStock = 15,
            availableStock = 12,
            rentedQuantity = 3,
            toolCondition = "New"
        ),
        Tool(
            toolId = 2L,
            name = "Drill Machine",
            rentPerDay = 25.0,
            totalStock = 10,
            availableStock = 8,
            rentedQuantity = 2,
            toolCondition = "Good"
        ),
        Tool(
            toolId = 3L,
            name = "Screwdriver Set",
            rentPerDay = 5.0,
            totalStock = 20,
            availableStock = 18,
            rentedQuantity = 2,
            toolCondition = "New"
        ),
        Tool(
            toolId = 4L,
            name = "Saw",
            rentPerDay = 15.0,
            totalStock = 8,
            availableStock = 5,
            rentedQuantity = 3,
            toolCondition = "Used"
        ),
        Tool(
            toolId = 5L,
            name = "Ladder",
            rentPerDay = 30.0,
            totalStock = 5,
            availableStock = 3,
            rentedQuantity = 2,
            toolCondition = "Good"
        )
    )
}

