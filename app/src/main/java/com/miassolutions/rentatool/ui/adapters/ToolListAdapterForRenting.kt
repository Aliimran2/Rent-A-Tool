package com.miassolutions.rentatool.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.widget.doAfterTextChanged
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.miassolutions.rentatool.data.relationship.ToolWithAvailability
import com.miassolutions.rentatool.databinding.ItemToolBinding

class ToolListAdapterForRenting(
    private val onToolChecked: (toolId: Long, isChecked: Boolean, quantity: Int) -> Unit,
    private val onQuantityChanged: (toolId: Long, quantity: Int) -> Unit
) : ListAdapter<ToolWithAvailability, ToolListAdapterForRenting.ToolViewHolder>(DiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ToolViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemToolBinding.inflate(inflater, parent, false)
        return ToolViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ToolViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class ToolViewHolder(private val binding: ItemToolBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: ToolWithAvailability) = with(binding) {
            tvToolName.text = item.tool.name
            inputLayout.helperText = "In Stock: ${item.availableQuantity}"
            etQuantitySelected.setText("1")
            etQuantitySelected.isEnabled = false
            cbTool.isChecked = false

            cbTool.setOnCheckedChangeListener(null)
            cbTool.setOnCheckedChangeListener { _, isChecked ->
                etQuantitySelected.isEnabled = isChecked
                val quantity = etQuantitySelected.text.toString().toIntOrNull() ?: 1
                onToolChecked(item.tool.toolId, isChecked, quantity)
            }

            etQuantitySelected.doAfterTextChanged {
                val quantity = it.toString().toIntOrNull() ?: 0
                if (quantity > item.availableQuantity) {
                    etQuantitySelected.error = "Max: ${item.availableQuantity}"
                    return@doAfterTextChanged
                } else {
                    etQuantitySelected.error = null
                    onQuantityChanged(item.tool.toolId, quantity)
                }
            }
        }
    }

    class DiffCallback : DiffUtil.ItemCallback<ToolWithAvailability>() {
        override fun areItemsTheSame(
            oldItem: ToolWithAvailability,
            newItem: ToolWithAvailability
        ) = oldItem.tool.toolId == newItem.tool.toolId

        override fun areContentsTheSame(
            oldItem: ToolWithAvailability,
            newItem: ToolWithAvailability
        ) = oldItem == newItem
    }
}
