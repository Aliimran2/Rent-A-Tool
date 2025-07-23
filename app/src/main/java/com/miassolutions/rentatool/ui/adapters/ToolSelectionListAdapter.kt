package com.miassolutions.rentatool.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.widget.doAfterTextChanged
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.miassolutions.rentatool.data.entities.ToolEntity
import com.miassolutions.rentatool.databinding.ItemToolBinding
import com.miassolutions.rentatool.ui.adapters.diffutil.ToolDiffUtil

class ToolSelectionListAdapter(
    private val listener: ToolSelectionListener

) : ListAdapter<ToolEntity, ToolSelectionListAdapter.ToolVH>(ToolDiffUtil()) {


    interface ToolSelectionListener {
        fun onToolSelectionChanged(tool: ToolEntity, quantity: Int, isChecked: Boolean)
    }

    inner class ToolVH(private val binding: ItemToolBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(tool: ToolEntity) {
            with(binding) {
                tvToolName.text = tool.name
                cbTool.setOnCheckedChangeListener(null)
                cbTool.isChecked = false
                etQuantitySelected.setText("")

                inputLayout.helperText = "In stock: ${tool.totalQuantity}"

                cbTool.setOnCheckedChangeListener { _, isChecked ->
                    etQuantitySelected.isEnabled = isChecked
                    if (!isChecked) {
                        etQuantitySelected.setText("")
                    }
                    listener.onToolSelectionChanged(
                        tool,
                        etQuantitySelected.text.toString().toIntOrNull() ?: 0,
                        isChecked
                    )
                }
                etQuantitySelected.doAfterTextChanged {
                    val qty = it.toString().toIntOrNull() ?: 0
                    val valid = qty in 1..tool.totalQuantity

                    etQuantitySelected.error = if (!valid && cbTool.isChecked) {
                        "Invalid quantity"
                    } else null

                    if (cbTool.isChecked && valid) {
                        listener.onToolSelectionChanged(tool, qty, true)
                    }
                }

                etQuantitySelected.isEnabled = false
            }

        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ToolVH =
        ToolVH(ItemToolBinding.inflate(LayoutInflater.from(parent.context), parent, false))

    override fun onBindViewHolder(holder: ToolVH, position: Int) {
        holder.bind(getItem(position))
    }
}