package com.miassolutions.rentatool.ui.fragments.lists.customers

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import androidx.core.view.MenuProvider
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.navigation.fragment.findNavController
import com.miassolutions.rentatool.R
import com.miassolutions.rentatool.utils.extenstions.collectingFlow
import com.miassolutions.rentatool.utils.extenstions.showToast
import com.miassolutions.rentatool.databinding.FragmentCustomersListBinding
import com.miassolutions.rentatool.ui.adapters.CustomerListAdapter
import com.miassolutions.rentatool.ui.viewmodel.MainViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest

@AndroidEntryPoint
class CustomersListFragment : Fragment(R.layout.fragment_customers_list) {

    private var _binding: FragmentCustomersListBinding? = null
    private val binding get() = _binding!!

    private val viewModel by viewModels<CustomerListViewModel>()
    private val mainViewModel by viewModels<MainViewModel>()
    private lateinit var adapter: CustomerListAdapter


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentCustomersListBinding.bind(view)


        menuProvider()
        setupRecyclerView()
        observeUiState()
        observeUiEvents()
        setupSearchField()


    }

    private fun setupRecyclerView() {
        adapter = CustomerListAdapter(
            navToRentals = { customer ->
                viewModel.navToRentals(customer.customerId, customer.customerName)
            },
            navToDetails = { customer ->
                // to details for update or delete customer
            },
            navToRentTools = { customer ->
                viewModel.navToRentTools(customer.customerId, customer.customerName)
            },
            navToLedger = {customer ->
                // to ledger of customer
            },
        )
        binding.rvCustomerList.adapter = adapter


    }

    private fun setupSearchField() {
        binding.searchInputLayout.editText?.doAfterTextChanged { text ->
            viewModel.onSearchQueryChanged(text.toString())
        }
    }


    private fun observeUiState() {
        collectingFlow {
            viewModel.uiState.collectLatest { state ->
                adapter.submitList(state.customerList)
            }
        }

    }

    private fun observeUiEvents() {

        collectingFlow {
            viewModel.uiEvent.collectLatest { event ->
                when (event) {
                    is CustomerListUiEvent.NavToCustomerRentals -> {
                        findNavController().navigate(
                            CustomersListFragmentDirections.actionCustomersListFragmentToFragmentRentals(
                                event.customerId,
                                event.customerName
                            )
                        )
                    }

                    is CustomerListUiEvent.NavToCustomerDetail -> {
                        showToast("Navigation to edit customer")
                    }

                    is CustomerListUiEvent.ShowToast -> {
                        showToast(event.message)
                    }

                    is CustomerListUiEvent.NavToRentTools -> {
                        val action =
                            CustomersListFragmentDirections.actionCustomersListFragmentToToolSelectionFragment(
                                event.customerId,
                                event.customerName
                            )

                        findNavController().navigate(action)
                    }
                }
            }
        }

    }


    private fun menuProvider() {
        requireActivity().addMenuProvider(object : MenuProvider {
            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                menuInflater.inflate(R.menu.main_menu, menu)
            }

            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                return when (menuItem.itemId) {
                    R.id.add_customer_menu -> {
                        findNavController().navigate(R.id.customerFormFragment)
                        true
                    }


                    else -> false
                }
            }
        }, viewLifecycleOwner, Lifecycle.State.RESUMED)

    }


    private fun initializePhoneCall(phoneNumber: String) {
        try {
            val intent = Intent(Intent.ACTION_DIAL).apply {
                data = Uri.parse("tel:$phoneNumber")  // This opens the dialer with the number
            }
            startActivity(intent)
        } catch (e: Exception) {
            //show toast
        }
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}