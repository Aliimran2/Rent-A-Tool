package com.miassolutions.rentatool.ui.fragments.confirmation

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.miassolutions.rentatool.R
import com.miassolutions.rentatool.databinding.FragmentConfirmRentingToolsBinding
import com.miassolutions.rentatool.databinding.FragmentRentalConfirmationBinding
import com.miassolutions.rentatool.ui.fragments.confirmation.rentconfirmation.ConfirmationUiEvent
import com.miassolutions.rentatool.ui.fragments.confirmation.rentconfirmation.ConfirmationViewModel
import com.miassolutions.rentatool.ui.fragments.confirmation.rentconfirmation.SelectedToolAdapter
import com.miassolutions.rentatool.ui.fragments.toolselection.SelectedTool
import com.miassolutions.rentatool.utils.Constants
import com.miassolutions.rentatool.utils.extenstions.collectingFlow
import com.miassolutions.rentatool.utils.extenstions.showSnackbar
import dagger.hilt.android.AndroidEntryPoint
import java.time.LocalDate

@AndroidEntryPoint
class RentalConfirmationFragment : Fragment(R.layout.fragment_confirm_renting_tools) {

    private var _binding: FragmentConfirmRentingToolsBinding? = null
    private val binding get() = _binding!!

    private val args by navArgs<RentalConfirmationFragmentArgs>()
    private lateinit var adapter: SelectedToolAdapter
    private val viewModel by viewModels<ConfirmationViewModel>()

    private val gson = Gson()
    private lateinit var selectedTools: List<SelectedTool>

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentConfirmRentingToolsBinding.bind(view)


        val estReturnDate = LocalDate.parse(args.estReturnDate)

        viewModel.initialize(args.selectedToolsJson, estReturnDate)

        val selectedToolsType = object : TypeToken<List<SelectedTool>>() {}.type
        selectedTools = gson.fromJson(args.selectedToolsJson, selectedToolsType)

        setupRecyclerView()
        observeUiState()
        observeUiEvent()
        setupClickListeners()

        selectedTools.forEach {
            Log.d(
                Constants.TAG,
                "${it.toolId} - ${it.toolName} - ${it.quantity} - ${args.estReturnDate}"
            )

        }


    }

    private fun setupClickListeners() {
        binding.btnConfirmRental.setOnClickListener {
            viewModel.confirmRental(args.customerId)
        }
    }

    private fun observeUiEvent() {
        collectingFlow {
            viewModel.uiEvent.collect { event ->
                when (event) {
                    is ConfirmationUiEvent.ShowToast -> binding.root.showSnackbar(event.message)
                    is ConfirmationUiEvent.NavigateBack -> findNavController().popBackStack()
                }
            }
        }
    }

    private fun observeUiState() {
        collectingFlow {
            viewModel.uiState.collect { state ->
                adapter.submitList(state.selectedTools)
                binding.apply {
                    tvTotalAmount.text = "Total : Rs. ${state.totalAmount}"
                    tvSelectedDate.text = "${state.days} days"


                }
            }
        }
    }

    private fun setupRecyclerView() {
        adapter = SelectedToolAdapter()
        binding.rvSelectedTools.adapter = adapter
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}