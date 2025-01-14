package com.miassolutions.rentatool.core.utils.mockdb

import com.miassolutions.rentatool.data.model.Customer
import com.miassolutions.rentatool.data.model.Tool

object MockDB {

    // Sample tools and customers
    val tools = listOf(
        Tool(name = "Tool 1", rentPerDay = 10.0, totalStock = 10, availableStock = 10, rentedQuantity = 0),
        Tool(name = "Tool 2", rentPerDay = 15.0, totalStock = 8, availableStock = 8, rentedQuantity = 0),
        Tool(name = "Tool 3", rentPerDay = 20.0, totalStock = 5, availableStock = 5, rentedQuantity = 0),
        Tool(name = "Tool 4", rentPerDay = 25.0, totalStock = 7, availableStock = 7, rentedQuantity = 0),
        Tool(name = "Tool 5", rentPerDay = 30.0, totalStock = 12, availableStock = 12, rentedQuantity = 0)
    )

    val customers = listOf(
        Customer(customerPic = "pic1.jpg", customerName = "Customer 1", cnicNumber = "123456789", customerPhone = "1234567890", constructionPlace = "Place 1", contractorName = "Contractor 1", contractorPhone = "1112223333", ownerName = "Owner 1", ownerPhone = "1239876543"),
        Customer(customerPic = "pic2.jpg", customerName = "Customer 2", cnicNumber = "234567890", customerPhone = "2345678901", constructionPlace = "Place 2", contractorName = "Contractor 2", contractorPhone = "2233445566", ownerName = "Owner 2", ownerPhone = "2348765432"),
        Customer(customerPic = "pic3.jpg", customerName = "Customer 3", cnicNumber = "345678901", customerPhone = "3456789012", constructionPlace = "Place 3", contractorName = "Contractor 3", contractorPhone = "3344556677", ownerName = "Owner 3", ownerPhone = "3457654321"),
        Customer(customerPic = "pic4.jpg", customerName = "Customer 4", cnicNumber = "456789012", customerPhone = "4567890123", constructionPlace = "Place 4", contractorName = "Contractor 4", contractorPhone = "4455667788", ownerName = "Owner 4", ownerPhone = "4566543210"),
        Customer(customerPic = "pic5.jpg", customerName = "Customer 5", cnicNumber = "567890123", customerPhone = "5678901234", constructionPlace = "Place 5", contractorName = "Contractor 5", contractorPhone = "5566778899", ownerName = "Owner 5", ownerPhone = "5675432109")
    )
}