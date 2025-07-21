package com.miassolutions.rentatool.ui.fragments.forms.tool

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.miassolutions.rentatool.R
import com.miassolutions.rentatool.core.utils.helper.clearInputs
import com.miassolutions.rentatool.data.model.ToolEntity
import com.miassolutions.rentatool.databinding.FragmentToolFormBinding

class ToolFormFragment : BottomSheetDialogFragment() {

    private var _binding: FragmentToolFormBinding? = null
    private val binding get() = _binding!!



    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentToolFormBinding.inflate(inflater,container, false)
        setupSubmitBtn()

        return binding.root
    }





    private fun setupSubmitBtn() {
        binding.btnSubmit.setOnClickListener {
            val tool = collectToolInput()
            if (tool != null) {
                checkAndAddTool(tool)
            }
        }
    }

    private fun checkAndAddTool(toolEntity: ToolEntity) {
        val toolName = toolEntity.name.lowercase().trim()
//        rentalViewModel.checkToExists(toolName).observe(viewLifecycleOwner){exists ->
//            exists?.let {
//                if (it ){
//                    showToast("This tool already exists")
//                } else {
//                    rentalViewModel.addTool(tool)
//                    showToast("Tool added successfully")
//                    clearInputsFields()
//                }
//            }
//
//        }
    }




    private fun collectToolInput(): ToolEntity? {
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
                return ToolEntity(
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
                false
            }

            binding.etQuantity.text.isNullOrEmpty() -> {
                false
            }

            binding.etRentPrice.text.isNullOrEmpty() -> {
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