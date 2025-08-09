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

    private val selectedQuantities = mutableMapOf<Long, Int>()

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
            val availableQty = item.availableQuantity
            val selectedQty = selectedQuantities[toolId]

            val isChecked = availableQty > 0 && selectedQty in 1..availableQty

            tvToolName.text = item.tool.name

            cbTool.setOnCheckedChangeListener(null)
            cbTool.isEnabled = availableQty > 0
            cbTool.isChecked = isChecked

            etQuantitySelected.apply {
                isEnabled = isChecked
                setText(
                    if (selectedQty != null && selectedQty > 0) selectedQty.toString() else ""
                )
            }



            inputLayout.error = if(availableQty == 0) "Out of stock" else null
            inputLayout.helperText = if(availableQty > 0) "In Stock: $availableQty" else null

            cbTool.setOnCheckedChangeListener { _, checked ->
                etQuantitySelected.isEnabled = checked && availableQty > 0

                val qty = etQuantitySelected.text.toString().toIntOrNull()

                if (checked && qty != null && qty in 1..availableQty){
                    selectedQuantities[toolId] = qty
                    onToolChecked(toolId, true, qty)
                    updateHelperText(availableQty, qty)
                } else {
                    selectedQuantities.remove(toolId)
                    onToolChecked(toolId, false, 0)
                    etQuantitySelected.setText("")
                    inputLayout.error = null
                    inputLayout.helperText = "In Stock: $availableQty"
                }
            }

            etQuantitySelected.doAfterTextChanged { text ->
                if(!cbTool.isChecked) return@doAfterTextChanged

                val qty = text.toString().toIntOrNull()

                when {
                    qty == null -> {
                        inputLayout.error = "Enter a valid number"
                        inputLayout.helperText = null
                        selectedQuantities.remove(toolId)
                        onToolChecked(toolId, false, 0)
                    }

                    qty < 1 -> {
                        inputLayout.error = "Min: 1"
                        inputLayout.helperText = null
                        selectedQuantities.remove(toolId)
                        onToolChecked(toolId,false, 0)
                    }

                    qty > availableQty -> {
                        inputLayout.error = "Max: $availableQty"
                        inputLayout.helperText = null
                        selectedQuantities.remove(toolId)
                        onToolChecked(toolId, false, 0)
                    }

                    else -> {
                        inputLayout.error = null
                        selectedQuantities[toolId] = qty
                        onToolChecked(toolId, true, qty)
                        onQuantityChanged(toolId, qty)
                        updateHelperText(availableQty, qty)
                    }
                }
            }


        }
        private fun updateHelperText(available: Int, selected: Int) {
            val remaining = available - selected
            binding.inputLayout.helperText = if (remaining == 0) "Fully selected" else "In Stock: $remaining"
        }


    }



    class DiffCallback : DiffUtil.ItemCallback<ToolWithAvailability>() {
        override fun areItemsTheSame(oldItem: ToolWithAvailability, newItem: ToolWithAvailability) =
            oldItem.tool.toolId == newItem.tool.toolId

        override fun areContentsTheSame(
            oldItem: ToolWithAvailability,
            newItem: ToolWithAvailability
        ) =
            oldItem == newItem
    }
}
