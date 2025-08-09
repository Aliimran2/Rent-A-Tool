package com.miassolutions.rentatool.ui.fragments.mainfragments.returntool

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.miassolutions.rentatool.R
import com.miassolutions.rentatool.databinding.FragmentReturnToolsBinding
import com.miassolutions.rentatool.utils.Constants
import com.miassolutions.rentatool.utils.extenstions.collectingFlow
import com.miassolutions.rentatool.utils.extenstions.showConfirmDialog
import com.miassolutions.rentatool.utils.extenstions.showToast
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ReturnToolsFragment : Fragment(R.layout.fragment_return_tools) {

    private var _binding: FragmentReturnToolsBinding? = null
    private val binding get() = _binding!!

    private val args by navArgs<ReturnToolsFragmentArgs>()

    private val viewModel by viewModels<ReturnToolsViewModel>()
    private lateinit var adapter: ReturnToolsAdapter


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentReturnToolsBinding.bind(view)

        adapter = ReturnToolsAdapter { id, isChecked, qty ->
            viewModel.updateSelection(id, isChecked, qty)
        }

        binding.rvToolsToReturn.adapter = adapter

        viewModel.loadRentedTools(args.orderId)

        binding.btnConfirmReturn.setOnClickListener {
            viewModel.onReturnButtonClick()

        }



        collectingFlow {
            viewModel.uiState.collect { state ->
                adapter.submitList(state.tools)
            }


        }

// Observe events
        collectingFlow {
            viewModel.event.collect { event ->
                when (event) {
                    is ReturnToolsUiEvent.ShowMessage -> showToast(event.message)
                    is ReturnToolsUiEvent.ReturnCompleted -> findNavController().popBackStack()
                    is ReturnToolsUiEvent.NavToReturnConfirm -> {
                        val returnToolsList = viewModel.getSelectedItems()
                        val action =
                            ReturnToolsFragmentDirections.actionReturnToolsFragmentToReturnConfirmationFragment(
                                ReturnToolListWrapper(returnToolsList)
                            )
                        findNavController().navigate(action)
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