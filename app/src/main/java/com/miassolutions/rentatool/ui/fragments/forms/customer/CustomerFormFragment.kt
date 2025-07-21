package com.miassolutions.rentatool.ui.fragments.forms.customer

import android.os.Bundle
import android.view.View
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.miassolutions.rentatool.R
import com.miassolutions.rentatool.core.utils.extenstions.collectingFlow
import com.miassolutions.rentatool.core.utils.extenstions.setTextIfChanged
import com.miassolutions.rentatool.core.utils.extenstions.showToast
import com.miassolutions.rentatool.core.utils.helper.clearInputs
import com.miassolutions.rentatool.databinding.FragmentCustomerFormBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest

@AndroidEntryPoint
class CustomerFormFragment : Fragment(R.layout.fragment_customer_form) {

    private var _binding: FragmentCustomerFormBinding? = null
    private val binding get() = _binding!!

    private val viewModel by viewModels<CustomerFormViewModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentCustomerFormBinding.bind(view)


        setupStateObserver()
        setupEventObserver()
        setupListeners()


    }

    private fun setupListeners() {

        with(binding) {
            etCustomerName.doAfterTextChanged { viewModel.onCustomerNameChange(it.toString()) }
            etCustomerPhone.doAfterTextChanged { viewModel.onCustomerPhoneChange(it.toString()) }
            etCnic.doAfterTextChanged { viewModel.onCnicChange(it.toString()) }
//            etContractorName.doAfterTextChanged { viewModel.onContractorNameChange(it.toString()) }
//            etContractorPhone.doAfterTextChanged { viewModel.onContractorPhoneChange(it.toString()) }
//            etConstructionPlace.doAfterTextChanged { viewModel.onConstructionPlaceChange(it.toString()) }
//            etOwnerName.doAfterTextChanged { viewModel.onOwnerNameChange(it.toString()) }
//            etOwnerPhone.doAfterTextChanged { viewModel.onOwnerPhoneChange(it.toString()) }

            saveAndNewBtn.setOnClickListener {
                if (validateInputs()) {
                    viewModel.onSaveClicked(isSaveAndExit = false)

                }
            }

            saveAndExitBtn.setOnClickListener {
                if (validateInputs()) {
                    viewModel.onSaveClicked(isSaveAndExit = true)
                }
            }
        }


    }

    private fun setupEventObserver() {
        collectingFlow {
            viewModel.uiEvent.collectLatest { event ->
                when (event) {
                    CustomerUiEvent.NavigateBack -> {
                        findNavController().popBackStack()
                    }
                    is CustomerUiEvent.ShowToast -> showToast(event.message)
                    is CustomerUiEvent.DuplicateCNIC -> {
                        binding.etCnic.error = "CNIC already existed."
                        binding.etCnic.requestFocus()
                    }

                    is CustomerUiEvent.CustomerAdded -> {
                        showToast("Customer added in database successfully")
                        clearAllFields()
                    }
                }
            }
        }
    }

    private fun setupStateObserver() {
        collectingFlow {
            viewModel.uiState.collectLatest { state ->
                binding.apply {
                    etCustomerName.setTextIfChanged(state.customerName)
                    etCustomerPhone.setTextIfChanged(state.customerPhone)
                    etCnic.setTextIfChanged(state.customerCnic)
                    etContractorName.setTextIfChanged(state.contractorName)
                    etContractorPhone.setTextIfChanged(state.contractorPhone)
                    etConstructionPlace.setTextIfChanged(state.constructionPlace)
                    etOwnerName.setTextIfChanged(state.ownerName)
                    etOwnerPhone.setTextIfChanged(state.ownerPhone)

                }

            }
        }

    }


    private fun validateInputs(): Boolean {
        with(binding) {
            etCustomerName.error = null
            etCustomerPhone.error = null
            etCnic.error = null
            etOwnerName.error = null
            etOwnerPhone.error = null
            etContractorName.error = null
            etContractorPhone.error = null
            etConstructionPlace.error = null


            return when {

                etCnic.text.isNullOrEmpty() -> {
                    etCnic.error = "Enter customer cnic no."
                    etCnic.requestFocus()
                    false
                }

                etCustomerName.text.isNullOrEmpty() -> {
                    etCustomerName.error = "Enter customer name"
                    etCustomerName.requestFocus()
                    false
                }

                etCustomerPhone.text.isNullOrEmpty() -> {
                    etCustomerPhone.error = "Enter customer phone no."
                    etCustomerPhone.requestFocus()
                    false
                }


//                etOwnerName.text.isNullOrEmpty() -> {
//                    etOwnerName.error = "Enter owner name"
//                    etOwnerName.requestFocus()
//                    false
//                }
//
//                etOwnerPhone.text.isNullOrEmpty() -> {
//                    etOwnerPhone.error = "Enter owner phone no."
//                    etOwnerPhone.requestFocus()
//                    false
//                }
//
//                etConstructionPlace.text.isNullOrEmpty() -> {
//                    etConstructionPlace.error = "Enter construction place"
//                    etConstructionPlace.requestFocus()
//                    false
//                }
//
//                etContractorName.text.isNullOrEmpty() -> {
//                    etContractorName.error = "Enter contractor name"
//                    etContractorName.requestFocus()
//                    false
//                }
//
//                etContractorPhone.text.isNullOrEmpty() -> {
//                    etContractorPhone.error = "Enter contractor phone"
//                    etContractorPhone.requestFocus()
//                    false
//                }

                else -> true
            }

        }

    }

    private fun clearAllFields() {
        with(binding) {
            clearInputs(
                etCustomerName,
                etCustomerPhone,
                etCnic,
                etContractorName,
                etContractorPhone,
                etOwnerName,
                etOwnerPhone,
                etConstructionPlace
            )
        }

    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}