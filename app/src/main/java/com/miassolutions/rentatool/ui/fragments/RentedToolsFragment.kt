package com.miassolutions.rentatool.ui.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import com.miassolutions.rentatool.R
import com.miassolutions.rentatool.databinding.FragmentRentedToolsBinding


class RentedToolsFragment : Fragment(R.layout.fragment_rented_tools) {

    private var _binding : FragmentRentedToolsBinding? = null
    private val binding get() = _binding!!



    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentRentedToolsBinding.bind(view)



    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


}