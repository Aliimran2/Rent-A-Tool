package com.miassolutions.rentatool

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.miassolutions.rentatool.databinding.FragmentRentalItemsBinding


class RentalItemsFragment : Fragment(R.layout.fragment_rental_items) {

    private var _binding: FragmentRentalItemsBinding? = null
    private val binding get() = _binding!!


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentRentalItemsBinding.bind(view)




    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}