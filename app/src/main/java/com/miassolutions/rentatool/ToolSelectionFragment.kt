package com.miassolutions.rentatool

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.miassolutions.rentatool.core.utils.extenstions.showDatePicker
import com.miassolutions.rentatool.databinding.FragmentToolSelectionBinding
import com.miassolutions.rentatool.ui.fragments.newfragments.ToolBottomSheet
import com.miassolutions.rentatool.ui.newadapters.RentedToolsAdapter
import com.miassolutions.rentatool.ui.newadapters.ToolSelectionAdapter
import com.miassolutions.rentatool.ui.viewmodels.SharedViewModel
import com.miassolutions.rentatool.ui.viewmodels.SharedViewModelFactory

class ToolSelectionFragment : Fragment(R.layout.fragment_tool_selection) {
    private var _binding: FragmentToolSelectionBinding? = null
    private val binding get() = _binding!!

    private var selectedEstimatedDate: Long = 0L
    private lateinit var toolBottomSheet: ToolBottomSheet
    private lateinit var toolSelectionAdapter: ToolSelectionAdapter

    private val rentalViewModel: SharedViewModel by activityViewModels {
        SharedViewModelFactory((requireActivity().application as MyApplication).repository)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentToolSelectionBinding.bind(view)


        toolSelectionAdapter = ToolSelectionAdapter { selectedTools ->
            rentalViewModel.updateSelectedTools(selectedTools)
        }

        binding.rvBottomSheet.adapter = toolSelectionAdapter

        binding.etEstimatedDate.setOnClickListener {
            showDatePicker("Estimated Returned Date") { dateInString, dateInLong ->
                binding.etEstimatedDate.setText(dateInString)
                selectedEstimatedDate = dateInLong
            }
        }

        rentalViewModel.selectedTools.observe(viewLifecycleOwner) {
            toolSelectionAdapter.submitList(it)
        }

        binding.btnConfirmation.setOnClickListener {

        }




    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}