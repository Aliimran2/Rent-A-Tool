package com.miassolutions.rentatool.utils.extenstions

import android.view.View
import android.widget.SearchView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.miassolutions.rentatool.data.model.Customer
import com.miassolutions.rentatool.data.model.Tool
import com.miassolutions.rentatool.databinding.BottomSheetCustomersBinding
import com.miassolutions.rentatool.databinding.BottomSheetToolsBinding
import com.miassolutions.rentatool.ui.adapters.CustomerSelectionListAdapter
import com.miassolutions.rentatool.ui.adapters.ToolSelectionListAdapter
import java.text.SimpleDateFormat
import java.util.Locale

fun Fragment.showConfirmDialog(
    title: String,
    message: String,
    positiveText: String = "Yes",
    negativeText: String = "Cancel",
    onConfirm: () -> Unit = {},
    onCancel: () -> Unit = {}
) {

    MaterialAlertDialogBuilder(requireContext())
        .setTitle(title)
        .setMessage(message)
        .setPositiveButton(positiveText) { dialog, _ ->
            onConfirm()
            dialog.dismiss()
        }
        .setNegativeButton(negativeText) { dialog, _ ->
            onCancel()
            dialog.dismiss()
        }.show()
}

fun Fragment.showToast(
    message: String
){
    Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
}

fun Fragment.showDatePicker(
    title: String,
    onDateSelected: (String, Long) -> Unit
) {
    val datePicker = MaterialDatePicker.Builder.datePicker()
        .setTitleText(title)
        .build()

    datePicker.addOnPositiveButtonClickListener { selectedDate ->

        val formattedDate = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(selectedDate)
        onDateSelected(formattedDate, selectedDate)

    }
    datePicker.show(parentFragmentManager, "DatePicker")

}

//Bottom Sheet Utility
fun Fragment.showBottomSheetDialogWithAction(
    contentBinding: View,
    onBind: (BottomSheetDialog) -> Unit
) {
    val dialog = BottomSheetDialog(requireContext())
    dialog.setContentView(contentBinding)
    onBind(dialog)
    dialog.show()
}

fun Fragment.showBottomSheetDialog(
    rootView: View
): BottomSheetDialog {
    val dialog = BottomSheetDialog(requireContext())
    dialog.setContentView(rootView)
    dialog.show()
    return dialog
}

//Customer selection utility

fun Fragment.showCustomerSelectionBottomSheet(
    customers: List<Customer>,
    onSelectedCustomer: (Customer) -> Unit
) {

    val binding = BottomSheetCustomersBinding.inflate(layoutInflater)

    val dialog = showBottomSheetDialog(binding.root)

    val adapter = CustomerSelectionListAdapter { customer ->
        onSelectedCustomer(customer)

        (dialog as? BottomSheetDialog)?.dismiss()

    }

    adapter.submitList(customers)
    binding.rvBottomSheet.adapter = adapter


}


fun Fragment.showToolSelectionBottomSheet(
    tools: List<Tool>,
    onSelectedTools: (List<Pair<Long, Int>>) -> Unit
) {
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

    showBottomSheetDialogWithAction(binding.root) { dialog ->
        binding.btnConfirmation.setOnClickListener {
            onSelectedTools(tempSelectedTools.map { it.key to it.value })
            dialog.dismiss()
        }
    }
}