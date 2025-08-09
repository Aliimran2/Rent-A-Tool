package com.miassolutions.rentatool.ui.fragments.mainfragments.confirmation.returnconfirmation

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.miassolutions.rentatool.databinding.ItemConfirmReturnBinding
import com.miassolutions.rentatool.ui.fragments.mainfragments.returntool.ReturnToolItem

class ConfirmReturnListAdapter :
    ListAdapter<ReturnToolItem, ConfirmReturnListAdapter.ReturnViewHolder>(ReturnDiffUtil()) {

    class ReturnViewHolder(private val binding: ItemConfirmReturnBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: ReturnToolItem)= with(binding) {
            tvToolName.text = item.toolName
            tvToolQuantity.text = item.returnQuantity.toString()

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ReturnViewHolder {
        val itemReturnBinding =
            ItemConfirmReturnBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ReturnViewHolder(itemReturnBinding)
    }

    override fun onBindViewHolder(holder: ReturnViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
}

class ReturnDiffUtil : DiffUtil.ItemCallback<ReturnToolItem>() {
    override fun areItemsTheSame(oldItem: ReturnToolItem, newItem: ReturnToolItem): Boolean {
        return oldItem.rentedToolId == newItem.rentedToolId
    }

    override fun areContentsTheSame(oldItem: ReturnToolItem, newItem: ReturnToolItem): Boolean {
        return oldItem == newItem
    }

}