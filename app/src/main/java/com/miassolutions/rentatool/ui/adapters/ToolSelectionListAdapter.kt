package com.miassolutions.rentatool.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.widget.doOnTextChanged
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.miassolutions.rentatool.data.model.ToolEntity
import com.miassolutions.rentatool.databinding.ItemToolBinding
import com.miassolutions.rentatool.ui.adapters.diffutil.ToolDiffUtil

class ToolSelectionListAdapter(

    private val onSelectionChanged: (Map<Long, Int>) -> Unit
) : ListAdapter<ToolEntity, ToolSelectionListAdapter.ToolVH>(ToolDiffUtil()) {

    private val selectedTools = mutableMapOf<Long, Int>()

    inner class ToolVH(private val binding: ItemToolBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(toolEntity: ToolEntity) {
            binding.apply {
                tvToolName.text = toolEntity.name
                inputLayout.helperText = "Stock:${toolEntity.totalQuantity}"
                //check box handling
                cbTool.isChecked = selectedTools.containsKey(toolEntity.toolId)
                etQuantitySelected.isEnabled = false

                etQuantitySelected.doOnTextChanged { text, _, _, _ ->
                    val enteredValue = text?.toString()?.toIntOrNull() ?: 0
                    if (enteredValue > toolEntity.totalQuantity) {
                        inputLayout.error = "${toolEntity.totalQuantity} available"
                        inputLayout.helperText = null
                    } else {
                        inputLayout.error = null
                        val remainingStock = toolEntity.totalQuantity - enteredValue
                        inputLayout.helperText = "${remainingStock} remaining"
                    }
                    if (cbTool.isChecked) {
                        selectedTools[toolEntity.toolId] = enteredValue
                        onSelectionChanged(selectedTools)
                    }
                }

                cbTool.setOnCheckedChangeListener { _, isChecked ->
                    if (isChecked) {
                        val quantity = etQuantitySelected.text?.toString()?.toIntOrNull() ?: 1
                        selectedTools[toolEntity.toolId] = quantity
                        etQuantitySelected.isEnabled = true
                    } else {
                        etQuantitySelected.isEnabled = false
                        selectedTools.remove(toolEntity.toolId)
                        etQuantitySelected.text?.clear()
                    }
                    onSelectionChanged(selectedTools)
                }

            }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ToolVH =
        ToolVH(ItemToolBinding.inflate(LayoutInflater.from(parent.context), parent, false))

    override fun onBindViewHolder(holder: ToolVH, position: Int) {
        holder.bind(getItem(position))
    }
}