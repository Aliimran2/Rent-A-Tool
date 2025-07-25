package com.miassolutions.rentatool.ui.fragments.lists.rentalrecord

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.miassolutions.rentatool.R
import com.miassolutions.rentatool.databinding.FragmentRentalsBinding
import com.miassolutions.rentatool.ui.adapters.RentalListAdapter
import com.miassolutions.rentatool.utils.Constants
import com.miassolutions.rentatool.utils.extenstions.collectingFlow
import com.miassolutions.rentatool.utils.extenstions.showSnackbar
import com.miassolutions.rentatool.utils.extenstions.showToast
import com.miassolutions.rentatool.utils.helper.hide
import com.miassolutions.rentatool.utils.helper.show
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.collectLatest

@AndroidEntryPoint
class RentalsFragment : Fragment(R.layout.fragment_rentals) {

    private val args: RentalsFragmentArgs by navArgs()
    private val viewModel by viewModels<RentalsViewModel>()
    private val adapter = RentalListAdapter()

    private var _binding: FragmentRentalsBinding? = null
    private val binding get() = _binding!!


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentRentalsBinding.bind(view)

        loadRentals()
        setupUiState()
        setupListeners()
        setupUiEvent()
        setupRecyclerview()


    }

    private fun loadRentals() {
        viewModel.loadData(args.customerId)
    }

    private fun setupRecyclerview() {
        binding.rvRentals.adapter = adapter
    }


    private fun setupUiEvent() {
        collectingFlow {
            viewModel.uiEvent.collect { event ->
                when (event) {
                    is RentalsUiEvent.NavigationToRentTools -> {


                    }

                    is RentalsUiEvent.NavigationToRentalDetail -> {
                        val action =
                            RentalsFragmentDirections.actionFragmentRentalsToRentalDetailFragment(
                                event.orderId
                            )
                        findNavController().navigate(action)

                    }

                    is RentalsUiEvent.NavigationToReturnTools -> {
                        val action =
                            RentalsFragmentDirections.actionFragmentRentalsToReturnToolsFragment(
                                event.orderId,
                                event.customerName
                            )
                        findNavController().navigate(action)
                    }

                    is RentalsUiEvent.ShowSnackbar -> {
                        binding.root.showSnackbar(event.message)
                    }
                }

            }
        }
    }

    private fun setupListeners() {
        binding.btnRentTools.setOnClickListener {
            viewModel.onRentToolsClick()
        }
    }

    private fun setupUiState() {
        collectingFlow {
            viewModel.uiState.collect { state ->
                adapter.submitList(state.rentalOrders)

                with(binding) {
                    if (state.rentalOrders.isEmpty()) {
                        emptyStateLayout.root.show()
                        cardSummary.hide()

                    } else {
                        cardSummary.show()
                        emptyStateLayout.root.hide()
                        tvTotalRent.text = "Rs${state.totalRent}"
                        tvActiveOrders.text = state.activeOrdersCount.toString()
                        tvReturnedOrders.text = state.returnedOrderCount.toString()
                    }


                }

            }

        }

    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


}