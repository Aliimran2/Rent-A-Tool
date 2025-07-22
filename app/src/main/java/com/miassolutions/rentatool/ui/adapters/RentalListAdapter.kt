package com.miassolutions.rentatool.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.miassolutions.rentatool.data.model.RentalOrderEntity
import com.miassolutions.rentatool.databinding.ItemRentalBinding

class RentalListAdapter(
    val onClickListener: (Long) -> Unit
) : ListAdapter<RentalOrderEntity, RentalListAdapter.RentalVH>(DIFF_CALLBACK) {
    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<RentalOrderEntity>() {
            override fun areItemsTheSame(
                oldItem: RentalOrderEntity,
                newItem: RentalOrderEntity
            ): Boolean {
                return oldItem.orderId == newItem.orderId
            }

            override fun areContentsTheSame(
                oldItem: RentalOrderEntity,
                newItem: RentalOrderEntity
            ): Boolean {
                return oldItem == newItem
            }

        }
    }

    inner class RentalVH(private val binding: ItemRentalBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(rentalEntity: RentalOrderEntity) {
            binding.apply {

                root.setOnClickListener { onClickListener(rentalEntity.orderId) }
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