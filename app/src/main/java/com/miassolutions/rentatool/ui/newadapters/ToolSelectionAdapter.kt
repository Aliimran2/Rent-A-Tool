package com.miassolutions.rentatool.ui.newadapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.widget.doOnTextChanged
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.miassolutions.rentatool.data.model.Tool
import com.miassolutions.rentatool.databinding.ItemDropDownToolBinding

class ToolSelectionAdapter(
    private val onSelectionChanged: (List<Tool>) -> Unit
) : ListAdapter<Tool, ToolSelectionAdapter.ToolViewHolder>(ToolDiffCallback()) {

     val selectedTools = mutableListOf<Tool>()


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ToolViewHolder {
        val binding =
            ItemDropDownToolBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ToolViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ToolViewHolder, position: Int) {
        val tool = getItem(position)
        holder.bind(tool)
    }

    inner class ToolViewHolder(private val binding: ItemDropDownToolBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(tool: Tool) {

            binding.apply {
                tvToolName.text = tool.name
                inputLayout.helperText = "In Stock : ${tool.availableStock}"

                val isSelected = selectedTools.any { it.toolId == tool.toolId }
                cbTool.isChecked = isSelected
                etQuantitySelected.isEnabled = isSelected

                etQuantitySelected.doOnTextChanged { text, _, _, _ ->
                    val enteredValue = text?.toString()?.toIntOrNull() ?: 0
                    if (enteredValue > tool.availableStock) {
                        inputLayout.error = "Only ${tool.availableStock} available"
                        inputLayout.helperText = null
                    } else {
                        inputLayout.error = null
                        val remainingStock = tool.availableStock - enteredValue
                        inputLayout.helperText = "$remainingStock remaining"
                    }

                    if (cbTool.isChecked) {
                        val selectedTool = selectedTools.find { it.toolId == tool.toolId }
                        if (selectedTool != null) {
                            selectedTool.rentedQuantity = enteredValue
                        } else {
                            selectedTools.add(tool.copy(rentedQuantity = enteredValue))
                        }
                        onSelectionChanged(selectedTools)
                    }
                }

                cbTool.setOnCheckedChangeListener { _, isChecked ->
                    if (isChecked) {
                        val quantity = etQuantitySelected.text?.toString()?.toIntOrNull() ?: 1
                        selectedTools.add(tool.copy(rentedQuantity = quantity))
                        etQuantitySelected.isEnabled = true
                    } else {
                        selectedTools.removeAll { it.toolId == tool.toolId }
                        etQuantitySelected.isEnabled = false
                        etQuantitySelected.text?.clear()
                    }
                    onSelectionChanged(selectedTools)
                }

            }

        }
    }


}
