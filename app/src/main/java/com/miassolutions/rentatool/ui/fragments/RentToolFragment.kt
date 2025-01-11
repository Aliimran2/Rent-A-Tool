package com.miassolutions.rentatool.ui.fragments

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.SearchView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.datepicker.MaterialDatePicker
import com.miassolutions.rentatool.R
import com.miassolutions.rentatool.data.model.Tool
import com.miassolutions.rentatool.databinding.BottomSheetToolsBinding
import com.miassolutions.rentatool.databinding.FragmentRentToolBinding
import com.miassolutions.rentatool.ui.adapters.BottomToolAdapter
import com.miassolutions.rentatool.ui.adapters.ToolSelectionListAdapter
import com.miassolutions.rentatool.ui.viewmodels.RentalViewModel
import com.miassolutions.rentatool.utils.helper.showToast
import com.miassolutions.rentatool.utils.mockdb.DataProvider
import java.text.SimpleDateFormat
import java.util.Locale

const val TAG = "RENT_TOOL_FRAGMENT"

class RentToolFragment : Fragment(R.layout.fragment_rent_tool) {

    private var _binding: FragmentRentToolBinding? = null
    private val binding get() = _binding!!

    private val rentalViewModel: RentalViewModel by activityViewModels()
    private val selectedTools =
        mutableListOf<Pair<Long, Int>>() //access selected tools by id and with selected quantities
    private lateinit var adapter: ToolSelectionListAdapter
    private var estimatedDate = 0L

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentRentToolBinding.bind(view)



        setupUI()
        observeViewModel()


    }

    private fun setupUI() {


        // set estimated date
        binding.etEstimatedDate.setOnClickListener {
            showDatePickerDialog { date, dateInMillis ->
                binding.etEstimatedDate.setText(date)
                estimatedDate = dateInMillis
            }
        }
    }

    private fun observeViewModel() {

        rentalViewModel.tools.observe(viewLifecycleOwner) { tools ->
            binding.btnToolSelection.setOnClickListener {
                showToolSelectionBottomSheet(tools){ selected ->
                    selectedTools.clear()
                    selectedTools.addAll(selected)
                    updatedToolsDetailsList()
                }
            }

        }

        rentalViewModel.customers.observe(viewLifecycleOwner) { customers ->

        }

    }

    private fun updatedToolsDetailsList() {
    }


    private fun showDatePickerDialog(onDateSelected: (String, Long) -> Unit) {
        val datePicker = MaterialDatePicker.Builder.datePicker()
            .setTitleText("Select Estimated Return Date")
            .build()
        datePicker.addOnPositiveButtonClickListener { selectedDate ->
            val dateInString =
                SimpleDateFormat("dd MMM yyyy", Locale.getDefault()).format(selectedDate)
            onDateSelected(dateInString, selectedDate)
        }
        datePicker.show(parentFragmentManager, "DatePicker")

    }

    private fun showToolSelectionBottomSheet(
        tools: List<Tool>,
        onSelectedTools: (List<Pair<Long, Int>>) -> Unit
    ) {
        //inflate the btm sheet
        val dialog = BottomSheetDialog(requireContext())
        val bottomSheetToolsBinding = BottomSheetToolsBinding.inflate(layoutInflater)
        dialog.setContentView(bottomSheetToolsBinding.root)

        //adapter setup
        val tempSelectedTools = mutableMapOf<Long, Int>()
        adapter = ToolSelectionListAdapter(tools) { selected ->
            onSelectedTools(selected.map { it.key to it.value })

        }

        bottomSheetToolsBinding.rvBottomSheet.adapter = adapter

        //search view setup
        bottomSheetToolsBinding.searchView.setOnQueryTextListener(object :
            SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean = false
            override fun onQueryTextChange(newText: String?): Boolean {
                newText?.let { adapter.filter(it) }
                return true
            }
        })

        // button setup
        bottomSheetToolsBinding.btnConfirmation.setOnClickListener {
            onSelectedTools(tempSelectedTools.map { it.key to it.value })
            tempSelectedTools.forEach { (tool, quantity) ->
                val toolId = tool
                val selectedQuantity = quantity
                Log.d(TAG, "$toolId - $selectedQuantity")
            }
            dialog.dismiss()
        }
        dialog.show()

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}