package com.miassolutions.rentatool.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.miassolutions.rentatool.data.model.Tool
import com.miassolutions.rentatool.databinding.ItemStockToolsBinding

class ToolListAdapter : ListAdapter<Tool, ToolListAdapter.ToolVH>(ToolDiffUtil()) {


    class ToolVH(private val binding : ItemStockToolsBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(tool: Tool){
            binding.apply {
                tvToolName.text = tool.name
                tvAvailablCount.text = "${tool.availableStock}/${tool.totalStock}"
                tvRentPerDay.text = "${tool.rentPerDay.toInt()} Rs/day"
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ToolVH {
        return ToolVH(ItemStockToolsBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: ToolVH, position: Int) = holder.bind(getItem(position))

}