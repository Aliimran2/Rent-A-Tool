package com.miassolutions.rentatool.ui.fragments

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.miassolutions.rentatool.R
import com.miassolutions.rentatool.databinding.FragmentAddToolBinding
import com.miassolutions.rentatool.ui.viewmodels.RentalViewModel

class AddToolFragment : Fragment(R.layout.fragment_add_tool) {

    private var _binding: FragmentAddToolBinding? = null
    private val binding get() = _binding!!

    private val rentalViewModel: RentalViewModel by activityViewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentAddToolBinding.bind(view)

        binding.apply {

        }

    }




    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}