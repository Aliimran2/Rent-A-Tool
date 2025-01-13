package com.miassolutions.rentatool.core.utils.helper

import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.PackageManagerCompat

object PermissionUtils {
    const val REQUEST_CODE = 100

    fun hasPermissions(context:Context, permissions: Array<String>) : Boolean {
        return permissions.all {
            ContextCompat.checkSelfPermission(context, it) == PackageManager.PERMISSION_GRANTED
        }
    }

    fun requestPermissions(activity: Activity, permissions: Array<String>){
        ActivityCompat.requestPermissions(activity, permissions, REQUEST_CODE)
    }
}