package com.miassolutions.rentatool.ui.fragments.mainfragments.returntool

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.miassolutions.rentatool.R
import com.miassolutions.rentatool.databinding.FragmentReturnToolsBinding


class ReturnToolsFragment : Fragment(R.layout.fragment_return_tools) {

    private var _binding: FragmentReturnToolsBinding? = null
    private val binding get() = _binding!!


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentReturnToolsBinding.bind(view)




    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}