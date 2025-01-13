package com.miassolutions.rentatool.core.utils.helper

import android.content.Context
import android.content.pm.PackageManager
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.google.android.material.textfield.TextInputEditText
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale


fun clearInputs(vararg inputFields : TextInputEditText){
    inputFields.forEach { field ->
        field.text?.clear()
    }
}


fun showToast(context: Context, message : String){
    Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
}

fun formattedDate(date: Date) : String {
    return SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(date)
}

// Helper function to check if a permission is granted
fun Fragment.isPermissionGranted(permission: String): Boolean {
    return ContextCompat.checkSelfPermission(requireContext(), permission) == PackageManager.PERMISSION_GRANTED
}

// Helper function to request a permission
fun Fragment.requestPermission(permission: String, requestCode: Int) {
    if (!isPermissionGranted(permission)) {
        requestPermissions(arrayOf(permission), requestCode)
    }
}

// Function to handle the result of permission requests
fun Fragment.handlePermissionResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray, onPermissionGranted: () -> Unit) {
    if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
        onPermissionGranted()
    } else {
        // Show a message to the user explaining that the permission is required
        showToast(requireContext(), "Permission denied")
    }
}
