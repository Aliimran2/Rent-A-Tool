package com.miassolutions.rentatool.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.miassolutions.rentatool.core.utils.helper.formattedDate
import com.miassolutions.rentatool.data.model.RentalDetail
import com.miassolutions.rentatool.data.model.Tool
import com.miassolutions.rentatool.databinding.ItemRentalDetailsBinding
import java.util.Date

class RentalDetailAdapter(
    private val tools: List<Tool>,
    private val onItemClick: (RentalDetail) -> Unit
) :
    ListAdapter<RentalDetail, RentalDetailAdapter.RentalDetailViewHolder>(DiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RentalDetailViewHolder {
        val binding =
            ItemRentalDetailsBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return RentalDetailViewHolder(binding)
    }

    override fun onBindViewHolder(holder: RentalDetailViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class RentalDetailViewHolder(private val binding: ItemRentalDetailsBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(detail: RentalDetail) {
            // Fetch tool name from the list based on the toolId
            val tool = tools.find { it.toolId == detail.toolId }
            binding.toolNameTextView.text = tool?.name ?: "Tool Not Found"
            binding.quantityTextView.text = "Quantity: ${detail.quantity}"
            binding.rentalDateTextView.text =
                "Rental Date: ${formattedDate(Date(detail.rentalDate))}"
            binding.root.setOnClickListener { onItemClick(detail) }
        }
    }

    class DiffCallback : DiffUtil.ItemCallback<RentalDetail>() {
        override fun areItemsTheSame(oldItem: RentalDetail, newItem: RentalDetail): Boolean {
            return oldItem.rentalDetailId == newItem.rentalDetailId
        }

        override fun areContentsTheSame(oldItem: RentalDetail, newItem: RentalDetail): Boolean {
            return oldItem == newItem
        }
    }
}


