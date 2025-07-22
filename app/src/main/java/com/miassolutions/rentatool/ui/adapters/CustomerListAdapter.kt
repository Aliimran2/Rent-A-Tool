package com.miassolutions.rentatool.ui.adapters

import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.miassolutions.rentatool.R
import com.miassolutions.rentatool.data.model.CustomerEntity
import com.miassolutions.rentatool.databinding.ItemCustomerBinding
import com.miassolutions.rentatool.ui.adapters.diffutil.CustomerDiffUtil

class CustomerListAdapter(
    val navToRentals: (CustomerEntity) -> Unit,
    val navToDetails: (CustomerEntity) -> Unit
) : ListAdapter<CustomerEntity, CustomerListAdapter.CustomerVH>(CustomerDiffUtil()) {


    inner class CustomerVH(private val binding: ItemCustomerBinding) :
        RecyclerView.ViewHolder(binding.root) {


        fun bind(customerEntity: CustomerEntity) {
            binding.apply {
                tvCustomerName.text = customerEntity.customerName

                if ((customerEntity.customerId % 2).toInt() == 0) {
                    tvStatus.setBackgroundResource(R.drawable.active_bg)
                    tvStatus.text = "Active"
                } else {
                    tvStatus.setBackgroundResource(R.drawable.inactive_bg)
                    tvStatus.text = "Inactive"
                }

                if (customerEntity.customerPic.isNotEmpty()) {
                    val customerPicUri = Uri.parse(customerEntity.customerPic)
                    ivCustomer.setImageURI(customerPicUri)
                } else {
                    ivCustomer.setImageResource(R.drawable.place_holder_image)
                }

                root.setOnClickListener {
                    navToRentals(customerEntity)
                }

                root.setOnLongClickListener {
                    navToDetails(customerEntity)
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

    override fun onBindViewHolder(holder: CustomerVH, position: Int) =
        holder.bind(getItem(position))
}