package com.miassolutions.rentatool.ui.fragments.mainfragments.toolselection

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

    private val selectedMap = mutableMapOf<Long, Int>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ToolViewHolder {
        val binding = ItemToolBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ToolViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ToolViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class ToolViewHolder(private val binding: ItemToolBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: ToolWithAvailability) = with(binding) {
            val toolId = item.tool.toolId
            val isSelected = selectedMap.containsKey(toolId)
            val quantity = selectedMap[toolId] ?: 1

            tvToolName.text = item.tool.name
            etQuantitySelected.setText(quantity.toString())
            etQuantitySelected.isEnabled = isSelected
            cbTool.isChecked = isSelected
            updateHelperText(item.availableQuantity, quantity)

            cbTool.setOnCheckedChangeListener(null)
            cbTool.setOnCheckedChangeListener { _, isChecked ->
                etQuantitySelected.isEnabled = isChecked
                val qty = etQuantitySelected.text.toString().toIntOrNull() ?: 1
                if (isChecked) {
                    selectedMap[toolId] = qty
                    onToolChecked(toolId, true, qty)
                } else {
                    selectedMap.remove(toolId)
                    onToolChecked(toolId, false, qty)
                }
                updateHelperText(item.availableQuantity, qty)
            }

            etQuantitySelected.doAfterTextChanged {
                val qty = it.toString().toIntOrNull() ?: 0
                if (qty <= 0) {
                    inputLayout.error = "Min: 1"
                    return@doAfterTextChanged
                }
                if (qty > item.availableQuantity) {
                    inputLayout.error = "Max: ${item.availableQuantity}"
                } else {
                    etQuantitySelected.error = null
                    updateHelperText(item.availableQuantity, qty)
                    selectedMap[toolId] = qty
                    onQuantityChanged(toolId, qty)
                }
            }
        }

        private fun updateHelperText(stock: Int, selected: Int) {
            val remaining = stock - selected
            if (remaining <= 0) {

                binding.inputLayout.error = "Out of stock"
            } else {

                binding.inputLayout.helperText = "In Stock: $remaining"
            }
        }
    }

    class DiffCallback : DiffUtil.ItemCallback<ToolWithAvailability>() {
        override fun areItemsTheSame(
            oldItem: ToolWithAvailability,
            newItem: ToolWithAvailability
        ): Boolean {
            return oldItem.tool.toolId == newItem.tool.toolId
        }

        override fun areContentsTheSame(
            oldItem: ToolWithAvailability,
            newItem: ToolWithAvailability
        ): Boolean {
            return oldItem == newItem
        }
    }
}
