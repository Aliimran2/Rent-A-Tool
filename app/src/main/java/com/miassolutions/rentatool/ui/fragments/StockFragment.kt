package com.miassolutions.rentatool.ui.fragments

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.miassolutions.rentatool.R
import com.miassolutions.rentatool.databinding.FragmentStockBinding
import com.miassolutions.rentatool.utils.helper.NavigationAnimation
import com.miassolutions.rentatool.utils.helper.getNavOptions

class StockFragment: Fragment(R.layout.fragment_stock) {

    private var _binding : FragmentStockBinding? = null
    private val binding get() = _binding!!

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentStockBinding.bind(view)


        val options = getNavOptions(NavigationAnimation.ZOOM_IN)

        binding.btnRentTool.setOnClickListener {

        findNavController().navigate(R.id.action_stockFragment_to_returnToolFragment, null, options)
        }


    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}