package com.miassolutions.rentatool.ui.fragments.mainfragments.forms.tool

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.viewModels
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.miassolutions.rentatool.R
import com.miassolutions.rentatool.utils.extenstions.collectingFlow
import com.miassolutions.rentatool.utils.extenstions.setTextIfChanged
import com.miassolutions.rentatool.utils.extenstions.showToast
import com.miassolutions.rentatool.databinding.FragmentToolFormBinding
import com.miassolutions.rentatool.utils.helper.clearInputs
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ToolFormFragment : BottomSheetDialogFragment() {

    private var _binding: FragmentToolFormBinding? = null
    private val binding get() = _binding!!
    private val viewModel by viewModels<ToolFormViewModel>()


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentToolFormBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupUiState()
        setupUiEvent()
        setupListeners()
    }

    private fun setupListeners() {
        with(binding) {
            etToolName.doAfterTextChanged { viewModel.onToolNameChange(it.toString()) }
            etQuantity.doAfterTextChanged { viewModel.onQuantityChanged(it.toString()) }
            etRentPrice.doAfterTextChanged { viewModel.onRentChanged(it.toString()) }
            rgCondition.setOnCheckedChangeListener { _, checkedId ->
                viewModel.onConditionChanged(
                    when (checkedId) {
                        R.id.rb_new -> ToolCondition.NEW
                        else -> ToolCondition.OLD
                    }
                )
            }

            btnSaveAndExit.setOnClickListener {
                viewModel.onSubmitClick(isSaveAndExit = true)
            }
            btnSaveAndNew.setOnClickListener {
                viewModel.onSubmitClick(isSaveAndExit = false)
                clearAllFields()
            }
        }
    }

    private fun setupUiEvent() {
        collectingFlow {
            viewModel.uiEvent.collect { event ->
                when (event) {
                    is ToolFormUiEvent.ShowToast -> showToast(event.message)
                    ToolFormUiEvent.NavigationBack -> {
                        dismiss()
                    }
                }

            }
        }
    }

    private fun clearAllFields() {
        with(binding) {
            clearInputs(
                etToolName,
                etQuantity,
                etRentPrice
            )
            binding.etToolName.requestFocus()
        }


    }

    private fun setupUiState() {
        collectingFlow {
            viewModel.uiState.collect { state ->
                with(binding) {
                    etToolName.setTextIfChanged(state.toolName)
                    etQuantity.setTextIfChanged(state.totalQuantity)
                    etRentPrice.setTextIfChanged(state.rentPricePerDay)
                    rgCondition.check(
                        when (state.condition) {
                            ToolCondition.NEW -> R.id.rb_new
                            ToolCondition.OLD -> R.id.rb_old
                        }
                    )
                }

            }
        }
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}