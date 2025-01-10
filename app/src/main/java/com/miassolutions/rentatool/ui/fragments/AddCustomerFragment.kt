package com.miassolutions.rentatool.ui.fragments

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.miassolutions.rentatool.R
import com.miassolutions.rentatool.databinding.FragmentAddCustomerBinding
import com.miassolutions.rentatool.ui.viewmodels.RentalViewModel

class AddCustomerFragment : Fragment(R.layout.fragment_add_customer) {

    private var _binding : FragmentAddCustomerBinding? = null
    private val binding get() = _binding!!

    private val rentalViewModel : RentalViewModel by activityViewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentAddCustomerBinding.bind(view)


    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}