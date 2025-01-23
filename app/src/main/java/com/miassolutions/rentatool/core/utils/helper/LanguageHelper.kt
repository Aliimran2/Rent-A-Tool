package com.miassolutions.rentatool.core.utils.helper

import android.content.Context
import android.content.res.Configuration
import java.util.Locale

object LanguageHelper {

    fun setLocale(context: Context, language: String) {
        val locale = Locale(language)
        Locale.setDefault(locale)
        val config = Configuration(context.resources.configuration)
        config.setLocale(locale)
        context.resources.updateConfiguration(config, context.resources.displayMetrics)
    }

    fun saveLanguagePreference(context: Context, language: String) {
        val preferences = context.getSharedPreferences("AppSettings", Context.MODE_PRIVATE)
        with(preferences.edit()) {
            putString("LANGUAGE", language)
            apply()
        }
    }

    fun getSavedLanguage(context: Context): String {
        val preferences = context.getSharedPreferences("AppSettings", Context.MODE_PRIVATE)
        return preferences.getString("LANGUAGE", "en") ?: "en"
    }

    fun applySavedLanguage(context: Context) {
        val language = getSavedLanguage(context)
        setLocale(context, language)
    }
}