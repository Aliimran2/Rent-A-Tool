package com.miassolutions.rentatool.ui.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.navArgs
import com.miassolutions.rentatool.MyApplication
import com.miassolutions.rentatool.R
import com.miassolutions.rentatool.databinding.FragmentCustomerDetailsBinding
import com.miassolutions.rentatool.databinding.FragmentRentalDetailsBinding
import com.miassolutions.rentatool.ui.viewmodels.SharedViewModel
import com.miassolutions.rentatool.ui.viewmodels.SharedViewModelFactory


class RentalDetailsFragment : Fragment(R.layout.fragment_rental_details) {

    private var _binding : FragmentRentalDetailsBinding? = null
    private val binding get() =  _binding!!

    private val rentalViewModel : SharedViewModel by activityViewModels {
        SharedViewModelFactory((requireActivity().application as MyApplication).repository)
    }


    private val args : RentalDetailsFragmentArgs by navArgs()
    private var rentalId : Long = 0L

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentRentalDetailsBinding.bind(view)


        rentalId =args.rentalId
        Log.d(TAG, "$rentalId")

    }

    private fun observeViewModel() {
        rentalViewModel.searchRentalsByCustomer(rentalId).observe(viewLifecycleOwner){rentals ->
        }
    }





    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        private const val TAG = "RentalDetailsFragment"
    }
}