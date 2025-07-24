package com.miassolutions.rentatool.ui.fragments.toolselection

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import com.google.android.material.datepicker.MaterialDatePicker
import com.miassolutions.rentatool.R
import com.miassolutions.rentatool.data.converter.LocalDateConverter
import com.miassolutions.rentatool.data.entities.ToolEntity
import com.miassolutions.rentatool.databinding.FragmentToolsSelectionBinding
import com.miassolutions.rentatool.ui.adapters.ToolSelectionListAdapter
import com.miassolutions.rentatool.utils.extenstions.collectingFlow
import com.miassolutions.rentatool.utils.extenstions.formattedDate
import com.miassolutions.rentatool.utils.extenstions.showDatePicker
import com.miassolutions.rentatool.utils.extenstions.toFormattedDate
import dagger.hilt.android.AndroidEntryPoint
import java.time.*
import java.time.format.DateTimeFormatter

@AndroidEntryPoint
class ToolSelectionFragment : Fragment(R.layout.fragment_tools_selection) {

    private var _binding: FragmentToolsSelectionBinding? = null
    private val binding get() = _binding!!

    private val viewModel by viewModels<ToolSelectionViewModel>()
    private lateinit var adapter: ToolSelectionListAdapter

    private val args by navArgs<ToolSelectionFragmentArgs>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentToolsSelectionBinding.bind(view)



        setupUiState()
        setupRecyclerview()
        setupListener()


    }


    private fun setupListener() {

//        binding.searchInput.doAfterTextChanged { viewModel.onSearchQueryChanged(it.toString()) }
//
        binding.etEstimatedDate.setOnClickListener {
            showDatePicker("Select Estimated Return Date"){localDate ->

                binding.etEstimatedDate.setText(localDate.toFormattedDate())
            }
        }

    }



    private fun setupRecyclerview() {
        adapter = ToolSelectionListAdapter(object : ToolSelectionListAdapter.ToolSelectionListener {
            override fun onToolSelectionChanged(
                tool: ToolEntity,
                quantity: Int,
                isChecked: Boolean
            ) {
//                viewModel.toggleToolSelection(tool, quantity, isChecked)
            }
        })

        binding.rvToolsSelection.adapter = adapter
    }

    private fun setupUiState() {
        collectingFlow {
            viewModel.uiState.collect { state ->
//                Log.d("MiasSolutionTag", state.tools.toString())
//                adapter.submitList(state.tools)

            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}