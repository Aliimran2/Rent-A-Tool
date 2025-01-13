package com.miassolutions.rentatool.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.miassolutions.rentatool.data.ToolRentalRepository

class SharedViewModelFactory(private val repository: ToolRentalRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(SharedViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return SharedViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
