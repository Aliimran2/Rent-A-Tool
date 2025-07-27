package com.miassolutions.rentatool.ui.fragments.mainfragments.lists.toolstock

import android.os.Bundle
import android.view.View
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.miassolutions.rentatool.R
import com.miassolutions.rentatool.utils.extenstions.collectingFlow
import com.miassolutions.rentatool.utils.extenstions.showToast
import com.miassolutions.rentatool.utils.helper.hide
import com.miassolutions.rentatool.utils.helper.show
import com.miassolutions.rentatool.databinding.FragmentStockListBinding
import com.miassolutions.rentatool.ui.fragments.mainfragments.forms.tool.ToolFormFragment
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest

@AndroidEntryPoint
class StockListFragment : Fragment(R.layout.fragment_stock_list) {

    private var _binding: FragmentStockListBinding? = null
    private val binding get() = _binding!!

    private val viewModel by viewModels<StockListViewModel>()
    private val adapter = ToolListAdapter()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentStockListBinding.bind(view)



        setupSearchQuery()
        setupUiState()
        setupRecyclerview()
        setupListeners()
    }

    private fun setupListeners() {
        binding.floatingActionButton.setOnClickListener{
            val bottomSheet = ToolFormFragment()
            bottomSheet.show(parentFragmentManager, null)
        }

    }



    private fun setupRecyclerview() {
        binding.rvStock.show()
        binding.rvStock.adapter = adapter
    }


    private fun setupUiState() {
        collectingFlow {
            viewModel.uiState.collectLatest { state ->
                binding.apply {
                    if (state.isLoading) progressbar.show() else progressbar.hide()
                }
                adapter.submitList(state.stockList)

                state.errorMessage?.let {
                    showToast(it)
                }
            }
        }
    }

    private fun setupSearchQuery() {
        binding.searchInput.doAfterTextChanged { viewModel.onSearchQueryChanged(it.toString()) }
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}