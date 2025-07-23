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
import com.miassolutions.rentatool.data.entities.ToolEntity
import com.miassolutions.rentatool.databinding.FragmentToolsSelectionBinding
import com.miassolutions.rentatool.ui.adapters.ToolSelectionListAdapter
import com.miassolutions.rentatool.utils.extenstions.collectingFlow
import dagger.hilt.android.AndroidEntryPoint
import java.time.*

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
//        binding.etEstimatedDate.setOnClickListener {
//            datePicker {
//                viewModel.onEstimatedReturnSelected(it)
//            }
//        }

    }

    private fun datePicker(onDateSelection : (LocalDate) -> Unit) {



        val datePicker = MaterialDatePicker.Builder.datePicker()
            .setTitleText("Set Promise Date")
            .setSelection(MaterialDatePicker.todayInUtcMilliseconds())
            .build()

        datePicker.addOnPositiveButtonClickListener { dateLong: Long ->
            val instant = Instant.ofEpochMilli(dateLong)
            val selectedDate = instant.atZone(ZoneId.systemDefault()).toLocalDate()
            binding.etEstimatedDate.setText(selectedDate.toString())
            onDateSelection(selectedDate)
        }
        datePicker.show(parentFragmentManager, null)


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