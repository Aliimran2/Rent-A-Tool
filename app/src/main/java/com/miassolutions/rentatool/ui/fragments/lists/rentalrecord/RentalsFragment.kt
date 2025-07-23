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
import com.miassolutions.rentatool.utils.extenstions.showToast
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.collectLatest

@AndroidEntryPoint
class RentalsFragment : Fragment(R.layout.fragment_rentals) {

    private val args: RentalsFragmentArgs by navArgs()
    private val viewModel by viewModels<RentalViewModel>()
    private val adapter = RentalListAdapter()

    private var _binding: FragmentRentalsBinding? = null
    private val binding get() = _binding!!


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentRentalsBinding.bind(view)

        viewModel.loadRentals(args.customerId)

        setupUiState()
        setupListeners()
        setupUiEvent()
        setupRecyclerview()


    }

    private fun setupRecyclerview() {
        binding.rvRentals.adapter = adapter
    }

    private fun setupUiEvent() {
        collectingFlow {
            viewModel.uiEvent.collect{event ->
                when(event){
                    is RentalUiEvent.NavigationToRentTools -> {
                        val action = RentalsFragmentDirections.actionFragmentRentalsToToolSelectionFragment(args.customerId)
                        findNavController().navigate(action)
                    }
                    is RentalUiEvent.NavigationToUpdateRentals -> {
                        showToast("Navigation to Updating Rentals")
                    }
                    is RentalUiEvent.ShowToast -> {
                        showToast(event.message)
                    }
                }

            }
        }
    }

    private fun setupListeners() {
        binding.rentToolsBtn.setOnClickListener {
            viewModel.onRentToolsClick()
        }
    }

    private fun setupUiState() {
        collectingFlow {
            viewModel.uiState.collect { state ->
                Log.d(Constants.TAG, "${state.rentalList} - ${state.customerId}")
                adapter.submitList(state.rentalList)
            }
        }
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


}