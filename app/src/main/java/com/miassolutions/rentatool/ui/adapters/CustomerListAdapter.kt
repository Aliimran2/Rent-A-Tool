package com.miassolutions.rentatool.ui.adapters

import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.miassolutions.rentatool.data.model.Customer
import com.miassolutions.rentatool.databinding.ItemDropDownCustomerBinding

class CustomerListAdapter(
    val itemClickListener : (Customer) -> Unit
) : ListAdapter<Customer, CustomerListAdapter.CustomerVH>(CustomerDiffUtil()){


    inner class CustomerVH(val binding : ItemDropDownCustomerBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(customer: Customer){
            binding.apply {

            }
        }

    }
}