package com.miassolutions.rentatool.ui.fragments

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.miassolutions.rentatool.R
import com.miassolutions.rentatool.databinding.FragmentCustomersListBinding
import com.miassolutions.rentatool.ui.adapters.CustomerListAdapter
import com.miassolutions.rentatool.ui.viewmodels.RentalViewModel
import com.miassolutions.rentatool.core.utils.helper.showToast

class CustomersListFragment : Fragment(R.layout.fragment_customers_list) {

    private var _binding: FragmentCustomersListBinding? = null
    private val binding get() = _binding!!

    private val rentalViewModel: RentalViewModel by activityViewModels()
    private lateinit var adapter: CustomerListAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentCustomersListBinding.bind(view)

        setupUI()
        observeViewModel()

    }

    private fun setupUI() {
        adapter = CustomerListAdapter(
            dialerClickListener = {initializePhoneCall(it.customerPhone)},
            navigationClickListener = { showToast(requireContext(), "Edit the customer") }
        )
        binding.rvCustomerList.adapter = adapter
        binding.btnAddCustomer.setOnClickListener {
            findNavController().navigate(R.id.addCustomerFragment)
        }
    }

    private fun observeViewModel() {
        rentalViewModel.customers.observe(viewLifecycleOwner) {
             adapter.submitList(it)
        }
    }

    private fun initializePhoneCall(phoneNumber : String){
        try {
            val intent = Intent(Intent.ACTION_DIAL).apply {
                data = Uri.parse("tel:$phoneNumber")  // This opens the dialer with the number
            }
            startActivity(intent)
        } catch (e: Exception) {
            showToast(requireContext(), "Unable to open the dialer.")
        }
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}