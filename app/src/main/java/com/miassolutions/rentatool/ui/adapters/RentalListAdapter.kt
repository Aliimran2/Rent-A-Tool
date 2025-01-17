package com.miassolutions.rentatool.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.miassolutions.rentatool.core.utils.helper.formattedDate
import com.miassolutions.rentatool.data.model.Rental
import com.miassolutions.rentatool.databinding.ItemRentalBinding
import java.util.Date

class RentalListAdapter(
    val onClickListener: (Long) -> Unit
) : ListAdapter<Rental, RentalListAdapter.RentalVH>(DIFF_CALLBACK) {
    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<Rental>() {
            override fun areItemsTheSame(oldItem: Rental, newItem: Rental): Boolean {
                return oldItem.rentalId == newItem.rentalId
            }

            override fun areContentsTheSame(oldItem: Rental, newItem: Rental): Boolean {
                return oldItem == newItem
            }

        }
    }

    inner class RentalVH(private val binding: ItemRentalBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(rental: Rental) {
            binding.apply {
                tvRentalId.text = "Rental Id : ${rental.rentalId} Customer Id : ${rental.customerId}"
                tvRentalDate.text = formattedDate(Date(rental.rentalDate))
                root.setOnClickListener { onClickListener(rental.customerId) }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RentalVH {
        return RentalVH(
            ItemRentalBinding.inflate(
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