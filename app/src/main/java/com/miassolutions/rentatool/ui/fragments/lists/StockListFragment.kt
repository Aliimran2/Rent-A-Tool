package com.miassolutions.rentatool.ui.fragments.lists

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.miassolutions.rentatool.R
import com.miassolutions.rentatool.databinding.FragmentStockListBinding


class StockListFragment : Fragment(R.layout.fragment_stock_list) {

    private var _binding: FragmentStockListBinding? = null
    private val binding get() = _binding!!

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }




    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}