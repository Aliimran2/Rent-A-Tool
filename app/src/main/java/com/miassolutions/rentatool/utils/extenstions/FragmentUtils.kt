package com.miassolutions.rentatool.utils.extenstions

import android.view.LayoutInflater
import android.view.View
import android.widget.SearchView
import androidx.fragment.app.Fragment
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.datepicker.MaterialDatePicker
import com.miassolutions.rentatool.data.model.Customer
import com.miassolutions.rentatool.data.model.Tool
import com.miassolutions.rentatool.databinding.BottomSheetCustomersBinding
import com.miassolutions.rentatool.databinding.BottomSheetToolsBinding
import com.miassolutions.rentatool.ui.adapters.CustomerSelectionListAdapter
import com.miassolutions.rentatool.ui.adapters.ToolSelectionListAdapter
import kotlinx.coroutines.currentCoroutineContext
import java.text.SimpleDateFormat
import java.util.Locale

fun Fragment.showDatePicker(
    title: String,
    onDateSelected: (String, Long) -> Unit
) {
    val datePicker = MaterialDatePicker.Builder.datePicker()
        .setTitleText(title)
        .build()

    datePicker.addOnPositiveButtonClickListener { selectedDate ->

        val formattedDate = SimpleDateFormat("dd/mm/yyyy", Locale.getDefault()).format(selectedDate)
        onDateSelected(formattedDate, selectedDate)

    }
    datePicker.show(parentFragmentManager, "DatePicker")

}

//Bottom Sheet Utility
fun Fragment.showBottomSheetDialog(
    layoutInflater: LayoutInflater,
    contentBinding: View,
    onBind: (BottomSheetDialog) -> Unit
){
    val dialog = BottomSheetDialog(requireContext())
    dialog.setContentView(contentBinding)
    onBind(dialog)
    dialog.show()
}

//Customer selection utility

fun Fragment.showCustomerSelectionBottomSheet (
    customers: List<Customer>,
    onSelectedCustomer : (Customer) -> Unit
){

    val binding = BottomSheetCustomersBinding.inflate(layoutInflater)

    val adapter = CustomerSelectionListAdapter {customer ->
        onSelectedCustomer(customer)
    }

    adapter.submitList(customers)
    binding.rvBottomSheet.adapter = adapter

    showBottomSheetDialog(layoutInflater, binding.root){ dialog ->
        dialog.dismiss() //to review

    }

}


fun Fragment.showToolSelectionBottomSheet(
    tools : List<Tool>,
    onSelectedTools: (List<Pair<Long, Int>>) -> Unit
){
    val binding = BottomSheetToolsBinding.inflate(layoutInflater)
    val tempSelectedTools = mutableMapOf<Long, Int>()
    val adapter = ToolSelectionListAdapter(tools) { selected ->
        tempSelectedTools.clear()
        tempSelectedTools.putAll(selected)
    }

    binding.rvBottomSheet.adapter = adapter

    binding.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
        override fun onQueryTextSubmit(query: String?): Boolean = false
        override fun onQueryTextChange(newText: String?): Boolean {
            newText?.let { adapter.filter(it) }
            return true
        }
    })

    showBottomSheetDialog(layoutInflater, binding.root) { dialog ->
        binding.btnConfirmation.setOnClickListener {
            onSelectedTools(tempSelectedTools.map { it.key to it.value })
            dialog.dismiss()
        }
    }
}