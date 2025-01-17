package com.miassolutions.rentatool

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.miassolutions.rentatool.core.utils.extenstions.showBottomSheetDialog
import com.miassolutions.rentatool.core.utils.extenstions.showToolSelectionBottomSheet
import com.miassolutions.rentatool.databinding.BottomSheetToolsBinding
import com.miassolutions.rentatool.databinding.FragmentRentedBinding
import com.miassolutions.rentatool.ui.newadapters.RentedToolsAdapter
import com.miassolutions.rentatool.ui.viewmodels.SharedViewModel
import com.miassolutions.rentatool.ui.viewmodels.SharedViewModelFactory


class RentedFragment : Fragment(R.layout.fragment_rented) {

    private var _binding: FragmentRentedBinding? = null
    private val binding get() = _binding!!
    private val rentalViewModel: SharedViewModel by activityViewModels {
        SharedViewModelFactory((requireActivity().application as MyApplication).repository)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentRentedBinding.bind(view)

        val adapter = RentedToolsAdapter()

        rentalViewModel.allTools.observe(viewLifecycleOwner){
            adapter.submitList(it)
        }

        binding.rvRentedTools.adapter
        binding.floatingActionButton.setOnClickListener {
            showToolSelection()
        }


    }

    private fun showToolSelection() {
        val btmBinding = BottomSheetToolsBinding.inflate(layoutInflater)
       showBottomSheetDialog(btmBinding.root)

    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}