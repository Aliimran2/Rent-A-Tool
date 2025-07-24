package com.miassolutions.rentatool.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.miassolutions.rentatool.data.entities.ToolEntity
import com.miassolutions.rentatool.databinding.ItemStockToolsBinding

class ToolListAdapter : ListAdapter<ToolEntity, ToolListAdapter.ToolVH>(StockDiff()) {


    class ToolVH(private val binding: ItemStockToolsBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(toolEntity: ToolEntity) {
            binding.apply {
                tvToolName.text = toolEntity.name
                toolCondition.text = toolEntity.condition
                tvRentPerDay.text = "${toolEntity.rentPricePerDay.toInt()} Rs/day"
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

class StockDiff : DiffUtil.ItemCallback<ToolEntity>() {
    override fun areItemsTheSame(oldItem: ToolEntity, newItem: ToolEntity): Boolean {
        return oldItem.toolId == newItem.toolId
    }

    override fun areContentsTheSame(oldItem: ToolEntity, newItem: ToolEntity): Boolean {
        return oldItem == newItem
    }
}