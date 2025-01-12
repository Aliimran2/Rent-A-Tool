package com.miassolutions.rentatool.ui.fragments

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.datepicker.MaterialDatePicker
import com.miassolutions.rentatool.R
import com.miassolutions.rentatool.data.model.Customer
import com.miassolutions.rentatool.data.model.Tool
import com.miassolutions.rentatool.databinding.BottomSheetCustomersBinding
import com.miassolutions.rentatool.databinding.BottomSheetToolsBinding
import com.miassolutions.rentatool.databinding.FragmentRentToolBinding
import com.miassolutions.rentatool.ui.adapters.CustomerSelectionListAdapter
import com.miassolutions.rentatool.ui.adapters.SelectedToolListAdapter
import com.miassolutions.rentatool.ui.adapters.ToolSelectionListAdapter
import com.miassolutions.rentatool.ui.viewmodels.RentalViewModel
import com.miassolutions.rentatool.utils.extenstions.showCustomerSelectionBottomSheet
import com.miassolutions.rentatool.utils.extenstions.showDatePicker
import com.miassolutions.rentatool.utils.helper.showToast
import java.text.SimpleDateFormat
import java.util.Locale


class RentToolFragment : Fragment(R.layout.fragment_rent_tool) {

    private val TAG = "RENT_TOOL_FRAGMENT"
    private var _binding: FragmentRentToolBinding? = null
    private val binding get() = _binding!!

    private val rentalViewModel: RentalViewModel by activityViewModels()
    private val selectedTools =
        mutableListOf<Pair<Long, Int>>() //access selected tools by id and with selected quantities
    private lateinit var toolSelectionAdapter: ToolSelectionListAdapter
    private lateinit var selectedToolListAdapter: SelectedToolListAdapter
    private var estimatedDate = 0L

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentRentToolBinding.bind(view)



        setupUI()
        observeViewModel()


    }

    private fun setupUI() {

        binding.etCustomerName.setOnClickListener { showCustomerSelection() }
        binding.etEstimatedDate.setOnClickListener { showDatePickerDialog() }
    }

    private fun showDatePickerDialog() {
        showDatePicker("Estimated Date") { date, dateInMillis ->
            binding.etEstimatedDate.setText(date)
            estimatedDate = dateInMillis
        }
    }



    private fun observeViewModel() {

        rentalViewModel.tools.observe(viewLifecycleOwner) { tools ->
            selectedToolListAdapter = SelectedToolListAdapter(tools)
            binding.btnToolSelection.setOnClickListener {
                showToolSelectionBottomSheet(tools) { selected ->
                    selectedTools.clear()
                    selectedTools.addAll(selected)
                    updatedToolsDetailsList()
                }
            }

        }
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
        toolSelectionAdapter = ToolSelectionListAdapter(tools) { selected ->
            tempSelectedTools.clear()
            tempSelectedTools.putAll(selected)

        }

        bottomSheetToolsBinding.rvBottomSheet.adapter = toolSelectionAdapter

        //search view setup
        bottomSheetToolsBinding.searchView.setOnQueryTextListener(object :
            SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean = false
            override fun onQueryTextChange(newText: String?): Boolean {
                newText?.let { toolSelectionAdapter.filter(it) }
                return true
            }
        })

        // button setup
        bottomSheetToolsBinding.btnConfirmation.setOnClickListener {
            onSelectedTools(tempSelectedTools.map { it.key to it.value })

            dialog.dismiss()
        }
        dialog.show()

    }

    private fun updatedToolsDetailsList() {

        selectedToolListAdapter.submitList(selectedTools)
        binding.rvSelectedToolsList.adapter = selectedToolListAdapter
    }

    private fun showCustomerSelection() {
        rentalViewModel.customers.observe(viewLifecycleOwner) { customers ->
            Log.d(TAG, "$customers")
            if (!customers.isNullOrEmpty()) {
                showCustomerSelectionBottomSheet(customers) { customer ->
                    binding.etCustomerName.text?.clear()
                    binding.etCustomerName.setText(customer.customerName)
                }
            } else {
                Log.d(TAG, "no customer found")
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}