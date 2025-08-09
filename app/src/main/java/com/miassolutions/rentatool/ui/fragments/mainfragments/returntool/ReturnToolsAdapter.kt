package com.miassolutions.rentatool.ui.fragments.mainfragments.returntool

import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.miassolutions.rentatool.databinding.ItemReturnToolBinding

class ReturnToolsAdapter(
    private val onSelectionChanged: (id: Long, isChecked: Boolean, qty: Int) -> Unit
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

        private var currentWatcher: TextWatcher? = null

        fun bind(item: ReturnToolItem) = with(binding) {
            tvToolName.text = item.toolName
            tvRentedQuantity.text = "Rented: ${item.rentedQuantity}"

            // Remove previous listeners
            cbReturnSelected.setOnCheckedChangeListener(null)
            etReturnQuantity.removeTextChangedListener(currentWatcher)

            // Set checkbox state and enable/disable EditText
            cbReturnSelected.isChecked = item.isSelected
            etReturnQuantity.isEnabled = item.isSelected

            // Set quantity text based on enteredQuantity or default
            if (item.isSelected) {
                val qtyToShow = item.enteredQuantity?.takeIf {
                    it in 1..item.remainingQuantity
                } ?: item.remainingQuantity

                etReturnQuantity.setText(qtyToShow.toString())
                etReturnQuantity.setSelection(qtyToShow.toString().length)
                tvRemainingQuantity.text = "Remaining: ${item.rentedQuantity - qtyToShow}"
            } else {
                etReturnQuantity.setText("")
                tvRemainingQuantity.text = "Remaining: ${item.rentedQuantity}"
            }

            // Checkbox listener
            cbReturnSelected.setOnCheckedChangeListener { _, isChecked ->
                etReturnQuantity.removeTextChangedListener(currentWatcher)

                if (isChecked) {
                    etReturnQuantity.isEnabled = true

                    val currentQty = etReturnQuantity.text.toString().toIntOrNull()
                    val qtyToUse = currentQty?.coerceIn(1, item.remainingQuantity) ?: item.remainingQuantity

                    etReturnQuantity.setText(qtyToUse.toString())
                    etReturnQuantity.setSelection(qtyToUse.toString().length)
                    tvRemainingQuantity.text = "Remaining: ${item.rentedQuantity - qtyToUse}"

                    onSelectionChanged(item.rentedToolId, true, qtyToUse)
                } else {
                    etReturnQuantity.setText("")
                    etReturnQuantity.isEnabled = false
                    tvRemainingQuantity.text = "Remaining: ${item.rentedQuantity}"

                    onSelectionChanged(item.rentedToolId, false, 0)
                }

                etReturnQuantity.addTextChangedListener(currentWatcher)
            }

            // Define new TextWatcher
            currentWatcher = object : TextWatcher {
                override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) = Unit
                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) = Unit

                override fun afterTextChanged(s: Editable?) {
                    if (!cbReturnSelected.isChecked) return

                    val inputQty = s?.toString()?.toIntOrNull()

                    if (inputQty == null || inputQty < 1 || inputQty > item.remainingQuantity) {
                        etReturnQuantity.error = "Enter between 1 and ${item.remainingQuantity}"
                        tvRemainingQuantity.text = "Remaining: ${item.rentedQuantity}"
                        onSelectionChanged(item.rentedToolId, true, 0)
                    } else {
                        etReturnQuantity.error = null
                        tvRemainingQuantity.text = "Remaining: ${item.rentedQuantity - inputQty}"
                        onSelectionChanged(item.rentedToolId, true, inputQty)
                    }
                }
            }

            etReturnQuantity.addTextChangedListener(currentWatcher)
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
