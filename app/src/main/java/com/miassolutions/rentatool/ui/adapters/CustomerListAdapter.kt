package com.miassolutions.rentatool.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.miassolutions.rentatool.data.model.Customer
import com.miassolutions.rentatool.databinding.ItemCustomerBinding

class CustomerListAdapter(
    val dialerClickListener: (Customer) -> Unit,
    val navigationClickListener: (Customer) -> Unit
) : ListAdapter<Customer, CustomerListAdapter.CustomerVH>(CustomerDiffUtil()) {


    inner class CustomerVH(private val binding: ItemCustomerBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(customer: Customer) {
            binding.apply {
                tvCustomerName.text = customer.customerName
                tvConstructionPlace.text = "Mock City"
                tvCustomerPhone.text = customer.customerPhone
                ivPhone.setOnClickListener {
                    dialerClickListener(customer)
                }
                ivCustomer.setOnLongClickListener {
                    navigationClickListener(customer)
                    true
                }

            }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CustomerVH {
        val inflater = LayoutInflater.from(parent.context)
        val mBinding = ItemCustomerBinding.inflate(inflater, parent, false)
        return CustomerVH(mBinding)
    }

    override fun onBindViewHolder(holder: CustomerVH, position: Int) = holder.bind(getItem(position))
}