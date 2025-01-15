package com.miassolutions.rentatool.ui.adapters

import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.miassolutions.rentatool.R
import com.miassolutions.rentatool.data.model.Customer
import com.miassolutions.rentatool.databinding.ItemDropDownCustomerBinding
import com.miassolutions.rentatool.ui.adapters.diffutil.CustomerDiffUtil

class CustomerSelectionListAdapter(
    val onClickListener: (Customer) -> Unit,
) : ListAdapter<Customer, CustomerSelectionListAdapter.CustomerVH>(CustomerDiffUtil()) {


    inner class CustomerVH(private val binding: ItemDropDownCustomerBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(customer: Customer) {
            binding.apply {
                tvCustomerName.text = customer.customerName

                if (customer.customerPic.isNotEmpty()) {
                    val customerPicUri = Uri.parse(customer.customerPic)
                    ivCustomer.setImageURI(customerPicUri)
                } else {
                    ivCustomer.setImageResource(R.drawable.place_holder_image)
                }

                root.setOnClickListener {
                    onClickListener(customer)
                }


            }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CustomerVH {
        val inflater = LayoutInflater.from(parent.context)
        val mBinding = ItemDropDownCustomerBinding.inflate(inflater, parent, false)
        return CustomerVH(mBinding)
    }

    override fun onBindViewHolder(holder: CustomerVH, position: Int) =
        holder.bind(getItem(position))
}