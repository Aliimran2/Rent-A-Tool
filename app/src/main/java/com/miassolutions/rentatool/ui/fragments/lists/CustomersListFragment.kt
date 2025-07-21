package com.miassolutions.rentatool.ui.fragments.lists

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import androidx.core.view.MenuProvider
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.miassolutions.rentatool.R
import com.miassolutions.rentatool.core.utils.helper.showToast
import com.miassolutions.rentatool.data.model.CustomerEntity
import com.miassolutions.rentatool.databinding.FragmentCustomersListBinding
import com.miassolutions.rentatool.ui.adapters.CustomerListAdapter
import com.miassolutions.rentatool.ui.fragments.forms.tool.ToolFormFragment

class CustomersListFragment : Fragment(R.layout.fragment_customers_list) {

    private var _binding: FragmentCustomersListBinding? = null
    private val binding get() = _binding!!


    private lateinit var adapter: CustomerListAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentCustomersListBinding.bind(view)





    }


    private fun menuProvider() {
        requireActivity().addMenuProvider(object : MenuProvider {
            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                menuInflater.inflate(R.menu.main_menu, menu)
            }

            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                return when (menuItem.itemId) {
                    R.id.add_customer_menu -> {
                        findNavController().navigate(R.id.customerFormFragment)
                        true
                    }

                    R.id.add_tool_menu -> {
                        val showBottomSheet = ToolFormFragment()
                        showBottomSheet.show(parentFragmentManager, showBottomSheet.tag)
                        true
                    }

                    else -> false
                }
            }
        }, viewLifecycleOwner)

    }









    private fun navigateToCustomerManagerFragment(customerEntity: CustomerEntity) {
        val customerId = customerEntity.customerId
        val customerName = customerEntity.customerName
        val action =
            CustomersListFragmentDirections.actionCustomersListFragmentToCustomerManagerFragment(
                customerId,
                customerName
            )
        findNavController().navigate(action)
    }

    private fun initializePhoneCall(phoneNumber: String) {
        try {
            val intent = Intent(Intent.ACTION_DIAL).apply {
                data = Uri.parse("tel:$phoneNumber")  // This opens the dialer with the number
            }
            startActivity(intent)
        } catch (e: Exception) {
            showToast(requireContext(), "Unable to open the dialer.")
        }
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}