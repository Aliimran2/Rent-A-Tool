package com.miassolutions.rentatool.ui.adapters

import android.annotation.SuppressLint
import android.app.Activity
import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.miassolutions.rentatool.R
import com.miassolutions.rentatool.core.utils.helper.PermissionUtils
import com.miassolutions.rentatool.data.model.Customer
import com.miassolutions.rentatool.databinding.ItemCustomerBinding

class CustomerListAdapter(
    val dialerClickListener: (Customer) -> Unit,
    val navigationClickListener: (Customer) -> Unit
) : ListAdapter<Customer, CustomerListAdapter.CustomerVH>(CustomerDiffUtil()) {


    inner class CustomerVH(private val binding: ItemCustomerBinding) :
        RecyclerView.ViewHolder(binding.root) {

        @SuppressLint("InlinedApi")
        fun bind(customer: Customer) {
            binding.apply {
                tvCustomerName.text = customer.customerName
                tvConstructionPlace.text = "Mock City"
                tvCustomerPhone.text = customer.customerPhone

                // Check for permissions before accessing the URI
                if (PermissionUtils.hasPermissions(
                        binding.root.context,
                        arrayOf(
                            android.Manifest.permission.READ_EXTERNAL_STORAGE,
                            android.Manifest.permission.READ_MEDIA_IMAGES // For Android 13+
                        )
                    )
                ) {
                    // Permissions granted, proceed to set the image
                    if (customer.customerPic.isNotEmpty()) {
                        val customerPicUri = Uri.parse(customer.customerPic)
                        Glide.with(binding.root.context)
                            .load(customerPicUri) // Load the image URI
                            .placeholder(R.drawable.place_holder_image) // Placeholder while loading
                            .error(R.drawable.place_holder_image) // Fallback for errors
                            .into(ivCustomer) // Set the image into the ImageView
                    } else {
                        ivCustomer.setImageResource(R.drawable.place_holder_image)
                    }
                } else {
                    // Permissions not granted, show placeholder image
                    ivCustomer.setImageResource(R.drawable.place_holder_image)

                    // Optionally, request permissions
                    PermissionUtils.requestPermissions(
                        (binding.root.context as Activity),
                        arrayOf(
                            android.Manifest.permission.READ_EXTERNAL_STORAGE,
                            android.Manifest.permission.READ_MEDIA_IMAGES // For Android 13+
                        )
                    )
                }

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