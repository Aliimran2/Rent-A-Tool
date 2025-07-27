package com.miassolutions.rentatool.ui.fragments.mainfragments.lists.toolstock

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.miassolutions.rentatool.data.relationship.ToolWithAvailability
import com.miassolutions.rentatool.databinding.ItemStockToolsBinding

class ToolListAdapter : ListAdapter<ToolWithAvailability, ToolListAdapter.ToolVH>(StockDiff()) {


    class ToolVH(private val binding: ItemStockToolsBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: ToolWithAvailability) {
            binding.apply {
                tvToolName.text = item.tool.name
                tvAvailablCount.text = "${item.availableQuantity}/${item.tool.totalQuantity}"
                tvRentPerDay.text = "${item.tool.rentPricePerDay.toInt()} Rs/day"
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ToolVH {
        return ToolVH(
            ItemStockToolsBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ToolVH, position: Int) = holder.bind(getItem(position))

}

class StockDiff : DiffUtil.ItemCallback<ToolWithAvailability>() {
    override fun areItemsTheSame(
        oldItem: ToolWithAvailability,
        newItem: ToolWithAvailability
    ): Boolean {
        return oldItem.tool.toolId == newItem.tool.toolId
    }

    override fun areContentsTheSame(
        oldItem: ToolWithAvailability,
        newItem: ToolWithAvailability
    ): Boolean {
        return oldItem == newItem
    }

}