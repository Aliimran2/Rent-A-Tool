package com.miassolutions.rentatool.ui.fragments.toolselection

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import com.miassolutions.rentatool.R
import com.miassolutions.rentatool.databinding.FragmentToolsSelectionBinding
import com.miassolutions.rentatool.utils.extenstions.collectingFlow
import com.miassolutions.rentatool.utils.extenstions.showDatePicker
import com.miassolutions.rentatool.utils.extenstions.showToast
import com.miassolutions.rentatool.utils.extenstions.toFormattedDate
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ToolSelectionFragment : Fragment(R.layout.fragment_tools_selection) {

    private var _binding: FragmentToolsSelectionBinding? = null
    private val binding get() = _binding!!

    private val viewModel by viewModels<ToolSelectionViewModel>()
    private lateinit var adapter: ToolListAdapterForRenting

    private val args by navArgs<ToolSelectionFragmentArgs>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentToolsSelectionBinding.bind(view)


        setupUiEvent()
        setupRecyclerview()
        setupListener()
        setupUiState()

    }

    private fun setupUiEvent() {
        collectingFlow {
            viewModel.uiEvent.collect{event ->
                when(event){
                    ToolSelectionUiEvent.NavigateToConfirmation -> {
                        val action = ToolSelectionFragmentDirections.actionToolSelectionFragmentToRentalConfirmationFragment()

                    }
                    is ToolSelectionUiEvent.ShowDatePicker -> {

                    }
                    is ToolSelectionUiEvent.ShowToast -> {
                        showToast(event.message)
                    }
                }

            }
        }


    }


    private fun setupListener() {

        binding.etEstimatedDate.setOnClickListener {
            showDatePicker("Select Estimated Return Date") { localDate ->

                binding.etEstimatedDate.setText(localDate.toFormattedDate())
            }
        }

    }


    private fun setupRecyclerview() {
         adapter = ToolListAdapterForRenting(
            onToolChecked = { toolId, isChecked, quantity ->
                if (isChecked) viewModel.onToolChecked(toolId, true, quantity)
                else viewModel.onToolChecked(toolId, false)
            },
            onQuantityChanged = { toolId, quantity ->
                viewModel.onQuantityChanged(toolId, quantity)
            }
        )

        binding.rvToolsSelection.adapter = adapter
    }

    private fun setupUiState() {
        collectingFlow {
            viewModel.uiState.collect { state ->
                adapter.submitList(state.tools)

            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}