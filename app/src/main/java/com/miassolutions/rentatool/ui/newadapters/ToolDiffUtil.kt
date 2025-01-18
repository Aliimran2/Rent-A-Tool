package com.miassolutions.rentatool.ui.newadapters

import androidx.recyclerview.widget.DiffUtil
import com.miassolutions.rentatool.data.model.Tool

class ToolDiffCallback : DiffUtil.ItemCallback<Tool>() {
    override fun areItemsTheSame(oldItem: Tool, newItem: Tool): Boolean {
        return oldItem.toolId == newItem.toolId
    }

    override fun areContentsTheSame(oldItem: Tool, newItem: Tool): Boolean {
        return oldItem == newItem
    }
}