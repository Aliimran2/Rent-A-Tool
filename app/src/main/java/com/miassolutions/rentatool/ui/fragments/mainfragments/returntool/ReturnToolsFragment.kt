package com.miassolutions.rentatool.ui.fragments.mainfragments.returntool

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.miassolutions.rentatool.R
import com.miassolutions.rentatool.databinding.FragmentReturnToolsBinding
import com.miassolutions.rentatool.utils.extenstions.collectingFlow
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

    adapter = ReturnToolsAdapter(
        onReturnQuantityChanged = { id, qty ->
//            viewModel.onReturnQuantityChanged(id, qty)
                                  }
        ,
        onCheckboxChanged = { id, check ->
//            viewModel.onCheckboxChanged(id, check)
        }
    )

        binding.rvToolsToReturn.adapter = adapter

        viewModel.loadCustomerAndTools(7L)


        collectingFlow {
            viewModel.uiState.collect { state ->
                binding.tvTotalRent.text = "Total Rent: Rs. ${state.totalRent}"
                adapter.submitList(state.tools)
            }
        }

// Observe events
        collectingFlow {
            viewModel.event.collect { event ->
                when (event) {
                    is ReturnToolsUiEvent.ShowMessage -> showToast(event.message)
                    is ReturnToolsUiEvent.ReturnCompleted -> findNavController().popBackStack()
                }
            }
        }






    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}