package com.miassolutions.rentatool.ui.fragments.confirmation.rentconfirmation


import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.miassolutions.rentatool.databinding.ItemSelectedToolBinding
import com.miassolutions.rentatool.ui.fragments.toolselection.SelectedTool


class SelectedToolAdapter : ListAdapter<SelectedTool, SelectedToolAdapter.ToolViewHolder>(DiffCallback) {

    inner class ToolViewHolder(private val binding: ItemSelectedToolBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(tool: SelectedTool) {
            binding.tvToolName.text = tool.toolName
            binding.tvToolQuantity.text = "Qty: ${tool.quantity}"
            binding.tvToolRate.text = "Rs. ${tool.rentPricePerDay}/day"
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ToolViewHolder {
        val binding = ItemSelectedToolBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ToolViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ToolViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    companion object {
        private val DiffCallback = object : DiffUtil.ItemCallback<SelectedTool>() {
            override fun areItemsTheSame(oldItem: SelectedTool, newItem: SelectedTool): Boolean {
                return oldItem.toolId == newItem.toolId
            }

            override fun areContentsTheSame(oldItem: SelectedTool, newItem: SelectedTool): Boolean {
                return oldItem == newItem
            }
        }
    }
}
