package com.miassolutions.rentatool.ui.fragments.newfragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.miassolutions.rentatool.MyApplication
import com.miassolutions.rentatool.data.model.Tool
import com.miassolutions.rentatool.databinding.BottomSheetToolsBinding
import com.miassolutions.rentatool.ui.newadapters.ToolSelectionAdapter
import com.miassolutions.rentatool.ui.viewmodels.SharedViewModel
import com.miassolutions.rentatool.ui.viewmodels.SharedViewModelFactory

class ToolBottomSheet : BottomSheetDialogFragment() {

    private val rentalViewModel: SharedViewModel by activityViewModels {
        SharedViewModelFactory((requireActivity().application as MyApplication).repository)
    }

    private lateinit var toolSelectionAdapter: ToolSelectionAdapter

    private var _binding: BottomSheetToolsBinding? = null
    private val binding get() = _binding!!


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = BottomSheetToolsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        toolSelectionAdapter = ToolSelectionAdapter { selectedTools ->

            rentalViewModel.updateSelectedTools(selectedTools)

        }

        binding.rvBottomSheet.adapter = toolSelectionAdapter

        rentalViewModel.allTools.observe(viewLifecycleOwner) {
            toolSelectionAdapter.submitList(it)
        }

        binding.btnConfirmation.setOnClickListener {

            dismiss()
        }


    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}