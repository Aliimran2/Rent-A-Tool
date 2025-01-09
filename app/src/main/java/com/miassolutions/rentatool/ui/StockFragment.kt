package com.miassolutions.rentatool.ui

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

//        val options = NavOptions.Builder()
//            .setEnterAnim(R.anim.slide_in_right)   // Enter animation
//            .setExitAnim(R.anim.slide_out_left)    // Exit animation
//            .setPopEnterAnim(R.anim.slide_in_left) // Pop Enter animation
//            .setPopExitAnim(R.anim.slide_out_right) // Pop Exit animation
//            .build()

        val options = getNavOptions(NavigationAnimation.ZOOM_IN)

        binding.rentButton.setOnClickListener {

        findNavController().navigate(R.id.action_stockFragment_to_returnToolFragment, null, options)
        }


    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}