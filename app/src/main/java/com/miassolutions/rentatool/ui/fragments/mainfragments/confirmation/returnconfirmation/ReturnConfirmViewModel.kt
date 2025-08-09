package com.miassolutions.rentatool.ui.fragments.mainfragments.confirmation.returnconfirmation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.miassolutions.rentatool.data.entities.ReturnedToolEntity
import com.miassolutions.rentatool.data.repository.MainRepository
import com.miassolutions.rentatool.ui.fragments.mainfragments.returntool.ReturnToolItem
import com.miassolutions.rentatool.ui.fragments.mainfragments.returntool.ReturnToolsUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ReturnConfirmViewModel @Inject constructor(private val repository: MainRepository) : ViewModel() {

    private val _uiState = MutableStateFlow(ReturnToolsUiState())
    val uiState = _uiState.asStateFlow()


    fun loadReturnToolList(tools: List<ReturnToolItem>) {
        _uiState.update {
            it.copy(tools = tools)
        }
    }

    fun performReturnTransaction(orderId : Long, returnsList : List<ReturnedToolEntity>){
        viewModelScope.launch {
            repository.performReturnTransaction(orderId, returnsList)
        }
    }

}