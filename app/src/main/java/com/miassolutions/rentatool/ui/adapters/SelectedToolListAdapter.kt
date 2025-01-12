package com.miassolutions.rentatool.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.miassolutions.rentatool.data.model.Tool
import com.miassolutions.rentatool.databinding.ItemSelectedToolsBinding
import com.miassolutions.rentatool.databinding.ItemStockToolsBinding

class SelectedToolListAdapter(private val toolsList : List<Tool>) : ListAdapter<Pair<Long, Int>, SelectedToolListAdapter.ToolVH>(SelectedToolDiffUtil()) {


    class ToolVH(private val binding : ItemSelectedToolsBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(tool: Pair<Long, Int>, toolsList: List<Tool>){
            binding.apply {

                val toolName = toolsList.find { it.toolId == tool.first }?.name ?: "Unknown Tool Id"
                binding.toolName.text = toolName
                binding.toolQuantity.text = "${tool.second}"
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ToolVH {
        return ToolVH(ItemSelectedToolsBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: ToolVH, position: Int) = holder.bind(getItem(position), toolsList)

}