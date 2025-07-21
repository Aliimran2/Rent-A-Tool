package com.miassolutions.rentatool.core.utils.mockdb

import com.miassolutions.rentatool.data.model.CustomerEntity
import com.miassolutions.rentatool.data.model.ToolEntity

fun getMockCustomers(): List<CustomerEntity> {
    return listOf(
        CustomerEntity(
            customerName = "Ali Khan",
            cnicNumber = "12345-6789012-3",
            customerPhone = "1234567890",
            constructionPlace = "Karachi Heights",
            contractorName = "Ahmed",
            contractorPhone = "9876543210",
            ownerName = "Usman",
            ownerPhone = "5432167890"
        ),
        CustomerEntity(
            customerName = "Ahmed Raza",
            cnicNumber = "98765-4321098-7",
            customerPhone = "0987654321",
            constructionPlace = "Lahore Tower",
            contractorName = "Hassan",
            contractorPhone = "1231231234",
            ownerName = "Fahad",
            ownerPhone = "5678901234"
        ),
        CustomerEntity(
            customerName = "Kashif Malik",
            cnicNumber = "13579-2468013-5",
            customerPhone = "5551234567",
            constructionPlace = "Islamabad Green",
            contractorName = "Zain",
            contractorPhone = "6667778889",
            ownerName = "Shahbaz",
            ownerPhone = "4445556666"
        ),
        CustomerEntity(
            customerName = "Bilal Shah",
            cnicNumber = "19283-7465820-9",
            customerPhone = "7778889990",
            constructionPlace = "Rawalpindi Plaza",
            contractorName = "Omar",
            contractorPhone = "2223334444",
            ownerName = "Imran",
            ownerPhone = "1112223333"
        ),
        CustomerEntity(
            customerPic = "",
            customerName = "Farhan Ali",
            cnicNumber = "54879-1234567-2",
            customerPhone = "8889990001",
            constructionPlace = "Peshawar Villas",
            contractorName = "Sami",
            contractorPhone = "9998887776",
            ownerName = "Nadeem",
            ownerPhone = "6665554443"
        )
    )
}


fun getMockTools(): List<ToolEntity> {
    return listOf(
        ToolEntity(
            toolId = 1L,
            name = "Hammer",
            rentPerDay = 10.0,
            totalStock = 15,
            availableStock = 12,
            rentedQuantity = 3,
            toolCondition = "New"
        ),
        ToolEntity(
            toolId = 2L,
            name = "Drill Machine",
            rentPerDay = 25.0,
            totalStock = 10,
            availableStock = 8,
            rentedQuantity = 2,
            toolCondition = "Good"
        ),
        ToolEntity(
            toolId = 3L,
            name = "Screwdriver Set",
            rentPerDay = 5.0,
            totalStock = 20,
            availableStock = 18,
            rentedQuantity = 2,
            toolCondition = "New"
        ),
        ToolEntity(
            toolId = 4L,
            name = "Saw",
            rentPerDay = 15.0,
            totalStock = 8,
            availableStock = 5,
            rentedQuantity = 3,
            toolCondition = "Used"
        ),
        ToolEntity(
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

