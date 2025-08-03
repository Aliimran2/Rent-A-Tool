package com.miassolutions.rentatool.ui.fragments.mainfragments.lists.rentalrecord

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.miassolutions.rentatool.data.relationship.RentalOrderWithRentedTools
import com.miassolutions.rentatool.databinding.ItemRentalOrderBinding
import com.miassolutions.rentatool.utils.extenstions.toFormattedDate

class RentalListAdapter(
        private val onItemClicked: (Long) ->Unit
) : ListAdapter<RentalOrderWithRentedTools, RentalListAdapter.RentalVH>(DIFF_CALLBACK) {
    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<RentalOrderWithRentedTools>() {
            override fun areItemsTheSame(
                oldItem: RentalOrderWithRentedTools,
                newItem: RentalOrderWithRentedTools
            ): Boolean {
                return oldItem.rentalOrder.orderId == newItem.rentalOrder.orderId
            }

            override fun areContentsTheSame(
                oldItem: RentalOrderWithRentedTools,
                newItem: RentalOrderWithRentedTools
            ): Boolean {
                return oldItem == newItem
            }

        }
    }

    inner class RentalVH(private val binding: ItemRentalOrderBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: RentalOrderWithRentedTools) {
            binding.apply {
                tvOrderId.text = "Order Id: ${item.rentalOrder.orderId}"
                tvOrderDate.text = "Rent Date: ${item.rentalOrder.rentDate.toFormattedDate()}"
                tvEstimatedReturn.text = "Est. Return Date: ${item.rentalOrder.rentDate.toFormattedDate()}" //todo()
                tvRentAmount.text = "Rent Amount: Rs. ${item.rentalOrder.totalAmount}"
                tvOrderStatus.text = if(item.rentalOrder.isClosed) "Active" else "Closed"

                btnReturnTools.setOnClickListener {
                    onItemClicked(item.rentalOrder.customerId)
                }



            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RentalVH {
        return RentalVH(
            ItemRentalOrderBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: RentalVH, position: Int) {
        holder.bind(getItem(position))
    }
}