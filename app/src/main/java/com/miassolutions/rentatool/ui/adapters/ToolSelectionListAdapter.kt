package com.miassolutions.rentatool.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.widget.doAfterTextChanged
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.miassolutions.rentatool.databinding.ItemToolBinding
import com.miassolutions.rentatool.ui.adapters.diffutil.ToolDiffUtil
import com.miassolutions.rentatool.ui.fragments.toolselection.ToolItemUiModel

class ToolSelectionListAdapter(
    private val onChecked: (Long, Boolean) -> Unit,
    private val onQuantityChanged: (Long, String) -> Unit

) : ListAdapter<ToolItemUiModel, ToolSelectionListAdapter.ToolVH>(ToolDiffUtil()) {


    inner class ToolVH(private val binding: ItemToolBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: ToolItemUiModel) = with(binding) {
            tvToolName.text = item.name
            cbTool.isChecked = item.isSelected
            etQuantitySelected.setText(item.selectedQuantity)

            inputLayout.helperText = "In Stock: ${item.availableQuantity}"

            etQuantitySelected.doAfterTextChanged {
                val qty = it.toString()
                if ((qty.toIntOrNull() ?: 0) > item.availableQuantity) {
                    inputLayout.error = "Exceeds stock!"
                } else {
                    inputLayout.error = null
                }
                onQuantityChanged(item.toolId, qty)
            }

            cbTool.setOnCheckedChangeListener(null)
            cbTool.setOnCheckedChangeListener { _, isChecked ->
                onChecked(item.toolId, isChecked)
            }

        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ToolVH =
        ToolVH(ItemToolBinding.inflate(LayoutInflater.from(parent.context), parent, false))

    override fun onBindViewHolder(holder: ToolVH, position: Int) {
        holder.bind(getItem(position))
    }
}