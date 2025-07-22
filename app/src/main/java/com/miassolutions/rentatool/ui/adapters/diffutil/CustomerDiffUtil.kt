package com.miassolutions.rentatool.ui.adapters.diffutil

import androidx.recyclerview.widget.DiffUtil
import com.miassolutions.rentatool.data.entities.CustomerEntity

class CustomerDiffUtil : DiffUtil.ItemCallback<CustomerEntity>() {
    override fun areItemsTheSame(oldItem: CustomerEntity, newItem: CustomerEntity): Boolean =
        oldItem.customerId == newItem.customerId

    override fun areContentsTheSame(oldItem: CustomerEntity, newItem: CustomerEntity): Boolean =
        oldItem == newItem

}
