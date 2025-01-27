package com.miassolutions.rentatool.ui.fragments.rentalrecord

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.navArgs
import com.miassolutions.rentatool.myapplication.MyApplication
import com.miassolutions.rentatool.R
import com.miassolutions.rentatool.core.utils.extenstions.showBottomSheetDialog
import com.miassolutions.rentatool.core.utils.extenstions.showToast
import com.miassolutions.rentatool.data.model.RentalDetail
import com.miassolutions.rentatool.databinding.DialogCalculateRentBinding
import com.miassolutions.rentatool.databinding.FragmentRentalDetailsBinding
import com.miassolutions.rentatool.ui.adapters.RentalDetailAdapter
import com.miassolutions.rentatool.ui.viewmodels.SharedViewModel
import com.miassolutions.rentatool.ui.viewmodels.SharedViewModelFactory
import java.text.SimpleDateFormat
import java.util.Locale


class RentalDetailsFragment : Fragment(R.layout.fragment_rental_details) {

    private var _binding: FragmentRentalDetailsBinding? = null
    private val binding get() = _binding!!
    private lateinit var adapter: RentalDetailAdapter

    private val rentalViewModel: SharedViewModel by activityViewModels {
        SharedViewModelFactory((requireActivity().application as MyApplication).repository)
    }


    private val args: RentalDetailsFragmentArgs by navArgs()
    private var rentalId: Long = 0L

    private var customerName: String? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentRentalDetailsBinding.bind(view)


        rentalId = args.rentalId
        customerName = args.customerName

        Log.d(TAG, "$rentalId")

        binding.tvCustomerName.text = customerName

        setupRecyclerView()
        observeViewModel()

        // Observe rentResult here
        rentalViewModel.rentResult.observe(viewLifecycleOwner) { rent: Double? ->
            rent?.let {
                showToast("Calculated Rent: $it")
            } ?: showToast("Failed to calculate rent")
        }

    }

    private fun observeViewModel() {
        rentalViewModel.rentalDetailsByRental(rentalId)
            .observe(viewLifecycleOwner) { it: List<RentalDetail>? ->
                Log.d(TAG, "${it}")
                it?.forEach { rd: RentalDetail ->

                    rentalViewModel.rentalDetailsByRental(rd.rentalId)
                        .observe(viewLifecycleOwner) {
                            adapter.submitList(it)
                        }
                }
            }


    }

    private fun setupRecyclerView() {
        rentalViewModel.getAllTools.observe(viewLifecycleOwner) { tools ->
            adapter = RentalDetailAdapter(tools) { rentalDetail ->
                showToast("${rentalDetail.rentalDetailId}")

                showDialog(rentalDetail.rentalDetailId)

            }
            binding.rvReturnedToolsList.adapter = adapter
        }
    }

    private fun showDialog(rentalDetailId: Long) {
        val dialogBinding = DialogCalculateRentBinding.inflate(layoutInflater)
        showBottomSheetDialog(dialogBinding.root)

        //todo()
        val dateFormat = SimpleDateFormat("dd MMM yyyy", Locale.getDefault())
        val date = dateFormat.parse("31 Jan 2025")
        val timestamp: Long = date?.time ?: 0L


        val returnQuantity = dialogBinding.etReturnQuantity.text.toString().toIntOrNull() ?: 0



        dialogBinding.button.setOnClickListener {
            rentalViewModel.returnTool(rentalDetailId, returnQuantity, timestamp)

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