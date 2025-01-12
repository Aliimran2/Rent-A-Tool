package com.miassolutions.rentatool.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.miassolutions.rentatool.data.model.Tool
import com.miassolutions.rentatool.databinding.ItemSelectedToolsBinding
import com.miassolutions.rentatool.databinding.ItemStockToolsBinding

class SelectedToolListAdapter : ListAdapter<Pair<Long, Int>, SelectedToolListAdapter.ToolVH>(SelectedToolDiffUtil()) {

    // Instead of passing the tools list through the constructor, we will use the submitList() method to update the list.
    private var toolsList: List<Tool> = emptyList()

    // Update toolsList when data is changed.
    fun updateToolsList(tools: List<Tool>) {
        toolsList = tools
        notifyDataSetChanged() // Notify that toolsList has been updated.
    }

    class ToolVH(private val binding: ItemSelectedToolsBinding) : RecyclerView.ViewHolder(binding.root) {

        // The bind method uses the toolsList to find the tool name.
        fun bind(tool: Pair<Long, Int>, toolsList: List<Tool>) {
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

    override fun onBindViewHolder(holder: ToolVH, position: Int) {
        holder.bind(getItem(position), toolsList)
    }
}
