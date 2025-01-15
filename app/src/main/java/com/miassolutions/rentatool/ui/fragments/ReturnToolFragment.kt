package com.miassolutions.rentatool.ui.fragments

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.miassolutions.rentatool.R
import com.miassolutions.rentatool.databinding.FragmentReturnToolBinding

class ReturnToolFragment : Fragment(R.layout.fragment_return_tool) {

    private var _binding : FragmentReturnToolBinding? = null
    private val binding get() = _binding!!

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentReturnToolBinding.bind(view)










    }



    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}