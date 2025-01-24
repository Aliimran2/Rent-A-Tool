package com.miassolutions.rentatool.ui.fragments.others

import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import androidx.core.view.MenuProvider
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.navArgs
import com.miassolutions.rentatool.R
import com.miassolutions.rentatool.core.utils.extenstions.showToast
import com.miassolutions.rentatool.databinding.FragmentUpdateCustomerBinding
import com.miassolutions.rentatool.myapplication.MyApplication
import com.miassolutions.rentatool.ui.viewmodels.SharedViewModel
import com.miassolutions.rentatool.ui.viewmodels.SharedViewModelFactory

class UpdateDeleteCustomerFragment : Fragment(R.layout.fragment_update_customer) {

    private var _binding : FragmentUpdateCustomerBinding? = null
    private val binding get() = _binding!!

    private val rentalViewModel: SharedViewModel by activityViewModels {
        SharedViewModelFactory((requireActivity().application as MyApplication).repository)
    }

    private val args: CustomerDetailsFragmentArgs by navArgs()

    private var customerId : Long? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentUpdateCustomerBinding.bind(view)

requireActivity().addMenuProvider(object : MenuProvider{
    override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
        menuInflater.inflate(R.menu.delete_menu, menu)
    }

    override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
        return when(menuItem.itemId){
            R.id.delete_menu -> {
                showToast("Deleting customer")
                true
            }
            else -> false
        }
    } },viewLifecycleOwner)

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}