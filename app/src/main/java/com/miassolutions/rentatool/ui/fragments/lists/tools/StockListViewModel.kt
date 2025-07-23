package com.miassolutions.rentatool.ui.fragments.lists.tools

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.miassolutions.rentatool.data.repositoryimpl.ToolRepositoryImpl
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class StockListViewModel @Inject constructor(private val toolRepository: ToolRepositoryImpl) : ViewModel() {

    private val _uiState = MutableStateFlow(StockUiState())
    val uiState = _uiState.asStateFlow()

    private val _uiEvent = MutableSharedFlow<StockUiEvent>()
    val uiEvent = _uiEvent.asSharedFlow()

    init {
        observeStockList()
    }


    fun onSearchQueryChanged(query: String) {
        _uiState.update { it.copy(searchQuery = query) }
    }





    @OptIn(FlowPreview::class, ExperimentalCoroutinesApi::class)
    private fun observeStockList() {
        viewModelScope.launch {

            _uiState.map { it.searchQuery }

                .debounce(300L)
                .distinctUntilChanged()
                .flatMapLatest { query ->
                    _uiState.update { it.copy(isLoading = true, errorMessage = null) }
                    try {

                        if (query.isBlank()) {
                            toolRepository.getAllTools()
                        } else {
                            toolRepository.searchTool(query)
                        }
                    } catch (e: Exception) {
                        _uiState.update { it.copy(errorMessage = "Failed to load tools : ${e.message}") }
                        flowOf(emptyList())
                    }

                }.collect { stockList ->
                    _uiState.update {
                        it.copy(
                            stockList = stockList,
                            isLoading = false,
                            errorMessage = null
                        )
                    }
                }
        }
    }

}