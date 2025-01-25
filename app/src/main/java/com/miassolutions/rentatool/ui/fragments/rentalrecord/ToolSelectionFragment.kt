package com.miassolutions.rentatool.ui.fragments.rentalrecord

import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import androidx.core.view.MenuProvider
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.miassolutions.rentatool.R
import com.miassolutions.rentatool.core.utils.extenstions.showDatePicker
import com.miassolutions.rentatool.core.utils.extenstions.showToast
import com.miassolutions.rentatool.databinding.FragmentToolSelectionBinding
import com.miassolutions.rentatool.myapplication.MyApplication
import com.miassolutions.rentatool.ui.adapters.ToolSelectionListAdapter
import com.miassolutions.rentatool.ui.viewmodels.SharedViewModel
import com.miassolutions.rentatool.ui.viewmodels.SharedViewModelFactory

class ToolSelectionFragment : Fragment(R.layout.fragment_tool_selection) {
    private var _binding: FragmentToolSelectionBinding? = null
    private val binding get() = _binding!!

    private var selectedEstimatedDate: Long = 0L
    private lateinit var toolSelectionListAdapter: ToolSelectionListAdapter

    private val rentalViewModel: SharedViewModel by activityViewModels {
        SharedViewModelFactory((requireActivity().application as MyApplication).repository)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentToolSelectionBinding.bind(view)

        requireActivity().addMenuProvider(object : MenuProvider{
            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                menuInflater.inflate(R.menu.done_menu, menu)
            }

            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                return when(menuItem.itemId){
                    R.id.done_menu -> {
                        showToast("Tools selected")
                        true
                    }
                    else -> false
                }
            }
        }, viewLifecycleOwner)


        toolSelectionListAdapter = ToolSelectionListAdapter {  selectedTools ->
            rentalViewModel.updatedSelectedTools(selectedTools)
        }

        rentalViewModel.allTools.observe(viewLifecycleOwner) {tools ->
            toolSelectionListAdapter.submitList(tools)
        }

        binding.rvBottomSheet.adapter = toolSelectionListAdapter

        binding.etEstimatedDate.setOnClickListener {
            showDatePicker("Estimated Returned Date") { dateInString, dateInLong ->
                binding.etEstimatedDate.setText(dateInString)
                selectedEstimatedDate = dateInLong
            }
        }


    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}