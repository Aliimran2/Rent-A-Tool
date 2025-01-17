package com.miassolutions.rentatool

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.NavArgs
import androidx.navigation.fragment.navArgs
import com.google.android.material.tabs.TabLayoutMediator
import com.miassolutions.rentatool.databinding.FragmentCustomerManagerBinding
import com.miassolutions.rentatool.ui.adapters.TabPagerAdapter


class CustomerManagerFragment : Fragment(R.layout.fragment_customer_manager) {
    companion object{
        private const val TAG = "CustomerManagerFragment"
    }

    private val args : CustomerManagerFragmentArgs by navArgs()

    private var _binding : FragmentCustomerManagerBinding? = null
    private val binding get() = _binding!!

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentCustomerManagerBinding.bind(view)

        val customerId = args.customerId
        Log.d(TAG, "$customerId")

        val adapter = TabPagerAdapter(this)
        binding.viewPager.adapter = adapter

        TabLayoutMediator(binding.tabLayout, binding.viewPager){tab, position ->
            when(position){
                0 -> tab.text = "Rented"
                1 -> tab.text = "Returned"
                2 -> tab.text = "Retained"
                3 -> tab.text = "Bill"

            }
        }.attach()



    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }



}