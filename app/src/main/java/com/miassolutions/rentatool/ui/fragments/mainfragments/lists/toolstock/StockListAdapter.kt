package com.miassolutions.rentatool.ui.fragments.mainfragments.lists.toolstock

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.miassolutions.rentatool.data.relationship.ToolWithAvailability
import com.miassolutions.rentatool.databinding.ItemStockToolsBinding

class ToolListAdapter : ListAdapter<ToolUiModel, ToolListAdapter.ToolVH>(StockDiff()) {


    class ToolVH(private val binding: ItemStockToolsBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: ToolUiModel) {
            binding.apply {
                tvToolName.text = item.name
                tvAvailablCount.text = "${item.availability} "
                tvRentPerDay.text = "${item.rentPerDay} Rs/day"
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

class StockDiff : DiffUtil.ItemCallback<ToolUiModel>() {
    override fun areItemsTheSame(oldItem: ToolUiModel, newItem: ToolUiModel): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: ToolUiModel, newItem: ToolUiModel): Boolean {
        return oldItem == newItem
    }


}