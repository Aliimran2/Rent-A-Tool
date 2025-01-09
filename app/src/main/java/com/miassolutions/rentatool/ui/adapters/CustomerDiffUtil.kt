package com.miassolutions.rentatool.ui.adapters

import androidx.recyclerview.widget.DiffUtil
import com.miassolutions.rentatool.data.model.Customer

class CustomerDiffUtil : DiffUtil.ItemCallback<Customer>() {
    override fun areItemsTheSame(oldItem: Customer, newItem: Customer): Boolean =
        oldItem.customerId == newItem.customerId

    override fun areContentsTheSame(oldItem: Customer, newItem: Customer): Boolean =
        oldItem == newItem

}
