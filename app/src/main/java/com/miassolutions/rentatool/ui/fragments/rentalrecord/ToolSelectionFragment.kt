package com.miassolutions.rentatool.ui.fragments.rentalrecord

import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import androidx.core.view.MenuProvider
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.navArgs
import com.miassolutions.rentatool.R
import com.miassolutions.rentatool.core.utils.extenstions.showDatePicker
import com.miassolutions.rentatool.core.utils.extenstions.showToast
import com.miassolutions.rentatool.databinding.FragmentToolSelectionBinding
import com.miassolutions.rentatool.myapplication.MyApplication
import com.miassolutions.rentatool.ui.adapters.ToolSelectionListAdapter
import com.miassolutions.rentatool.ui.viewmodels.SharedViewModel
import com.miassolutions.rentatool.ui.viewmodels.SharedViewModelFactory

class ToolSelectionFragment : Fragment(R.layout.fragment_tool_selection) {

    companion object {
        private const val TAG = "ToolSelectionFragment"
    }

    private var _binding: FragmentToolSelectionBinding? = null
    private val binding get() = _binding!!

    private var selectedEstimatedDate: Long = 0L
    private lateinit var toolSelectionListAdapter: ToolSelectionListAdapter

    val tempSelectedTools = mutableMapOf<Long, Int>()

    private val rentalViewModel: SharedViewModel by activityViewModels {
        SharedViewModelFactory((requireActivity().application as MyApplication).repository)
    }
    private val args: ToolSelectionFragmentArgs by navArgs()

    private var customerId: Long? = null

//    private val selectedTools = mutableListOf<Pair<Long, Int>>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentToolSelectionBinding.bind(view)

        customerId = args.customerId

        requireActivity().addMenuProvider(object : MenuProvider {
            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                menuInflater.inflate(R.menu.done_menu, menu)
            }

            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                return when (menuItem.itemId) {
                    R.id.done_menu -> {
                        showToast("Tools selected")
                        updateDatabase()
                        true
                    }

                    else -> false
                }
            }
        }, viewLifecycleOwner)


        toolSelectionListAdapter = ToolSelectionListAdapter { selectedTools ->
            tempSelectedTools.clear()

            tempSelectedTools.putAll(selectedTools)
            rentalViewModel.updatedSelectedTools(selectedTools)
        }

        rentalViewModel.allTools.observe(viewLifecycleOwner) { tools ->
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

    private fun updateDatabase() {
        if (customerId == null) {
            showToast("Customer ID is missing.")
            Log.e(TAG, "Customer ID is null.")
            return
        }

        if (tempSelectedTools.isEmpty()) {
            showToast("No tools selected.")
            Log.e(TAG, "No tools selected.")
            return
        }

        if (selectedEstimatedDate == 0L) {
            showToast("Please select an estimated return date.")
            Log.e(TAG, "Estimated return date not selected.")
            return
        }
        val selectedToolsList = tempSelectedTools.map { Pair(it.key, it.value) }
        rentalViewModel.addRental(customerId!!, selectedToolsList, System.currentTimeMillis())
            .also {
                showToast("Rental record successfully added.")
                Log.d(
                    TAG,
                    "Rental record added for customer ID $customerId with tools $selectedToolsList and estimated return date $selectedEstimatedDate."
                )
            }
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}