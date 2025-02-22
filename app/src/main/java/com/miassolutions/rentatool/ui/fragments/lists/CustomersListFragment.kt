package com.miassolutions.rentatool.ui.fragments.lists

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import androidx.core.view.MenuProvider
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.miassolutions.rentatool.myapplication.MyApplication
import com.miassolutions.rentatool.R
import com.miassolutions.rentatool.core.utils.helper.showToast
import com.miassolutions.rentatool.data.model.Customer
import com.miassolutions.rentatool.databinding.FragmentCustomersListBinding
import com.miassolutions.rentatool.ui.adapters.CustomerListAdapter
import com.miassolutions.rentatool.ui.fragments.entries.AddToolFragment
import com.miassolutions.rentatool.ui.viewmodels.SharedViewModel
import com.miassolutions.rentatool.ui.viewmodels.SharedViewModelFactory
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class CustomersListFragment : Fragment(R.layout.fragment_customers_list) {

    private var _binding: FragmentCustomersListBinding? = null
    private val binding get() = _binding!!

    private val rentalViewModel: SharedViewModel by activityViewModels {
        SharedViewModelFactory((requireActivity().application as MyApplication).repository)
    }
    private lateinit var adapter: CustomerListAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentCustomersListBinding.bind(view)



        setupUI()
        observeViewModel()

    }


    private fun menuProvider() {
        requireActivity().addMenuProvider(object : MenuProvider {
            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                menuInflater.inflate(R.menu.main_menu, menu)
            }

            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                return when (menuItem.itemId) {
                    R.id.add_customer_menu -> {
                        findNavController().navigate(R.id.addCustomerFragment)
                        true
                    }

                    R.id.add_tool_menu -> {
                        val showBottomSheet = AddToolFragment()
                        showBottomSheet.show(parentFragmentManager, showBottomSheet.tag)
                        true
                    }

                    else -> false
                }
            }
        }, viewLifecycleOwner)

    }


    private fun setupUI() {
        menuProvider()

        adapter = CustomerListAdapter(
            navigationClickListener = { navigateToCustomerManagerFragment(it) },
            navigateToDetailsListener = { navigateToDetails(it) }
        )

        binding.searchView.setOnQueryTextListener(object :
            androidx.appcompat.widget.SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                if (query.isNullOrEmpty()) {

                }
                query?.let { rentalViewModel.searchCustomer(it) }
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                rentalViewModel.searchCustomer(newText ?: "")
                return true
            }
        })
        binding.rvCustomerList.adapter = adapter
        binding.searchView.setOnQueryTextListener(object : androidx.appcompat.widget.SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(query: String?): Boolean {
                query?.let { rentalViewModel.searchCustomer(it) }
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                rentalViewModel.searchCustomer(newText ?: "")
                return true
            }
        })

    }

    private fun observeViewModel() {

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED){
                rentalViewModel.customerSearchResult.collectLatest {searchResult ->
                    adapter.submitList(searchResult)

                }
            }
        }
    }

    private fun navigateToDetails(customer: Customer) {
        val action =
            CustomersListFragmentDirections.actionCustomersListFragmentToCustomerDetailsFragment(
                customer.customerId
            )
        findNavController().navigate(action)
    }



    private fun navigateToCustomerManagerFragment(customer: Customer) {
        val customerId = customer.customerId
        val customerName = customer.customerName
        val action =
            CustomersListFragmentDirections.actionCustomersListFragmentToCustomerManagerFragment(
                customerId,
                customerName
            )
        findNavController().navigate(action)
    }

    private fun initializePhoneCall(phoneNumber: String) {
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