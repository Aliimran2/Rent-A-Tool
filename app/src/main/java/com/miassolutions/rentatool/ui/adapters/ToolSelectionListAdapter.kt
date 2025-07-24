package com.miassolutions.rentatool.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.widget.doAfterTextChanged
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.miassolutions.rentatool.data.entities.ToolEntity
import com.miassolutions.rentatool.databinding.ItemToolBinding
import com.miassolutions.rentatool.ui.adapters.diffutil.ToolDiffUtil
import com.miassolutions.rentatool.ui.fragments.toolselection.ToolSelectionItem

class ToolSelectionListAdapter(
    private val onCheckedChanged: (Long, Boolean) -> Unit,
    private val onQuantityChanged: (Long, String) -> Unit

) : ListAdapter<ToolSelectionItem, ToolSelectionListAdapter.ToolVH>(ToolDiffUtil()) {


    interface ToolSelectionListener {
        fun onToolSelectionChanged(tool: ToolEntity, quantity: Int, isChecked: Boolean)
    }

    inner class ToolVH(private val binding: ItemToolBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: ToolSelectionItem) = with(binding) {
            checkBoxToolName.text = item.toolName
            checkBoxToolName.isChecked = item.isSelected
            tvAvailable.text = "Available: ${item.availableQuantity}"
            inputQuantity.setText(item.selectedQuantity)

            layoutQuantity.error = item.inputError

            checkBoxToolName.setOnCheckedChangeListener(null)
            inputQuantity.doAfterTextChanged { val nothing = null }

            checkBoxToolName.isChecked = item.isSelected
            inputQuantity.setText(item.selectedQuantity)

            checkBoxToolName.setOnCheckedChangeListener { _, isChecked ->
                onCheckedChanged(item.toolId, isChecked)
            }

            inputQuantity.doAfterTextChanged { text ->
                onQuantityChanged(item.toolId, text.toString())
            }

            layoutQuantity.isEnabled = item.isSelected

        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ToolVH =
        ToolVH(ItemToolBinding.inflate(LayoutInflater.from(parent.context), parent, false))

    override fun onBindViewHolder(holder: ToolVH, position: Int) {
        holder.bind(getItem(position))
    }
}