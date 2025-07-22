package com.miassolutions.rentatool.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.miassolutions.rentatool.data.entities.ToolEntity
import com.miassolutions.rentatool.databinding.ItemStockToolsBinding
import com.miassolutions.rentatool.ui.adapters.diffutil.ToolDiffUtil

class ToolListAdapter : ListAdapter<ToolEntity, ToolListAdapter.ToolVH>(ToolDiffUtil()) {


    class ToolVH(private val binding : ItemStockToolsBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(toolEntity: ToolEntity){
            binding.apply {
                tvToolName.text = toolEntity.name
                toolCondition.text = toolEntity.toolCondition
                tvRentPerDay.text = "${toolEntity.rentPerDay.toInt()} Rs/day"
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ToolVH {
        return ToolVH(ItemStockToolsBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: ToolVH, position: Int) = holder.bind(getItem(position))

}