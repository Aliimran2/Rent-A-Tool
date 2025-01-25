package com.miassolutions.rentatool.core.utils.helper

import android.content.Context
import android.graphics.Typeface
import androidx.core.content.res.ResourcesCompat
import com.miassolutions.rentatool.R

object FontHelper {

    fun getFont(context: Context, fontName: String): Typeface? {
        return when (fontName) {
            "roboto" -> ResourcesCompat.getFont(context, R.font.roboto)
            "jameel_nastaliq" -> ResourcesCompat.getFont(context, R.font.jameel_nastaliq)
            "calibri" -> ResourcesCompat.getFont(context, R.font.calibri)
            "dmsansregular" -> ResourcesCompat.getFont(context, R.font.dmsansregular)
            else -> ResourcesCompat.getFont(context, R.font.roboto) // Default font
        }
    }

    fun applyFontToViews(context: Context, fontName: String, rootView: android.view.View) {
        val typeface = getFont(context, fontName)
        if (typeface != null) {
            setFontRecursively(rootView, typeface)
        }
    }

    private fun setFontRecursively(view: android.view.View, typeface: Typeface) {
        when (view) {
            is android.widget.TextView -> view.typeface = typeface
            is android.view.ViewGroup -> {
                for (i in 0 until view.childCount) {
                    setFontRecursively(view.getChildAt(i), typeface)
                }
            }
        }
    }
}