package com.miassolutions.rentatool.ui.fragments.mainfragments.returntool

import androidx.lifecycle.ViewModel
import com.miassolutions.rentatool.data.repository.MainRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class ReturnToolsViewModel @Inject constructor(
    private val repository: MainRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(ReturnToolsUiState())
    val uiState: StateFlow<ReturnToolsUiState> = _uiState.asStateFlow()

    private val _event = MutableSharedFlow<ReturnToolsUiEvent>()
    val event = _event.asSharedFlow()





}




