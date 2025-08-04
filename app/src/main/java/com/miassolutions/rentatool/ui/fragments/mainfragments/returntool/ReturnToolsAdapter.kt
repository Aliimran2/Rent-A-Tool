package com.miassolutions.rentatool.ui.fragments.mainfragments.returntool


import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.widget.doAfterTextChanged
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.miassolutions.rentatool.databinding.ItemReturnToolBinding

class ReturnToolsAdapter(

    private val onSelectionChanged: (id: Long, isChecked: Boolean, qty: Int) -> Unit
) : ListAdapter<ReturnToolItem, ReturnToolsAdapter.ReturnToolViewHolder>(DiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ReturnToolViewHolder {
        val binding =
            ItemReturnToolBinding.inflate(LayoutInflater.from(parent.context), parent, false)
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
            cbReturnSelected.isChecked = item.isSelected
            etReturnQuantity.setText(if (item.returnQuantity > 0) item.returnQuantity.toString() else "")
            cbReturnSelected.setOnCheckedChangeListener { _, isChecked ->
                val qty = etReturnQuantity.text.toString().toIntOrNull() ?: 0
                onSelectionChanged(item.rentedToolId, isChecked, qty)
            }

            etReturnQuantity.doAfterTextChanged {
                val qty = it.toString().toIntOrNull() ?: 0
                onSelectionChanged(item.rentedToolId, cbReturnSelected.isChecked, qty)
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
