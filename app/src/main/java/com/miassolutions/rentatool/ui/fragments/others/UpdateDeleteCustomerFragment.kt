package com.miassolutions.rentatool.ui.fragments.others

import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import androidx.activity.OnBackPressedCallback
import androidx.core.view.MenuProvider
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.Snackbar
import com.miassolutions.rentatool.R
import com.miassolutions.rentatool.core.utils.extenstions.showToast
import com.miassolutions.rentatool.data.model.Customer
import com.miassolutions.rentatool.databinding.FragmentUpdateCustomerBinding
import com.miassolutions.rentatool.myapplication.MyApplication
import com.miassolutions.rentatool.ui.viewmodels.SharedViewModel
import com.miassolutions.rentatool.ui.viewmodels.SharedViewModelFactory

class UpdateDeleteCustomerFragment : Fragment(R.layout.fragment_update_customer) {

    private var _binding: FragmentUpdateCustomerBinding? = null
    private val binding get() = _binding!!

    private val rentalViewModel: SharedViewModel by activityViewModels {
        SharedViewModelFactory((requireActivity().application as MyApplication).repository)
    }

    private val args: UpdateDeleteCustomerFragmentArgs by navArgs()

    private var customerId: Long = 0L

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentUpdateCustomerBinding.bind(view)


        customerId = args.cutomerId
        rentalViewModel.getCustomerById(customerId).observe(viewLifecycleOwner) { customer ->
            if (customer != null) {
                setupUI(customer)
            }
        }

        requireActivity().addMenuProvider(object : MenuProvider {
            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                menuInflater.inflate(R.menu.delete_menu, menu)
            }

            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                return when (menuItem.itemId) {
                    R.id.delete_menu -> {
                        rentalViewModel.customer.observe(viewLifecycleOwner) { customer ->
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

        requireActivity().onBackPressedDispatcher.addCallback(
            viewLifecycleOwner, object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    updateData()
                }
            }
        )

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
            etCustomerName.setText(customer.customerName)
            etCnic.setText(customer.cnicNumber)
            etCustomerPhone.setText(customer.customerPhone) // Should be phoneNumber here instead of cnicNumber
            etContractorName.setText(customer.contractorName)
            etContractorCell.setText(customer.contractorPhone)
            etConstructionPlace.setText(customer.constructionPlace)
            etOwnerName.setText(customer.ownerName)
            etOwnerCell.setText(customer.ownerPhone)


        }
    }

    private fun updateData() {
        val updatedCustomer = collectCustomerInput()
        if (updatedCustomer != null) {

            rentalViewModel.updateCustomer(updatedCustomer)
            showToast("Customer data updated!")
            findNavController().popBackStack()
        } else {

            showToast("Invalid customer data!")
        }
    }

    private fun collectCustomerInput(): Customer? {
        binding.apply {
            val customerName = etCustomerName.text.toString().trim()
            val cNicNumber = etCnic.text.toString().trim()
            val customerPhone = etCustomerPhone.text.toString().trim()
            val placeOfConstruction = etConstructionPlace.text.toString().trim()
            val contractorName = etContractorName.text.toString().trim()
            val contractorPhone = etContractorCell.text.toString().trim()
            val ownerName = etOwnerName.text.toString().trim()
            val ownerPhone = etOwnerCell.text.toString().trim()

            // Validate inputs
            if (
                customerName.isNotBlank() &&
                cNicNumber.isNotBlank() &&
                customerPhone.isNotBlank() &&
                placeOfConstruction.isNotBlank() &&
                contractorName.isNotBlank() &&
                contractorPhone.isNotBlank() &&
                ownerName.isNotBlank() &&
                ownerPhone.isNotBlank()
            ) {
                return Customer(
                    customerId = customerId,
                    customerName = customerName,
                    cnicNumber = cNicNumber,
                    customerPhone = customerPhone,
                    constructionPlace = placeOfConstruction,
                    contractorName = contractorName,
                    contractorPhone = contractorPhone,
                    ownerName = ownerName,
                    ownerPhone = ownerPhone
                )
            }
        }
        return null // If validation fails, return null
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}