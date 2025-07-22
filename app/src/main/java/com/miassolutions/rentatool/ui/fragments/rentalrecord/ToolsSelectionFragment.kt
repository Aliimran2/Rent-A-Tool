package com.miassolutions.rentatool.ui.fragments.rentalrecord

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.navArgs
import com.miassolutions.rentatool.R
import com.miassolutions.rentatool.utils.extenstions.showDatePicker
import com.miassolutions.rentatool.utils.extenstions.showToast
import com.miassolutions.rentatool.databinding.FragmentToolsSelectionBinding
import com.miassolutions.rentatool.ui.adapters.ToolSelectionListAdapter
import kotlinx.coroutines.launch

class ToolsSelectionFragment : Fragment(R.layout.fragment_tools_selection) {

    companion object {
        private const val TAG = "ToolSelectionFragment"
    }

    private var _binding: FragmentToolsSelectionBinding? = null
    private val binding get() = _binding!!

    private var selectedEstimatedDate: Long = 0L
    private lateinit var toolSelectionListAdapter: ToolSelectionListAdapter

    private val tempSelectedTools = mutableMapOf<Long, Int>()


    private val args: ToolsSelectionFragmentArgs by navArgs()

    private var customerId: Long? = null

//    private val selectedTools = mutableListOf<Pair<Long, Int>>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentToolsSelectionBinding.bind(view)

        customerId = args.customerId




        toolSelectionListAdapter = ToolSelectionListAdapter { selectedTools ->
            tempSelectedTools.clear()

            tempSelectedTools.putAll(selectedTools)
        }
        lifecycleScope.launch {

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

    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}