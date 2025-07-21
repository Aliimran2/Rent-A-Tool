package com.miassolutions.rentatool.ui.adapters.diffutil

import androidx.recyclerview.widget.DiffUtil
import com.miassolutions.rentatool.data.model.ToolEntity

class ToolDiffUtil : DiffUtil.ItemCallback<ToolEntity>() {
    override fun areItemsTheSame(oldItem: ToolEntity, newItem: ToolEntity): Boolean = oldItem.toolId == newItem.toolId
    override fun areContentsTheSame(oldItem: ToolEntity, newItem: ToolEntity): Boolean = oldItem == newItem
}