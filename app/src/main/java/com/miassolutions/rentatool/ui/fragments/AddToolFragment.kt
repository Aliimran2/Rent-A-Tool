package com.miassolutions.rentatool.ui.fragments

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.miassolutions.rentatool.MyApplication
import com.miassolutions.rentatool.R
import com.miassolutions.rentatool.core.utils.helper.clearInputs
import com.miassolutions.rentatool.core.utils.helper.showToast
import com.miassolutions.rentatool.data.model.Tool
import com.miassolutions.rentatool.databinding.FragmentAddToolBinding
import com.miassolutions.rentatool.ui.viewmodels.SharedViewModel
import com.miassolutions.rentatool.ui.viewmodels.SharedViewModelFactory

class AddToolFragment : Fragment(R.layout.fragment_add_tool) {

    private var _binding: FragmentAddToolBinding? = null
    private val binding get() = _binding!!

    private val rentalViewModel: SharedViewModel by activityViewModels {
        SharedViewModelFactory((requireActivity().application as MyApplication).repository)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentAddToolBinding.bind(view)

        setupSubmitBtn()

    }

    private fun setupSubmitBtn() {
        binding.btnSubmit.setOnClickListener {
            val tool = collectToolInput()
            if (tool != null) {
                rentalViewModel.addTool(tool)
                Log.d("AddToolFragment", "Tool: $tool")

                showToast(requireContext(), getString(R.string.is_saved_successfully, tool.name))
                clearInputsFields()
            }
        }
    }


    private fun collectToolInput(): Tool? {
        //access all views and store into variables
        binding.apply {
            val toolName = etToolName.text.toString()
            val quantityOfTool = etQuantity.text.toString().toIntOrNull() ?: 0
            val condition = when (rgCondition.checkedRadioButtonId) {
                R.id.rb_new -> getString(R.string.new_condition)
                R.id.rb_old -> getString(R.string.old_condition)
                else -> getString(R.string.new_condition)
            }
            val rentPrice = etRentPrice.text.toString().toDoubleOrNull() ?: 0.0

            if (validateInputs()) {
                return Tool(
                    toolId = System.currentTimeMillis(),
                    name = toolName,
                    rentPerDay = rentPrice,
                    totalStock = quantityOfTool,
                    availableStock = quantityOfTool,
                    rentedQuantity = 0,
                    toolCondition = condition
                )

            }

        }
        return null

    }

    private fun validateInputs(): Boolean {
        return when {
            binding.etToolName.text.isNullOrEmpty() -> {
                showToast(requireContext(), getString(R.string.enter_tool_name))
                false
            }

            binding.etQuantity.text.isNullOrEmpty() -> {
                showToast(requireContext(), getString(R.string.please_enter_quantity_of_tool))
                false
            }

            binding.etRentPrice.text.isNullOrEmpty() -> {
                showToast(requireContext(), getString(R.string.please_enter_the_rent_price))
                false
            }

            else -> true

        }
    }

    private fun clearInputsFields() {
        binding.apply {
            clearInputs(
                etToolName,
                etQuantity,
                etRentPrice
            )
        }
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}