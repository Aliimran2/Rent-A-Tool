package com.miassolutions.rentatool.ui.newadapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.miassolutions.rentatool.core.utils.helper.formattedDateAndTime
import com.miassolutions.rentatool.data.model.Tool
import com.miassolutions.rentatool.databinding.ItemRentedToolsBinding
import java.util.Date

class RentedToolsAdapter : ListAdapter<Tool, RentedToolsAdapter.RentedVH>(DIFF_UTIL) {

    companion object {
        val DIFF_UTIL = object : DiffUtil.ItemCallback<Tool>() {
            override fun areItemsTheSame(oldItem: Tool, newItem: Tool): Boolean =
                oldItem.toolId == newItem.toolId

            override fun areContentsTheSame(oldItem: Tool, newItem: Tool): Boolean =
                oldItem == newItem
        }
    }

    class RentedVH(val binding: ItemRentedToolsBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(tool: Tool) {
            binding.apply {
                tvRentedDate.text = formattedDateAndTime(Date())
                tvToolName.text = tool.name
                tvToolQuantity.text = "${tool.rentedQuantity}"
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RentedVH {
        return RentedVH(
            ItemRentedToolsBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: RentedVH, position: Int) {
        holder.bind(getItem(position))
    }
}