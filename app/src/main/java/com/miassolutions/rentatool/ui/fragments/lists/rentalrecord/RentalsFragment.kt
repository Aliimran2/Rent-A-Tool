package com.miassolutions.rentatool.ui.fragments.lists.rentalrecord

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import com.miassolutions.rentatool.R
import com.miassolutions.rentatool.databinding.FragmentRentalsBinding
import com.miassolutions.rentatool.ui.adapters.RentalListAdapter
import com.miassolutions.rentatool.utils.extenstions.collectingFlow
import dagger.hilt.android.AndroidEntryPoint
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


    }

    private fun setupUiState() {
        collectingFlow {
            viewModel.uiState.collect { state ->
                adapter.submitList(state.rentalList)
            }
        }
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


}