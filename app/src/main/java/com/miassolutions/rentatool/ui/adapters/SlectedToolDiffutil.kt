package com.miassolutions.rentatool.ui.adapters

import androidx.recyclerview.widget.DiffUtil

class SelectedToolDiffUtil : DiffUtil.ItemCallback<Pair<Long, Int>>() {
    override fun areItemsTheSame(oldItem: Pair<Long, Int>, newItem: Pair<Long, Int>): Boolean = oldItem.first == newItem.first

    override fun areContentsTheSame(oldItem: Pair<Long, Int>, newItem: Pair<Long, Int>): Boolean = oldItem == newItem

}
