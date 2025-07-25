package com.miassolutions.rentatool.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.miassolutions.rentatool.R
import com.miassolutions.rentatool.data.entities.CustomerEntity
import com.miassolutions.rentatool.databinding.ItemCustomerBinding
import com.miassolutions.rentatool.ui.adapters.diffutil.CustomerDiffUtil

class CustomerListAdapter(
    val navToRentals: (CustomerEntity) -> Unit,
    val navToDetails: (CustomerEntity) -> Unit,
    val navToRentTools: (CustomerEntity) -> Unit,
    val navToLedger : (CustomerEntity) -> Unit
) : ListAdapter<CustomerEntity, CustomerListAdapter.CustomerVH>(CustomerDiffUtil()) {


    inner class CustomerVH(private val binding: ItemCustomerBinding) :
        RecyclerView.ViewHolder(binding.root) {


        fun bind(customer: CustomerEntity) {
            binding.apply {
                tvCustomerName.text = customer.customerName

                btnSelectTools.setOnClickListener {
                    navToRentTools(customer)
                }

                btnLedger.setOnClickListener {
                    navToLedger(customer)
                }

                btnRentals.setOnClickListener {
                    navToRentals(customer)
                }

                ivCustomer.setOnClickListener {
                    navToDetails(customer)
                }




            }
        }


    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CustomerVH {
        val inflater = LayoutInflater.from(parent.context)
        val mBinding = ItemCustomerBinding.inflate(inflater, parent, false)
        return CustomerVH(mBinding)
    }

    override fun onBindViewHolder(holder: CustomerVH, position: Int) =
        holder.bind(getItem(position))
}