package com.miassolutions.rentatool.ui.fragments

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.navArgs
import com.miassolutions.rentatool.MyApplication
import com.miassolutions.rentatool.R
import com.miassolutions.rentatool.data.model.Customer
import com.miassolutions.rentatool.databinding.FragmentCustomerDetailsBinding
import com.miassolutions.rentatool.ui.viewmodels.SharedViewModel
import com.miassolutions.rentatool.ui.viewmodels.SharedViewModelFactory

class CustomerDetailsFragment : Fragment(R.layout.fragment_customer_details) {

    private var _binding: FragmentCustomerDetailsBinding? = null
    private val binding get() = _binding!!

    private val rentalViewModel: SharedViewModel by activityViewModels {
        SharedViewModelFactory((requireActivity().application as MyApplication).repository)
    }

    private val args : CustomerDetailsFragmentArgs by navArgs()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentCustomerDetailsBinding.bind(view)

        val customerId = args.customerId
        rentalViewModel.getCustomerById(customerId)

        observeViewModel()

    }

    private fun setupUI(customer: Customer) {
        binding.apply {
            tvCustomerName.text = customer.customerName
            tvCnic.text = customer.cnicNumber
            tvPlace.text = customer.constructionPlace
            tvContractorName.text = customer.contractorName
            tvContractorName.text = customer.contractorPhone
            tvOwnerName.text = customer.ownerName
            tvOwnerPhone.text = customer.ownerPhone
        }
    }

    private fun observeViewModel() {
        rentalViewModel.customer.observe(viewLifecycleOwner) { customer ->

            customer?.let {
                setupUI(customer)
            }
        }
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}