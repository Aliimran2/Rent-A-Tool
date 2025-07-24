package com.miassolutions.rentatool.ui.adapters.diffutil

import androidx.recyclerview.widget.DiffUtil
import com.miassolutions.rentatool.ui.fragments.toolselection.ToolSelectionItem

class ToolDiffUtil : DiffUtil.ItemCallback<ToolSelectionItem>() {
    override fun areItemsTheSame(oldItem: ToolSelectionItem, newItem: ToolSelectionItem): Boolean {
        return oldItem.toolId == newItem.toolId
    }

    override fun areContentsTheSame(
        oldItem: ToolSelectionItem,
        newItem: ToolSelectionItem
    ): Boolean {
        return oldItem == newItem
    }

}