package com.miassolutions.rentatool.ui.fragments

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.miassolutions.rentatool.MyApplication
import com.miassolutions.rentatool.R
import com.miassolutions.rentatool.databinding.FragmentStockBinding
import com.miassolutions.rentatool.ui.adapters.ToolListAdapter
import com.miassolutions.rentatool.ui.viewmodels.SharedViewModel
import com.miassolutions.rentatool.ui.viewmodels.SharedViewModelFactory

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


        binding.btnRentTool.setOnClickListener {

            findNavController().navigate(R.id.action_stockFragment_to_rentToolFragment, null)

        }
    }

    private fun observeViewModel() {
        rentalViewModel.tools.observe(viewLifecycleOwner) {
            adapter.submitList(it)
        }
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}