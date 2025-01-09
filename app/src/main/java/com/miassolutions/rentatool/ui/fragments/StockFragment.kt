package com.miassolutions.rentatool.ui.fragments

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.miassolutions.rentatool.R
import com.miassolutions.rentatool.databinding.FragmentStockBinding
import com.miassolutions.rentatool.ui.adapters.ToolListAdapter
import com.miassolutions.rentatool.ui.viewmodels.RentalViewModel
import com.miassolutions.rentatool.utils.helper.NavigationAnimation
import com.miassolutions.rentatool.utils.helper.getNavOptions
import com.miassolutions.rentatool.utils.mockdb.DataProvider

class StockFragment : Fragment(R.layout.fragment_stock) {

    private var _binding: FragmentStockBinding? = null
    private val binding get() = _binding!!

    private val rentalViewModel: RentalViewModel by activityViewModels()
    private lateinit var adapter: ToolListAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentStockBinding.bind(view)

        adapter = ToolListAdapter()

        rentalViewModel.tools.observe(viewLifecycleOwner){tools ->

            adapter.submitList(tools)
            binding.rvStockList.adapter = adapter
        }

        val options = getNavOptions(NavigationAnimation.ZOOM_IN)

        binding.btnRentTool.setOnClickListener {

            findNavController().navigate(
                R.id.action_stockFragment_to_rentToolFragment,
                null,
                options
            )
        }


    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}