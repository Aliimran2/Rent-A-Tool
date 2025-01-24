package com.miassolutions.rentatool

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.miassolutions.rentatool.data.model.Tool
import com.miassolutions.rentatool.databinding.FragmentRentedBinding
import com.miassolutions.rentatool.myapplication.MyApplication
import com.miassolutions.rentatool.ui.fragments.ToolBottomSheet
import com.miassolutions.rentatool.ui.newadapters.RentedToolsAdapter
import com.miassolutions.rentatool.ui.viewmodels.SharedViewModel
import com.miassolutions.rentatool.ui.viewmodels.SharedViewModelFactory


class RentedFragment : Fragment(R.layout.fragment_rented) {

    private var _binding: FragmentRentedBinding? = null
    private val binding get() = _binding!!

    private lateinit var toolBottomSheet: ToolBottomSheet

    private val rentalViewModel: SharedViewModel by activityViewModels {
        SharedViewModelFactory((requireActivity().application as MyApplication).repository)
    }
    private lateinit var rentedToolsAdapter: RentedToolsAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentRentedBinding.bind(view)


        rentalViewModel.selectedTools.observe(viewLifecycleOwner){
            handleSelectedTools(it)
        }


        binding.floatingActionButton.setOnClickListener {
            showBottomSheet()
        }


    }

    private fun handleSelectedTools(selectedTools: List<Tool>) {
        rentedToolsAdapter = RentedToolsAdapter()
        rentedToolsAdapter.submitList(selectedTools)

        binding.rvRentedTools.adapter = rentedToolsAdapter

    }

    private fun showBottomSheet() {
        val toolBottomSheet = ToolBottomSheet()
        toolBottomSheet.show(parentFragmentManager, toolBottomSheet.tag)

    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}