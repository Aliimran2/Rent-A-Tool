package com.miassolutions.rentatool.ui.adapters.diffutil

import androidx.recyclerview.widget.DiffUtil
import com.miassolutions.rentatool.ui.fragments.toolselection.ToolItemUiModel

class ToolDiffUtil : DiffUtil.ItemCallback<ToolItemUiModel>() {
    override fun areItemsTheSame(oldItem: ToolItemUiModel, newItem: ToolItemUiModel): Boolean {
        return oldItem.toolId == newItem.toolId
    }

    override fun areContentsTheSame(
        oldItem: ToolItemUiModel,
        newItem: ToolItemUiModel
    ): Boolean {
        return oldItem == newItem
    }

}