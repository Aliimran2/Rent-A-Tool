package com.miassolutions.rentatool.ui.fragments

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.miassolutions.rentatool.R
import com.miassolutions.rentatool.data.model.Customer
import com.miassolutions.rentatool.databinding.FragmentAddCustomerBinding
import com.miassolutions.rentatool.ui.viewmodels.RentalViewModel
import com.miassolutions.rentatool.core.utils.helper.clearInputs
import com.miassolutions.rentatool.core.utils.helper.isPermissionGranted
import com.miassolutions.rentatool.core.utils.helper.requestPermission
import com.miassolutions.rentatool.core.utils.helper.showToast
import java.io.File

class AddCustomerFragment : Fragment(R.layout.fragment_add_customer) {

    private var _binding: FragmentAddCustomerBinding? = null
    private val binding get() = _binding!!

    private val rentalViewModel: RentalViewModel by activityViewModels()

    private var customerPicUri: Uri? = null // Holds the URI of the selected image

    private val REQUEST_CODE_CAMERA_PERMISSION = 101

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentAddCustomerBinding.bind(view)

        setupSubmitBtn()
        setupSelectPicBtn()

    }

    private fun setupSubmitBtn() {
        binding.btnSubmit.setOnClickListener {
            val customer = collectCustomer()
            if (customer != null) {

                rentalViewModel.addCustomer(customer)
                showToast(
                    requireContext(),
                    getString(R.string.is_saved_successfully, customer.customerName)
                )
                clearInputFields()
            }

        }
    }

    private fun collectCustomer(): Customer? {
        binding.apply {

            val customerName = etCustomerName.text.toString()
            val customerCnic = etCnic.text.toString()
            val customerPhone = etCustomerPhone.text.toString()
            val constructionPlace = etConstructionPlace.text.toString()
            val contractorName = etContractorName.text.toString()
            val contractorPhone = etContractorPhone.text.toString()
            val ownerName = etOwnerName.text.toString()
            val ownerPhone = etOwnerPhone.text.toString()

            if (validateInputs()) {
                return Customer(
                    customerPic = customerPicUri?.toString() ?: "",
                    customerId = System.currentTimeMillis(),
                    customerName = customerName,
                    cnicNumber = customerCnic,
                    customerPhone = customerPhone,
                    constructionPlace = constructionPlace,
                    contractorName = contractorName,
                    contractorPhone = contractorPhone,
                    ownerName = ownerName,
                    ownerPhone = ownerPhone
                )
            }
        }
        return null
    }

    private fun validateInputs(): Boolean {
        return when {
            binding.etCustomerName.text.isNullOrEmpty() -> {
                showToast(requireContext(), getString(R.string.please_enter_customer_name))
                false
            }

            binding.etCnic.text.isNullOrEmpty() -> {
                showToast(requireContext(), getString(R.string.please_enter_customer_cnic))
                false
            }

            binding.etCustomerPhone.text.isNullOrEmpty() -> {
                showToast(requireContext(), getString(R.string.please_enter_customer_phone_no))
                false
            }

            binding.etConstructionPlace.text.isNullOrEmpty() -> {
                showToast(requireContext(), getString(R.string.please_enter_construction_place))

                false
            }

            binding.etContractorName.text.isNullOrEmpty() -> {
                showToast(requireContext(), getString(R.string.please_enter_contractor_name))
                false
            }

            binding.etContractorPhone.text.isNullOrEmpty() -> {
                showToast(requireContext(), getString(R.string.please_enter_contractor_phone_no))
                false
            }

            binding.etOwnerName.text.isNullOrEmpty() -> {
                showToast(requireContext(), getString(R.string.please_enter_owner_name))
                false
            }

            binding.etOwnerPhone.text.isNullOrEmpty() -> {
                showToast(requireContext(), getString(R.string.please_enter_owner_phone))
                false
            }

            else -> true
        }
    }

    private fun clearInputFields() {
        binding.apply {
            clearInputs(
                etCustomerName,
                etCnic,
                etCustomerPhone,
                etConstructionPlace,
                etContractorName,
                etContractorPhone,
                etOwnerName,
                etOwnerPhone
            )
        }
    }

    private fun setupSelectPicBtn() {
        binding.customerImage.setOnClickListener {
            showImageSourceOptions()
        }
    }

    private fun showImageSourceOptions() {
        // Open a dialog to let the user choose between Camera and Gallery
        val options = arrayOf("Take Photo", "Choose from Gallery")
        val builder = androidx.appcompat.app.AlertDialog.Builder(requireContext())
        builder.setItems(options) { _, which ->
            when (which) {
                0 -> openCamera()
                1 -> openGallery()
            }
        }
        builder.show()
    }

    private fun openCamera() {
        if (isPermissionGranted(Manifest.permission.CAMERA)) {
            // Open Camera
            val photoFile = createImageFile()
            customerPicUri = FileProvider.getUriForFile(
                requireContext(),
                "com.miassolutions.rentatool.fileprovider",
                photoFile
            )
            val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE).apply {
                putExtra(MediaStore.EXTRA_OUTPUT, customerPicUri)
            }
            cameraResultLauncher.launch(intent)
        } else {
            requestPermission(Manifest.permission.CAMERA, REQUEST_CODE_CAMERA_PERMISSION)
        }
    }

    private fun openGallery() {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        galleryResultLauncher.launch(intent)
    }

    // Camera and Gallery result handlers
    private val cameraResultLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                binding.customerImage.setImageURI(customerPicUri)
            }
        }

    private val galleryResultLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK && result.data != null) {
                customerPicUri = result.data?.data
                binding.customerImage.setImageURI(customerPicUri)
            }
        }

    private fun createImageFile(): File {
        val storageDir: File = requireContext().getExternalFilesDir(null)!!
        return File.createTempFile(
            "customer_pic_",  /* prefix */
            ".jpg",  /* suffix */
            storageDir /* directory */
        )
    }




    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}