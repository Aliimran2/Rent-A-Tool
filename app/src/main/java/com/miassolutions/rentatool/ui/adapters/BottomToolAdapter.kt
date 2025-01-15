package com.miassolutions.rentatool.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.miassolutions.rentatool.data.model.Tool
import com.miassolutions.rentatool.databinding.ItemDropDownToolBinding
import com.miassolutions.rentatool.ui.adapters.diffutil.ToolDiffUtil

class BottomToolAdapter : ListAdapter<Tool, BottomToolAdapter.BottomToolVH>(ToolDiffUtil()) {


    inner class BottomToolVH(private val binding: ItemDropDownToolBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(tool: Tool) {
            binding.apply {
                tvToolName.text = tool.name
                inputLayout.helperText = tool.availableStock.toString()
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BottomToolVH {
        return BottomToolVH(
            ItemDropDownToolBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: BottomToolVH, position: Int) {
        TODO("Not yet implemented")
    }
}