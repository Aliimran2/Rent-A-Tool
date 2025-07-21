package com.miassolutions.rentatool.ui.fragments.forms.customer

import androidx.lifecycle.ViewModel
import com.miassolutions.rentatool.data.repository.ToolRentalRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class CustomerFormViewModel @Inject constructor(private val repository: ToolRentalRepository) :ViewModel() {

}