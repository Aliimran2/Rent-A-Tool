package com.miassolutions.rentatool.ui.fragments.toolselection

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

    private val selectedQuantities = mutableMapOf<Long, Int>()

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
            etQuantitySelected.setText("1")
            etQuantitySelected.isEnabled = false
            cbTool.isChecked = false

            updateHelperText(item.availableQuantity, 1)

            cbTool.setOnCheckedChangeListener(null)
            cbTool.setOnCheckedChangeListener { _, isChecked ->
                val quantity = etQuantitySelected.text.toString().toIntOrNull() ?: 1
                etQuantitySelected.isEnabled = isChecked
                updateHelperText(item.availableQuantity, quantity)
                onToolChecked(item.tool.toolId, isChecked, quantity)
                if (isChecked) selectedQuantities[item.tool.toolId] = quantity
                else selectedQuantities.remove(item.tool.toolId)
            }

            etQuantitySelected.doAfterTextChanged {
                val quantity = it.toString().toIntOrNull() ?: 0
                if (quantity > item.availableQuantity) {
                    etQuantitySelected.error = "Max: ${item.availableQuantity}"
                } else {
                    etQuantitySelected.error = null
                    updateHelperText(item.availableQuantity, quantity)
                    onQuantityChanged(item.tool.toolId, quantity)
                    selectedQuantities[item.tool.toolId] = quantity
                }
            }
        }

        private fun updateHelperText(stock: Int, selected: Int) {
            val remaining = stock - selected
            binding.inputLayout.helperText = "In Stock: $remaining"
        }
    }

    class DiffCallback : DiffUtil.ItemCallback<ToolWithAvailability>() {
        override fun areItemsTheSame(oldItem: ToolWithAvailability, newItem: ToolWithAvailability) =
            oldItem.tool.toolId == newItem.tool.toolId

        override fun areContentsTheSame(oldItem: ToolWithAvailability, newItem: ToolWithAvailability) =
            oldItem == newItem
    }
}

