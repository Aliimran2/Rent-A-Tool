package com.miassolutions.rentatool.ui.fragments.lists

import android.os.Bundle
import android.view.View
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.miassolutions.rentatool.myapplication.MyApplication
import com.miassolutions.rentatool.R
import com.miassolutions.rentatool.databinding.FragmentStockBinding
import com.miassolutions.rentatool.ui.adapters.ToolListAdapter
import com.miassolutions.rentatool.ui.viewmodels.SharedViewModel
import com.miassolutions.rentatool.ui.viewmodels.SharedViewModelFactory
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class StockFragment : Fragment(R.layout.fragment_stock) {

    private var _binding: FragmentStockBinding? = null
    private val binding get() = _binding!!

    private val rentalViewModel: SharedViewModel by activityViewModels {
        SharedViewModelFactory((requireActivity().application as MyApplication).repository)
    }
    private lateinit var adapter: ToolListAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentStockBinding.bind(view)

        setupUI()
        observeViewModel()



    }

    private fun setupUI() {
        //initialize the adapter and assigning to the recyclerview
        adapter = ToolListAdapter()
        binding.rvStockList.adapter = adapter

        binding.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                rentalViewModel.searchTool(newText?:"")
                return true
            }
        })



    }

    private fun observeViewModel() {
        lifecycleScope.launch {
           repeatOnLifecycle(Lifecycle.State.STARTED){
                rentalViewModel.toolSearchResult.collectLatest { tools ->
                    adapter.submitList(tools)
                }
           }
        }
    }




    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}