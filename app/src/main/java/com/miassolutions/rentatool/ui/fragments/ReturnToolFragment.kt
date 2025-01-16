package com.miassolutions.rentatool.ui.fragments

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.miassolutions.rentatool.MyApplication
import com.miassolutions.rentatool.R
import com.miassolutions.rentatool.core.utils.extenstions.showCustomerSelectionBottomSheet
import com.miassolutions.rentatool.data.model.Rental
import com.miassolutions.rentatool.data.model.RentalDetail
import com.miassolutions.rentatool.databinding.FragmentReturnToolBinding
import com.miassolutions.rentatool.ui.adapters.RentalDetailAdapter
import com.miassolutions.rentatool.ui.viewmodels.SharedViewModel
import com.miassolutions.rentatool.ui.viewmodels.SharedViewModelFactory

const val TAG = "ReturnToolFragment"

class ReturnToolFragment : Fragment(R.layout.fragment_return_tool) {

    private var _binding: FragmentReturnToolBinding? = null
    private val binding get() = _binding!!

    private val rentalViewModel: SharedViewModel by activityViewModels {
        SharedViewModelFactory((requireActivity().application as MyApplication).repository)
    }
    private var selectedCustomer: Long = 0L
    private lateinit var rentalDetailAdapter: RentalDetailAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentReturnToolBinding.bind(view)







        setupRecyclerView()
        setupUI()
        observeViewModel()


    }

    private fun setupRecyclerView() {

        rentalViewModel.allTools.observe(viewLifecycleOwner) { tools ->

            rentalDetailAdapter = RentalDetailAdapter(tools)
            binding.rvReturnedToolsList.apply {
                adapter = rentalDetailAdapter
            }
        }
    }

    private fun setupUI() {
        binding.apply {
            etCustomerName.setOnClickListener {
                showCustomerSelection()
            }
        }

    }

    private fun observeViewModel() {

        rentalViewModel.customer.observe(viewLifecycleOwner) { customer ->
            customer?.let {

                val customerId = it.customerId
                Log.d(TAG, "Selected Customer : $customerId")

                rentalViewModel.searchRentalsByCustomer(customerId)
                    .observe(viewLifecycleOwner) { rentals ->
                        Log.d(TAG, "Rentals : $rentals")

                        rentals.forEach { rental ->
                            rentalViewModel.searchRentalDetailsByRental(rental.rentalId)
                                .observe(viewLifecycleOwner) { rentalDetails ->
                                    rentalDetailAdapter.submitList(rentalDetails)
                                    Log.d(
                                        TAG,
                                        "Rental ID : ${rental.rentalId}: Details : ${rentalDetails}"
                                    )
                                    updateRentalDetails(rental.rentalId, rentalDetails)
                                }
                        }
                    }
            }
        }

        rentalViewModel.searchRentalsByCustomer(selectedCustomer)
            .observe(viewLifecycleOwner) { rentals ->
                updateRentalList(rentals)

            }
    }

    private fun updateRentalDetails(rentalId: Long, rentalDetails: List<RentalDetail>) {
        Log.d(TAG, "Updated Rental Details for Rental ID $rentalId: $rentalDetails")
        // Update your UI with rental details for the given rentalId
    }

    private fun updateRentalList(rental: List<Rental>) {
        Log.d(TAG, "$rental")
    }

    private fun showCustomerSelection() {
        rentalViewModel.allCustomers.observe(viewLifecycleOwner) { customers ->

            if (!customers.isNullOrEmpty()) {
                showCustomerSelectionBottomSheet(customers) { customer ->
                    binding.etCustomerName.text?.clear()
                    rentalViewModel.setCustomer(customer)
                    binding.etCustomerName.setText(customer.customerName)
                }
            } else {
                Log.d(TAG, "no customer found")
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}