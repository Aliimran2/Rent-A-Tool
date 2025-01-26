package com.miassolutions.rentatool.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.miassolutions.rentatool.core.utils.helper.formattedDate
import com.miassolutions.rentatool.data.model.CustomerLedger
import com.miassolutions.rentatool.databinding.ItemPaymentBinding
import java.util.Date
import java.util.Locale

class LedgerAdapter : ListAdapter<CustomerLedger, LedgerAdapter.LedgerVH>(DIFF_UTIL) {
    companion object {
        val DIFF_UTIL = object : DiffUtil.ItemCallback<CustomerLedger>() {
            override fun areItemsTheSame(
                oldItem: CustomerLedger,
                newItem: CustomerLedger
            ): Boolean = oldItem.ledgerId == newItem.ledgerId

            override fun areContentsTheSame(
                oldItem: CustomerLedger,
                newItem: CustomerLedger
            ): Boolean = oldItem == newItem
        }
    }

    inner class LedgerVH(val binding: ItemPaymentBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(ledger: CustomerLedger) {
            binding.apply {
                tvDate.text = formattedDate(Date(ledger.paymentDate))

            }

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LedgerVH {
        return LedgerVH(
            ItemPaymentBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    override fun onBindViewHolder(holder: LedgerVH, position: Int) {

    }
}