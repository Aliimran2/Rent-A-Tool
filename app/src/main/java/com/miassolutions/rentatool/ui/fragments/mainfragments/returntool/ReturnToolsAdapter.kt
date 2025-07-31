package com.miassolutions.rentatool.ui.fragments.mainfragments.returntool


import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.widget.doAfterTextChanged
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.miassolutions.rentatool.databinding.ItemReturnToolBinding

class ReturnToolsAdapter(
    private val onReturnQuantityChanged: (toolId: Long, quantity: Int) -> Unit,
    private val onCheckboxChanged: (toolId: Long, isChecked: Boolean) -> Unit
) : ListAdapter<ReturnToolItem, ReturnToolsAdapter.ReturnToolViewHolder>(DiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ReturnToolViewHolder {
        val binding = ItemReturnToolBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ReturnToolViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ReturnToolViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class ReturnToolViewHolder(
        private val binding: ItemReturnToolBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(item: ReturnToolItem) = with(binding) {
            tvToolName.text = item.toolName
            tvRentedQuantity.text = "Rented: ${item.rentedQuantity}"
            tvRemainingQuantity.text = "Remaining: ${item.remainingQuantity}"
            etReturnQuantity.setText(item.returnQuantity.takeIf { it > 0 }?.toString() ?: "")
            cbReturnSelected.isChecked = item.isSelected

            // Return Quantity Input
            etReturnQuantity.doAfterTextChanged { text ->
                val newQty = text?.toString()?.toIntOrNull() ?: 0
                onReturnQuantityChanged(item.toolId, newQty)
            }

            // Checkbox
            cbReturnSelected.setOnCheckedChangeListener { _, isChecked ->
                onCheckboxChanged(item.toolId, isChecked)
            }
        }
    }

    class DiffCallback : DiffUtil.ItemCallback<ReturnToolItem>() {
        override fun areItemsTheSame(oldItem: ReturnToolItem, newItem: ReturnToolItem): Boolean {
            return oldItem.toolId == newItem.toolId
        }

        override fun areContentsTheSame(oldItem: ReturnToolItem, newItem: ReturnToolItem): Boolean {
            return oldItem == newItem
        }
    }
}
