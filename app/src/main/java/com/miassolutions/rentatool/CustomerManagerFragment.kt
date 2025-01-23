package com.miassolutions.rentatool

import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import androidx.core.view.MenuProvider
import androidx.fragment.app.Fragment
import androidx.navigation.NavArgs
import androidx.navigation.fragment.navArgs
import com.google.android.material.tabs.TabLayoutMediator
import com.miassolutions.rentatool.core.utils.extenstions.showToast
import com.miassolutions.rentatool.databinding.FragmentCustomerManagerBinding
import com.miassolutions.rentatool.ui.adapters.TabPagerAdapter


class CustomerManagerFragment : Fragment(R.layout.fragment_customer_manager) {
    companion object{
        private const val TAG = "CustomerManagerFragment"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    private val args : CustomerManagerFragmentArgs by navArgs()

    private var _binding : FragmentCustomerManagerBinding? = null
    private val binding get() = _binding!!

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentCustomerManagerBinding.bind(view)

        val customerId = args.customerId


        requireActivity().addMenuProvider(object : MenuProvider{
            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                menuInflater.inflate(R.menu.customer_fragment_menu, menu)
            }

            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                return when(menuItem.itemId){
                    R.id.menu_pdf_report -> {
                        showToast("Creating pdf...")
                        true
                    }
                    R.id.remind_menu, R.id.call_menu -> {
                        showToast("Reminding the customer")
                        true
                    }
                    R.id.call_menu -> {
                        showToast("Call the customer")
                        true
                    }
                    else -> false
                }
            }
        }, viewLifecycleOwner)









    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }



}