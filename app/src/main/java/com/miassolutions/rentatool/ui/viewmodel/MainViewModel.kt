package com.miassolutions.rentatool.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.miassolutions.rentatool.data.db.AppDatabase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(private val appDatabase: AppDatabase) : ViewModel() {

    fun resetAllDatabase() {
        viewModelScope.launch(Dispatchers.IO) {
            appDatabase.clearAllTablesAndReset()
        }
    }
}