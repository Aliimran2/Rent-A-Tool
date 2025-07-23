package com.miassolutions.rentatool.ui.fragments.forms.customer

import android.os.Bundle
import android.view.View
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.miassolutions.rentatool.R
import com.miassolutions.rentatool.utils.extenstions.collectingFlow
import com.miassolutions.rentatool.utils.extenstions.setTextIfChanged
import com.miassolutions.rentatool.utils.extenstions.showToast
import com.miassolutions.rentatool.utils.helper.clearInputs
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
            viewModel.uiEvent.collect { event ->
                when (event) {
                    CustomerUiEvent.NavigateBack -> {
                        findNavController().popBackStack()
                    }
                    is CustomerUiEvent.ShowToast -> {
                        binding.etCnic.error = "CNIC already existed."
                        binding.etCnic.requestFocus()
                        showToast(event.message)
                    }

                    is CustomerUiEvent.CustomerAdded -> {
                        showToast("Customer added in db successfully with Id : ${event.customerId}")
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

                }

            }
        }

    }


    private fun validateInputs(): Boolean {
        with(binding) {
            etCustomerName.error = null
            etCustomerPhone.error = null
            etCnic.error = null

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

                else -> true
            }

        }

    }

    private fun clearAllFields() {
        with(binding) {
            clearInputs(
                etCustomerName,
                etCustomerPhone,
                etCnic
            )
            binding.etCnic.requestFocus()
        }


    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}