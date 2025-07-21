package com.miassolutions.rentatool.ui.fragments.forms.tool

import androidx.lifecycle.ViewModel
import com.miassolutions.rentatool.data.repository.ToolRentalRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ToolFormViewModel @Inject constructor(private val repository: ToolRentalRepository) :
    ViewModel() {

}