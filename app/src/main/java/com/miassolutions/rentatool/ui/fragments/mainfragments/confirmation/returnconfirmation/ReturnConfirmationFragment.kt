package com.miassolutions.rentatool.ui.fragments.mainfragments.confirmation.returnconfirmation

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.miassolutions.rentatool.R
import com.miassolutions.rentatool.data.entities.ReturnedToolEntity
import com.miassolutions.rentatool.databinding.FragmentConfirmRentingToolsBinding
import com.miassolutions.rentatool.databinding.FragmentReturnConfirmationBinding
import com.miassolutions.rentatool.ui.fragments.mainfragments.returntool.ReturnToolItem
import com.miassolutions.rentatool.ui.fragments.mainfragments.returntool.ReturnToolsAdapter
import com.miassolutions.rentatool.utils.Constants
import com.miassolutions.rentatool.utils.extenstions.collectingFlow
import com.miassolutions.rentatool.utils.extenstions.showToast
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import java.time.LocalDate

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

        Log.d(Constants.TAG, args.orderId.toString())


        binding.rvReturnTools.adapter = adapter

        collectingFlow {
            viewModel.uiState.collect {
                adapter.submitList(it.tools)
            }
        }

        binding.btnConfirmReturn.setOnClickListener {
            val returnsList = args.selectedItemsWrapper.selectedItems
                .filter { it.isSelected && it.returnQuantity > 0 }
                .map {
                    Log.d(Constants.TAG, "Returning rentedToolId=${it.rentedToolId}")
                    ReturnedToolEntity(
                        id = 0L,
                        rentedToolId = it.rentedToolId,
                        returnedQuantity = it.returnQuantity,
                        returnDate = LocalDate.now()
                    )
                }

            if (returnsList.isNotEmpty()){
                viewModel.performReturnTransaction(args.orderId, returnsList)
                showToast("Returns Successfully")
                findNavController().navigateUp()
            }

        }


    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}