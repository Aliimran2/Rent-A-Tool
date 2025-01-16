package com.miassolutions.rentatool.ui.fragments

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.miassolutions.rentatool.MyApplication
import com.miassolutions.rentatool.R
import com.miassolutions.rentatool.core.utils.extenstions.showCustomerSelectionBottomSheet
import com.miassolutions.rentatool.core.utils.extenstions.showToast
import com.miassolutions.rentatool.databinding.FragmentRentalBinding
import com.miassolutions.rentatool.ui.adapters.RentalListAdapter
import com.miassolutions.rentatool.ui.viewmodels.SharedViewModel
import com.miassolutions.rentatool.ui.viewmodels.SharedViewModelFactory


class RentalFragment : Fragment(R.layout.fragment_rental) {

    private var _binding: FragmentRentalBinding? = null
    private val binding get() = _binding!!

    private val rentalViewModel: SharedViewModel by activityViewModels {
        SharedViewModelFactory((requireActivity().application as MyApplication).repository)
    }

    private lateinit var rentalListAdapter: RentalListAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentRentalBinding.bind(view)

        setupUI()
        setupInitialState()
        observeViewModel()
    }

    private fun setupUI() {
        rentalListAdapter = RentalListAdapter(
            onClickListener = { rental ->
               showToast(rental.rentalId.toString())

            }
        )
        binding.rvReturnedToolsList.adapter = rentalListAdapter

        // Initially hide the list
        binding.rvReturnedToolsList.isVisible = false

        binding.etCustomerName.setOnClickListener {
            showCustomerSelection()
        }
    }

    private fun navigateToRentalDetails(rentalId: Long) {
        val action = RentalFragmentDirections.actionRentalFragmentToRentalDetailsFragment(rentalId)
        findNavController().navigate(action)

    }

    private fun setupInitialState() {
        // Clear rentals list and hide it to avoid showing old data on initialization
        rentalViewModel.setCustomer(null) // Reset selected customer
        rentalListAdapter.submitList(emptyList())
        binding.rvReturnedToolsList.isVisible = false
    }

    private fun observeViewModel() {
        rentalViewModel.customer.observe(viewLifecycleOwner) { customer ->
            if (customer == null) {
                // No customer selected, clear rentals and hide list
                rentalListAdapter.submitList(emptyList())
                binding.rvReturnedToolsList.isVisible = false
            } else {
                // Customer selected, load rentals
                loadRentalsForCustomer(customer.customerId)
            }
        }
    }

    private fun loadRentalsForCustomer(customerId: Long) {
        rentalViewModel.searchRentalsByCustomer(customerId).observe(viewLifecycleOwner) { rentals ->
            rentalListAdapter.submitList(rentals)
            binding.rvReturnedToolsList.isVisible = rentals.isNotEmpty()
        }
    }

    private fun showCustomerSelection() {
        rentalViewModel.allCustomers.observe(viewLifecycleOwner) { customers ->
            if (!customers.isNullOrEmpty()) {
                showCustomerSelectionBottomSheet(customers) { selectedCustomer ->
                    rentalViewModel.setCustomer(selectedCustomer)
                    binding.etCustomerName.setText(selectedCustomer.customerName)
                }
            } else {
                Log.d(TAG, "No customers found")
            }
        }
    }



    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        private const val TAG = "RentalFragment"
    }
}
