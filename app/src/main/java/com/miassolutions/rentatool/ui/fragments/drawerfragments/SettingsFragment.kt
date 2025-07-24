package com.miassolutions.rentatool.ui.fragments.drawerfragments

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.miassolutions.rentatool.R
import com.miassolutions.rentatool.databinding.FragmentSettingsBinding
import com.miassolutions.rentatool.ui.viewmodel.MainViewModel
import com.miassolutions.rentatool.utils.extenstions.showConfirmDialog
import com.miassolutions.rentatool.utils.extenstions.showToast
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SettingsFragment : Fragment(R.layout.fragment_settings) {

    private var _binding: FragmentSettingsBinding? = null
    private val binding get() = _binding!!

    private val viewModel by viewModels<MainViewModel>()


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentSettingsBinding.bind(view)

        binding.resetBtn.setOnClickListener {
            showConfirmDialog(
                title = "Confirmation",
                message = "Are you sure to reset database? This can't be undone.",
                positiveText = "Yes, sure",
                onConfirm = {
                    viewModel.resetAllDatabase()
                    showToast("Database reset")
                }
            )

        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}