package com.miassolutions.rentatool.utils.extenstions

import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId
import java.time.format.DateTimeFormatter


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
    onDateSelected: (LocalDate) -> Unit
) {


    val datePicker = MaterialDatePicker.Builder.datePicker()
        .setTitleText(title)
        .build()

    datePicker.addOnPositiveButtonClickListener { selectedLong ->

        val instant = Instant.ofEpochMilli(selectedLong)

        val selectedDate = instant.atZone(ZoneId.systemDefault()).toLocalDate()

        onDateSelected(selectedDate)


    }
    datePicker.show(parentFragmentManager, "DatePicker")

}


fun formattedDate(
    date: LocalDate,
    format: DateTimeFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy")
): String = date.format(format)

fun LocalDate.toFormattedDate(pattern: String = "dd-MM-yyyy"): String {
    val mFormat = DateTimeFormatter.ofPattern(pattern)
    return this.format(mFormat)
}




