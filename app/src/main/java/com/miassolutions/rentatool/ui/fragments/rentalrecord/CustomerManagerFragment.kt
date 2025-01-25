package com.miassolutions.rentatool.ui.fragments.rentalrecord

import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import androidx.core.view.MenuProvider
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.miassolutions.rentatool.R
import com.miassolutions.rentatool.core.utils.extenstions.showToast
import com.miassolutions.rentatool.data.model.Customer
import com.miassolutions.rentatool.databinding.FragmentCustomerManagerBinding
import com.miassolutions.rentatool.myapplication.MyApplication
import com.miassolutions.rentatool.ui.adapters.RentalListAdapter
import com.miassolutions.rentatool.ui.viewmodels.SharedViewModel
import com.miassolutions.rentatool.ui.viewmodels.SharedViewModelFactory


class CustomerManagerFragment : Fragment(R.layout.fragment_customer_manager) {
    companion object {
        private const val TAG = "CustomerManagerFragment"
    }

    private val rentalViewModel: SharedViewModel by activityViewModels {
        SharedViewModelFactory((requireActivity().application as MyApplication).repository)
    }

    private val args: CustomerManagerFragmentArgs by navArgs()

    private var _binding: FragmentCustomerManagerBinding? = null
    private val binding get() = _binding!!

    var customerId: Long? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentCustomerManagerBinding.bind(view)


        customerId = args.customerId



        val adapter = RentalListAdapter {
            val rentalId: Long = 2L
            val action =
                CustomerManagerFragmentDirections.actionCustomerManagerFragmentToRentalDetailsFragment(
                    rentalId

                )
            findNavController().navigate(action)
        }

        rentalViewModel.rentalsByCustomer(customerId!!).observe(viewLifecycleOwner) {
            adapter.submitList(it)
        }

        binding.rvCustomerManager.adapter = adapter

        binding.rentToolsBtn.setOnClickListener {
           rentalViewModel.getCustomerById(customerId!!).observe(viewLifecycleOwner){customer ->
               if (customer != null){
                   navigateToToolsSelections(customer)
               }
           }
        }


        requireActivity().addMenuProvider(object : MenuProvider {
            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                menuInflater.inflate(R.menu.customer_fragment_menu, menu)
            }

            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                return when (menuItem.itemId) {
                    R.id.menu_pdf_report -> {
                        showToast("Creating pdf...")
                        true
                    }

                    R.id.remind_menu, R.id.call_menu -> {
                        showToast("Reminding the customer")
                        true
                    }

                    R.id.call_menu -> {
                        showToast("Call the customer")
                        true
                    }


                    else -> false
                }
            }
        }, viewLifecycleOwner)


    }

    private fun navigateToToolsSelections(customer: Customer){
        val customerName = customer.customerName
        val action =
            CustomerManagerFragmentDirections.actionCustomerManagerFragmentToToolSelectionFragment(
                customerId!!,
                customerName
            )
        findNavController().navigate(action)
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


}