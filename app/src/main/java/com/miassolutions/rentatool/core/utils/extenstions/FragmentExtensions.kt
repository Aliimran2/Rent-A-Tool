package com.miassolutions.rentatool.core.utils.extenstions

import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.dialog.MaterialAlertDialogBuilder
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
) {
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


fun Fragment.showBottomSheetDialog(
    rootView: View
): BottomSheetDialog {
    val bottomSheetDialog = BottomSheetDialog(requireContext())
    bottomSheetDialog.setContentView(rootView)
    bottomSheetDialog.show()
    return bottomSheetDialog
}




