package com.miassolutions.rentatool.ui.fragments

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.miassolutions.rentatool.R
import com.miassolutions.rentatool.data.model.Customer
import com.miassolutions.rentatool.databinding.FragmentAddCustomerBinding
import com.miassolutions.rentatool.ui.viewmodels.RentalViewModel
import com.miassolutions.rentatool.utils.helper.clearInputs
import com.miassolutions.rentatool.utils.helper.showToast

class AddCustomerFragment : Fragment(R.layout.fragment_add_customer) {

    private var _binding: FragmentAddCustomerBinding? = null
    private val binding get() = _binding!!

    private val rentalViewModel: RentalViewModel by activityViewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentAddCustomerBinding.bind(view)

        setupSubmitBtn()

    }

    private fun setupSubmitBtn() {
        binding.btnSubmit.setOnClickListener {
            val customer = collectCustomer()
            if (customer != null) {

                rentalViewModel.addCustomer(customer)
                showToast(
                    requireContext(),
                    getString(R.string.is_saved_successfully, customer.customerName)
                )
                clearInputFields()
            }

        }
    }

    private fun collectCustomer(): Customer? {
        binding.apply {
            val customerPic = ""
            val customerName = etCustomerName.text.toString()
            val customerCnic = etCnic.text.toString()
            val customerPhone = etCustomerPhone.text.toString()
            val constructionPlace = etConstructionPlace.text.toString()
            val contractorName = etContractorName.text.toString()
            val contractorPhone = etContractorPhone.text.toString()
            val ownerName = etOwnerName.text.toString()
            val ownerPhone = etOwnerPhone.text.toString()

            if (validateInputs()) {
                return Customer(
                    customerId = System.currentTimeMillis(),
                    customerName = customerName,
                    cnicNumber = customerCnic,
                    customerPhone = customerPhone,
                    constructionPlace = constructionPlace,
                    contractorName = contractorName,
                    contractorPhone = contractorPhone,
                    ownerName = ownerName,
                    ownerPhone = ownerPhone
                )
            }
        }
        return null
    }

    private fun validateInputs(): Boolean {
        return when {
            binding.etCustomerName.text.isNullOrEmpty() -> {
                showToast(requireContext(), getString(R.string.please_enter_customer_name))
                false
            }

            binding.etCnic.text.isNullOrEmpty() -> {
                showToast(requireContext(), getString(R.string.please_enter_customer_cnic))
                false
            }

            binding.etCustomerPhone.text.isNullOrEmpty() -> {
                showToast(requireContext(), getString(R.string.please_enter_customer_phone_no))
                false
            }

            binding.etConstructionPlace.text.isNullOrEmpty() -> {
                showToast(requireContext(), getString(R.string.please_enter_construction_place))

                false
            }

            binding.etContractorName.text.isNullOrEmpty() -> {
                showToast(requireContext(), getString(R.string.please_enter_contractor_name))
                false
            }

            binding.etContractorPhone.text.isNullOrEmpty() -> {
                showToast(requireContext(), getString(R.string.please_enter_contractor_phone_no))
                false
            }

            binding.etOwnerName.text.isNullOrEmpty() -> {
                showToast(requireContext(), getString(R.string.please_enter_owner_name))
                false
            }

            binding.etOwnerPhone.text.isNullOrEmpty() -> {
                showToast(requireContext(), getString(R.string.please_enter_owner_phone))
                false
            }

            else -> true
        }
    }

    private fun clearInputFields() {
        binding.apply {
            clearInputs(
                etCustomerName,
                etCnic,
                etCustomerPhone,
                etConstructionPlace,
                etContractorName,
                etContractorPhone,
                etOwnerName,
                etOwnerPhone
            )
        }
    }



    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}