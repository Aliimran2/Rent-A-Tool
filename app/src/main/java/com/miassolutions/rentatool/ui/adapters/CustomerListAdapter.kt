package com.miassolutions.rentatool.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.miassolutions.rentatool.data.model.Customer
import com.miassolutions.rentatool.databinding.ItemCustomerBinding
import com.miassolutions.rentatool.databinding.ItemDropDownCustomerBinding

class CustomerListAdapter(
    val itemClickListener: (Customer) -> Unit
) : ListAdapter<Customer, CustomerListAdapter.CustomerVH>(CustomerDiffUtil()) {


    inner class CustomerVH(private val binding: ItemCustomerBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(customer: Customer) {
            binding.apply {
                tvCustomerName.text = customer.name
                tvConstructionPlace.text = "Mock City"
                tvCustomerPhone.text = customer.contact
                ivPhone.setOnClickListener {
                    Toast.makeText(root.context, "Calling function handle later", Toast.LENGTH_SHORT).show()
                }
                ivCustomer.setOnClickListener {
                    itemClickListener(customer)
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