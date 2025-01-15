package com.miassolutions.rentatool.ui.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.miassolutions.rentatool.R
import com.miassolutions.rentatool.core.utils.helper.formattedDate
import com.miassolutions.rentatool.data.model.RentalDetail
import java.util.Date

class RentalDetailAdapter :
    ListAdapter<RentalDetail, RentalDetailAdapter.RentalDetailViewHolder>(DiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RentalDetailViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_rental_details, parent, false)
        return RentalDetailViewHolder(view)
    }

    override fun onBindViewHolder(holder: RentalDetailViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class RentalDetailViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val toolNameTextView: TextView = itemView.findViewById(R.id.toolNameTextView)
        private val quantityTextView: TextView = itemView.findViewById(R.id.quantityTextView)
        private val rentPerDayTextView: TextView = itemView.findViewById(R.id.rentPerDayTextView)
        private val rentalDateTextView: TextView = itemView.findViewById(R.id.rentalDateTextView)

        fun bind(detail: RentalDetail) {
            toolNameTextView.text = "Tool ID: ${detail.toolId}" // Replace with actual tool name if available
            quantityTextView.text = "Quantity: ${detail.quantity}"
            rentPerDayTextView.text = "Rent/Day: ${detail.rentPerDay}"

            rentalDateTextView.text = "Rental Date: ${formattedDate(Date(detail.rentalDate))}"
        }
    }

    class DiffCallback : DiffUtil.ItemCallback<RentalDetail>() {
        override fun areItemsTheSame(oldItem: RentalDetail, newItem: RentalDetail): Boolean {
            return oldItem.rentalDetailId == newItem.rentalDetailId
        }

        override fun areContentsTheSame(oldItem: RentalDetail, newItem: RentalDetail): Boolean {
            return oldItem == newItem
        }
    }
}
