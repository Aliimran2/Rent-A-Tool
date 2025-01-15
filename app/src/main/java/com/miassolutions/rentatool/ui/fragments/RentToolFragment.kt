package com.miassolutions.rentatool.ui.fragments

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.miassolutions.rentatool.MyApplication
import com.miassolutions.rentatool.R
import com.miassolutions.rentatool.core.utils.extenstions.showConfirmDialog
import com.miassolutions.rentatool.core.utils.extenstions.showCustomerSelectionBottomSheet
import com.miassolutions.rentatool.core.utils.extenstions.showDatePicker
import com.miassolutions.rentatool.core.utils.extenstions.showToast
import com.miassolutions.rentatool.data.model.Tool
import com.miassolutions.rentatool.databinding.BottomSheetToolsBinding
import com.miassolutions.rentatool.databinding.FragmentRentToolBinding
import com.miassolutions.rentatool.ui.adapters.SelectedToolListAdapter
import com.miassolutions.rentatool.ui.adapters.ToolSelectionListAdapter
import com.miassolutions.rentatool.ui.viewmodels.SharedViewModel
import com.miassolutions.rentatool.ui.viewmodels.SharedViewModelFactory


class RentToolFragment : Fragment(R.layout.fragment_rent_tool) {

    companion object {
        private const val TAG = "RentToolFragment" // review
    }

    private var _binding: FragmentRentToolBinding? = null
    private val binding get() = _binding!!

//    private val rentalViewModel: RentalViewModel by activityViewModels()
private val rentalViewModel: SharedViewModel by activityViewModels {
    SharedViewModelFactory((requireActivity().application as MyApplication).repository)
}
    private val selectedTools = mutableListOf<Pair<Long, Int>>()
    private lateinit var toolSelectionAdapter: ToolSelectionListAdapter
    private lateinit var selectedToolListAdapter: SelectedToolListAdapter
    private  var selectedCustomer : Long = 0L
    private var estimatedDate = 0L

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentRentToolBinding.bind(view)

        selectedToolListAdapter = SelectedToolListAdapter()


        setupUI()
        observeViewModel()


    }

    private fun setupUI() {

        binding.etCustomerName.setOnClickListener { showCustomerSelection() }
        binding.etEstimatedDate.setOnClickListener { showDatePickerDialog() }
        binding.btnSubmit.setOnClickListener {
            if (validateInputs() && validateStock()){
                showConfirmDialog(
                    "Save Transaction", "Are you sure?",
                    onConfirm = {
                        navigateToNextFragment()
                        updateDatabase()

                    },
                    onCancel = { showToast("Action Cancelled") }
                )
            }


        }
    }

    private fun validateInputs(): Boolean {

        return when {
            binding.etCustomerName.text.isNullOrEmpty() -> {
                showToast(getString(R.string.please_select_a_customer))
               false
            }
            binding.etEstimatedDate.text.isNullOrEmpty() -> {
                showToast(getString(R.string.please_select_estimated_return_date))
                false
            }
            selectedTools.isEmpty() -> {
                showToast(getString(R.string.please_select_some_tools))
                false
            }

            else -> true
        }

    }

    private fun validateStock() : Boolean {
       //if selected tools are out of stock return false
        val toolsInStock = rentalViewModel.allTools.value?: return false
        for ((toolId, quantityRequested) in selectedTools){
            val tool = toolsInStock.find { it.toolId == toolId }
            if (tool == null || tool.availableStock < quantityRequested ){
                showToast("Tool '${tool?.name ?: "Unknown"} is out of stock or insufficient quantity")
                return false
            }
        }
        return true
    }

    private fun showDatePickerDialog() {
        showDatePicker("Estimated Date") { date, dateInMillis ->
            binding.etEstimatedDate.setText(date)
            rentalViewModel.setEstimatedReturnDate(dateInMillis)
            estimatedDate = dateInMillis
        }
    }

    private fun updateDatabase() {
        rentalViewModel.addRental(
            customerId = selectedCustomer,
            toolRentals = selectedTools,
            rentalDate = System.currentTimeMillis()
        )
    }


    private fun observeViewModel() {

        rentalViewModel.customer.observe(viewLifecycleOwner) {
            it?.let {
                selectedCustomer = it.customerId
            }
        }

        rentalViewModel.allTools.observe(viewLifecycleOwner) { tools ->

            binding.btnToolSelection.setOnClickListener {
                showToolSelectionBottomSheet(tools) { selected ->
                    selectedTools.clear()
                    selectedTools.addAll(selected)
                    updatedToolsDetailsList()
                }
            }

        }
        rentalViewModel.allTools.observe(viewLifecycleOwner) { tools ->
            selectedToolListAdapter.updateToolsList(tools)
        }

        rentalViewModel.selectedTools.observe(viewLifecycleOwner) { selectedTools ->
            selectedToolListAdapter.submitList(selectedTools)
        }

        rentalViewModel.setSelectedTools(selectedTools)

    }

    private fun navigateToNextFragment() {

        findNavController().navigate(R.id.action_rentToolFragment_to_confirmDialogFragment)
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
        rentalViewModel.allCustomers.observe(viewLifecycleOwner) { customers ->

            if (!customers.isNullOrEmpty()) {
                showCustomerSelectionBottomSheet(customers) { customer ->
                    binding.etCustomerName.text?.clear()
                    rentalViewModel.setCustomer(customer)
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