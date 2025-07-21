package com.miassolutions.rentatool.ui.fragments.rentalrecord

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import com.miassolutions.rentatool.R
import com.miassolutions.rentatool.databinding.FragmentRentalsBinding


class RentalsFragment : Fragment(R.layout.fragment_rentals) {
    companion object {
        private const val TAG = "CustomerManagerFragment"
    }


    private val args: RentalsFragmentArgs by navArgs()

    private var _binding: FragmentRentalsBinding? = null
    private val binding get() = _binding!!

    var customerId: Long? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentRentalsBinding.bind(view)

        customerId = args.customerId

    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


}