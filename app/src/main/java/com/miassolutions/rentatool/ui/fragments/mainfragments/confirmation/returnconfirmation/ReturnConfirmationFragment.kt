package com.miassolutions.rentatool.ui.fragments.mainfragments.confirmation.returnconfirmation

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import com.miassolutions.rentatool.R
import com.miassolutions.rentatool.databinding.FragmentConfirmRentingToolsBinding
import com.miassolutions.rentatool.databinding.FragmentReturnConfirmationBinding
import com.miassolutions.rentatool.ui.fragments.mainfragments.returntool.ReturnToolItem
import com.miassolutions.rentatool.ui.fragments.mainfragments.returntool.ReturnToolsAdapter
import com.miassolutions.rentatool.utils.Constants
import com.miassolutions.rentatool.utils.extenstions.collectingFlow
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect

@AndroidEntryPoint
class ReturnConfirmationFragment : Fragment(R.layout.fragment_return_confirmation) {

    private var _binding: FragmentReturnConfirmationBinding? = null
    private val binding get() = _binding!!

    private val args by navArgs<ReturnConfirmationFragmentArgs>()
    private val viewModel by viewModels<ReturnConfirmViewModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentReturnConfirmationBinding.bind(view)


        val list: List<ReturnToolItem> = args.selectedItemsWrapper.selectedItems
        viewModel.loadReturnToolList(list)


        val adapter = ReturnConfirmationListAdapter()


        binding.rvReturnTools.adapter = adapter

        collectingFlow {
            viewModel.uiState.collect {
                adapter.submitList(it.tools)
            }
        }


    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}