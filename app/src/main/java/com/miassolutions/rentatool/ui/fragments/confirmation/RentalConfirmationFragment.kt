package com.miassolutions.rentatool.ui.fragments.confirmation

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.miassolutions.rentatool.R
import com.miassolutions.rentatool.databinding.FragmentRentalConfirmationBinding
import com.miassolutions.rentatool.ui.fragments.toolselection.SelectedTool
import com.miassolutions.rentatool.utils.Constants

class RentalConfirmationFragment : Fragment(R.layout.fragment_rental_confirmation) {

    private var _binding: FragmentRentalConfirmationBinding? = null
    private val binding get() = _binding!!

    private val args by navArgs<RentalConfirmationFragmentArgs>()

    private val gson = Gson()
    private lateinit var selectedTools: List<SelectedTool>

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentRentalConfirmationBinding.bind(view)

        val selectedToolsType = object : TypeToken<List<SelectedTool>>() {}.type
        selectedTools = gson.fromJson(args.selectedToolsJson,selectedToolsType)

        selectedTools.forEach {
        Log.d(Constants.TAG, "${it.toolId} - ${it.toolName} - ${it.quantity} - ${args.estReturnDate}")

        }


    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}