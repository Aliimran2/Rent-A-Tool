package com.miassolutions.rentatool.ui.adapters.diffutil

import androidx.recyclerview.widget.DiffUtil
import com.miassolutions.rentatool.data.model.Tool

class ToolDiffUtil : DiffUtil.ItemCallback<Tool>() {
    override fun areItemsTheSame(oldItem: Tool, newItem: Tool): Boolean = oldItem.toolId == newItem.toolId


    override fun areContentsTheSame(oldItem: Tool, newItem: Tool): Boolean = oldItem == newItem

}