package com.miassolutions.rentatool.ui.fragments.toolselection

import android.os.Bundle
import android.view.View
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.miassolutions.rentatool.R
import com.miassolutions.rentatool.databinding.FragmentToolsSelectionBinding
import com.miassolutions.rentatool.utils.extenstions.collectingFlow
import com.miassolutions.rentatool.utils.extenstions.showDatePicker
import com.miassolutions.rentatool.utils.extenstions.showSnackbar
import com.miassolutions.rentatool.utils.extenstions.showToast
import com.miassolutions.rentatool.utils.extenstions.toFormattedDate
import dagger.hilt.android.AndroidEntryPoint
import java.time.LocalDate

@AndroidEntryPoint
class ToolSelectionFragment : Fragment(R.layout.fragment_tools_selection) {

    private var _binding: FragmentToolsSelectionBinding? = null
    private val binding get() = _binding!!

    private val viewModel by viewModels<ToolSelectionViewModel>()
    private lateinit var adapter: ToolListAdapterForRenting

    private val args by navArgs<ToolSelectionFragmentArgs>()
    private var customerId: Long = -1L
    private var customerName: String = ""


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentToolsSelectionBinding.bind(view)

        customerId = args.customerId
        customerName = args.customerName

        setupUiEvent()
        setupRecyclerview()
        setupListener()
        setupUiState()

    }

    private fun setupUiEvent() {
        collectingFlow {
            viewModel.uiEvent.collect { event ->
                when (event) {

                    is ToolSelectionUiEvent.NavigateToConfirmation -> {
                        val action = ToolSelectionFragmentDirections
                            .actionToolSelectionFragmentToRentalConfirmationFragment(
                                customerId = event.customerId,
                                customerName = event.customerName,
                                selectedToolsJson = event.selectedToolsJson,
                                estReturnDate = event.estReturnDate
                            )
                        findNavController().navigate(action)
                    }

                    is ToolSelectionUiEvent.ShowDatePicker -> {
                        showDatePicker("Select Estimated Return Date") {
                            viewModel.onDateSelected(it)
                            binding.etEstimatedDate.setText(it.toFormattedDate())
                        }
                    }

                    is ToolSelectionUiEvent.ShowToast -> {
                        binding.root.showSnackbar(event.message)
                    }
                }
            }
        }
    }



    private fun setupListener() {
        with(binding) {
            submitBtn.setOnClickListener {
                viewModel.onSubmitClicked()
            }

            etEstimatedDate.setOnClickListener {
                viewModel.onDateClicked()

            }

            searchInput.doAfterTextChanged {
                viewModel.onSearchChanged(it.toString())
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