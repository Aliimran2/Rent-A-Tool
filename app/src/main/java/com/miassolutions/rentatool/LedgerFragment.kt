package com.miassolutions.rentatool

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.miassolutions.rentatool.databinding.FragmentLedgerBinding


class LedgerFragment : Fragment(R.layout.fragment_ledger) {

    private var _binding : FragmentLedgerBinding? = null
    private val binding get() = _binding!!

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        _binding = FragmentLedgerBinding.bind(view)




    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }



}