package com.miassolutions.rentatool.ui.adapters

import android.annotation.SuppressLint
import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.miassolutions.rentatool.R
import com.miassolutions.rentatool.data.model.CustomerEntity
import com.miassolutions.rentatool.databinding.ItemCustomerBinding
import com.miassolutions.rentatool.ui.adapters.diffutil.CustomerDiffUtil
import java.util.Locale

class CustomerListAdapter(
    val navigationClickListener: (CustomerEntity) -> Unit,
    val navigateToDetailsListener : (CustomerEntity) -> Unit
) : ListAdapter<CustomerEntity, CustomerListAdapter.CustomerVH>(CustomerDiffUtil()) {




    inner class CustomerVH(private val binding: ItemCustomerBinding) :
        RecyclerView.ViewHolder(binding.root) {

        @SuppressLint("InlinedApi")
        fun bind(customerEntity: CustomerEntity) {
            binding.apply {
                tvCustomerName.text = customerEntity.customerName

                tvCustomerPhone.text = String.format(Locale.getDefault(),"Rs. %d",customerEntity.customerId)

                if (customerEntity.customerPic.isNotEmpty()) {
                    val customerPicUri = Uri.parse(customerEntity.customerPic)
                    ivCustomer.setImageURI(customerPicUri)
                } else {
                    ivCustomer.setImageResource(R.drawable.place_holder_image)
                }

                root.setOnClickListener {
                    navigationClickListener(customerEntity)
                }

                root.setOnLongClickListener {
                    navigateToDetailsListener(customerEntity)
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