package com.miassolutions.rentatool.ui.fragments.rentalrecord

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.navArgs
import com.miassolutions.rentatool.myapplication.MyApplication
import com.miassolutions.rentatool.R
import com.miassolutions.rentatool.core.utils.extenstions.showToast
import com.miassolutions.rentatool.data.model.RentalDetail
import com.miassolutions.rentatool.databinding.FragmentRentalDetailsBinding
import com.miassolutions.rentatool.ui.adapters.RentalDetailAdapter
import com.miassolutions.rentatool.ui.viewmodels.SharedViewModel
import com.miassolutions.rentatool.ui.viewmodels.SharedViewModelFactory


class RentalDetailsFragment : Fragment(R.layout.fragment_rental_details) {

    private var _binding: FragmentRentalDetailsBinding? = null
    private val binding get() = _binding!!
    private lateinit var adapter: RentalDetailAdapter

    private val rentalViewModel: SharedViewModel by activityViewModels {
        SharedViewModelFactory((requireActivity().application as MyApplication).repository)
    }


    private val args: RentalDetailsFragmentArgs by navArgs()
    private var rentalId: Long = 0L

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentRentalDetailsBinding.bind(view)


        rentalId = args.rentalId
        Log.d(TAG, "$rentalId")

        setupRecyclerView()
        observeViewModel()

    }

    private fun observeViewModel() {



        rentalViewModel.rentalDetailsByRental(rentalId).observe(viewLifecycleOwner) { it: List<RentalDetail>? ->
            Log.d(TAG, "${it}")
            it?.forEach { rd: RentalDetail ->
//                binding.tvCustomerName.text = "Customer Id : ${rental.customerId}\nRental Id : ${rental.rentalId}"
                rentalViewModel.rentalDetailsByRental(rd.rentalId)
                    .observe(viewLifecycleOwner) {
                        adapter.submitList(it)
                    }
            }
        }
    }

    private fun setupRecyclerView() {
        rentalViewModel.allTools.observe(viewLifecycleOwner) { tools ->
            adapter = RentalDetailAdapter(tools){rentalDetail ->
                showToast("${rentalDetail.quantity}")
            }
            binding.rvReturnedToolsList.adapter = adapter
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