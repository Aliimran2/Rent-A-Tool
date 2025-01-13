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
            if (validateInputs()){
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

        rentalViewModel.rentalDetails.observe(viewLifecycleOwner){
            it?.let {
                Log.d(TAG, "Rental Details: ${it.joinToString("\n") { d ->
                    "Rental ID: ${d.rentalDetailId}\nTool ID: ${d.toolId}\nTool Quantity: ${d.quantity}\nRental date: ${d.rentalDate}\nrentPerDay: ${d.rentPerDay}\n"  }}")
            }?: run {
                Log.d(TAG, "No details")
            }
        }

        rentalViewModel.rentals.observe(viewLifecycleOwner){
            it?.let {
                Log.d(TAG, "Rental Details: ${it.joinToString("\n") { d ->
                    "Rental ID: ${d.rentalId}\nRentDate: ${d.rentalDate}\nReturn Date: ${d.returnDate}\nCustomer ID: ${d.customerId}\n\n"  }}")
            }?: run {
                Log.d(TAG, "No details")
            }
        }

        rentalViewModel.customer.observe(viewLifecycleOwner) {
            it?.let {
                selectedCustomer = it.customerId
            }
        }

        rentalViewModel.tools.observe(viewLifecycleOwner) { tools ->

            binding.btnToolSelection.setOnClickListener {
                showToolSelectionBottomSheet(tools) { selected ->
                    selectedTools.clear()
                    selectedTools.addAll(selected)
                    updatedToolsDetailsList()
                }
            }

        }
        rentalViewModel.tools.observe(viewLifecycleOwner) { tools ->
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
        rentalViewModel.customers.observe(viewLifecycleOwner) { customers ->
            Log.d(TAG, "$customers")
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