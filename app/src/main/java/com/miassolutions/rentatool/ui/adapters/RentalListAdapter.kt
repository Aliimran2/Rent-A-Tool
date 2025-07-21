package com.miassolutions.rentatool.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.miassolutions.rentatool.data.model.RentalEntity
import com.miassolutions.rentatool.databinding.ItemRentalBinding

class RentalListAdapter(
    val onClickListener: (Long) -> Unit
) : ListAdapter<RentalEntity, RentalListAdapter.RentalVH>(DIFF_CALLBACK) {
    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<RentalEntity>() {
            override fun areItemsTheSame(oldItem: RentalEntity, newItem: RentalEntity): Boolean {
                return oldItem.rentalId == newItem.rentalId
            }

            override fun areContentsTheSame(oldItem: RentalEntity, newItem: RentalEntity): Boolean {
                return oldItem == newItem
            }

        }
    }

    inner class RentalVH(private val binding: ItemRentalBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(rentalEntity: RentalEntity) {
            binding.apply {

                root.setOnClickListener { onClickListener(rentalEntity.rentalId) }
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