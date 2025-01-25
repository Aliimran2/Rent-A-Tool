package com.miassolutions.rentatool.ui.fragments.others

import android.net.Uri
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import androidx.core.view.MenuProvider
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.Snackbar
import com.miassolutions.rentatool.myapplication.MyApplication
import com.miassolutions.rentatool.R
import com.miassolutions.rentatool.data.model.Customer
import com.miassolutions.rentatool.databinding.FragmentCustomerDetailsBinding
import com.miassolutions.rentatool.ui.viewmodels.SharedViewModel
import com.miassolutions.rentatool.ui.viewmodels.SharedViewModelFactory

class CustomerDetailsFragment : Fragment(R.layout.fragment_customer_details) {

    private var _binding: FragmentCustomerDetailsBinding? = null
    private val binding get() = _binding!!

    private val rentalViewModel: SharedViewModel by activityViewModels {
        SharedViewModelFactory((requireActivity().application as MyApplication).repository)
    }

    private val args: CustomerDetailsFragmentArgs by navArgs()
    private var customerName : String = ""


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentCustomerDetailsBinding.bind(view)

        val customerId = args.customerId

        requireActivity().addMenuProvider(object : MenuProvider {
            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                menuInflater.inflate(R.menu.customer_details_fragment_menu, menu)
            }

            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                return when (menuItem.itemId) {


                    R.id.edit_menu -> {
                        val action =
                            CustomerDetailsFragmentDirections.actionCustomerDetailsFragmentToUpdateDeleteCustomerFragment(customerId, customerName)
                        findNavController().navigate(action)
                        true
                    }
                    R.id.delete_menu -> {
                        rentalViewModel.getCustomerById(customerId).observe(viewLifecycleOwner) { customer ->
                            if (customer != null) {
                                confirmDeleteDialog(customer)
                            }
                        }
                        true
                    }

                    else -> false
                }
            }
        }, viewLifecycleOwner)


        observeViewModel(customerId)

    }
    private fun confirmDeleteDialog(customer: Customer) {
        MaterialAlertDialogBuilder(requireContext())
            .setTitle("Delete Customer?")
            .setMessage("Are you sure?")
            .setPositiveButton("Yes") { dialog, _ ->
                rentalViewModel.deleteCustomer(customer)
                Snackbar.make(
                    binding.root,
                    "${customer.customerName} deleted",
                    Snackbar.LENGTH_LONG
                ).setAction("Undo") {
                    rentalViewModel.addCustomer(customer)
                }.show()

                findNavController().popBackStack()
                dialog.dismiss()

            }
            .setNegativeButton("Cancel") { dialog, _ ->
                dialog.dismiss()
            }
            .show()
    }

    private fun setupUI(customer: Customer) {
        binding.apply {
            if (customer.customerPic.isNotEmpty()) {
                val customerPicUri = Uri.parse(customer.customerPic)
                ivCustomer.setImageURI(customerPicUri)
            } else {
                ivCustomer.setImageResource(R.drawable.place_holder_image)
            }
            tvCustomerName.text = customer.customerName
            customerName = customer.customerName // for passing to edit fragment
            tvCustomerPhone.text = customer.customerPhone
            tvCnic.text = customer.cnicNumber
            tvPlace.text = customer.constructionPlace
            tvContractorName.text = customer.contractorName
            tvContractorName.text = customer.contractorPhone
            tvOwnerName.text = customer.ownerName
            tvOwnerPhone.text = customer.ownerPhone
        }
    }

    private fun observeViewModel(customerId: Long) {


        rentalViewModel.getCustomerById(customerId).observe(viewLifecycleOwner) { customer ->
            customer?.let {
                setupUI(it)
            }
        }
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}