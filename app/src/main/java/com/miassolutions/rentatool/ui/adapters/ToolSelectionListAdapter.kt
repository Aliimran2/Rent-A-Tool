package com.miassolutions.rentatool.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.widget.doOnTextChanged
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.miassolutions.rentatool.data.model.Tool
import com.miassolutions.rentatool.databinding.ItemDropDownToolBinding

class ToolSelectionListAdapter(
//    private val toolsList :List<Tool>,
//    private val onSelectionChanged: (Map<Tool, Int>) -> Unit
) : ListAdapter<Tool, ToolSelectionListAdapter.ToolVH>(ToolSelectionDiffUtil()) {

    private val selectedTools = mutableMapOf<Tool, Int>()
//    private var filteredTools = toolsList
//
//    fun filter(query : String){
//        filteredTools = if (query.isEmpty()){
//            toolsList
//        } else {
//            toolsList.filter { it.name.contains(query, ignoreCase = true) }
//        }
//    }


    inner class ToolVH(private val binding: ItemDropDownToolBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(tool: Tool) {
            binding.apply {
                tvToolName.text = tool.name
                inputLayout.helperText = "In Stock : ${tool.availableStock}"
                //check box handling
                cbTool.isChecked = selectedTools.containsKey(tool)
                etQuantitySelected.isEnabled = false

                etQuantitySelected.doOnTextChanged { text, _, _, _ ->
                    val enteredValue = text?.toString()?.toIntOrNull() ?: 0
                    if (enteredValue > tool.availableStock) {
                        inputLayout.error = "Only ${tool.availableStock} available"
                        inputLayout.helperText = null
                    } else {
                        inputLayout.error = null
                        val remainingStock = tool.availableStock - enteredValue
                        inputLayout.helperText = "${remainingStock} remaining"
                    }
                    if (cbTool.isChecked) {
                        selectedTools[tool] = enteredValue
                    }
                }

                cbTool.setOnCheckedChangeListener { _, isChecked ->
                    if (isChecked) {
                        val quantity = etQuantitySelected.text.toString().toIntOrNull() ?: 1
                        selectedTools[tool] = quantity
                        etQuantitySelected.isEnabled =true
                    } else {
                        etQuantitySelected.isEnabled = false
                        selectedTools.remove(tool)
                        etQuantitySelected.text?.clear()
                    }
//                    onSelectionChanged(selectedTools)
                }

            }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ToolVH =
        ToolVH(ItemDropDownToolBinding.inflate(LayoutInflater.from(parent.context), parent, false))

    override fun onBindViewHolder(holder: ToolVH, position: Int) = holder.bind(getItem(position))
}