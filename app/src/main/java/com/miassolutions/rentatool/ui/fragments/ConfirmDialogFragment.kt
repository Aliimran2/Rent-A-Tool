package com.miassolutions.rentatool.ui.fragments

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.miassolutions.rentatool.MyApplication
import com.miassolutions.rentatool.R
import com.miassolutions.rentatool.core.utils.helper.formattedDate
import com.miassolutions.rentatool.data.model.Customer
import com.miassolutions.rentatool.databinding.FragmentConfirmDialogBinding
import com.miassolutions.rentatool.ui.adapters.SelectedToolListAdapter
import com.miassolutions.rentatool.ui.viewmodels.SharedViewModel
import com.miassolutions.rentatool.ui.viewmodels.SharedViewModelFactory
import java.util.Date

class ConfirmDialogFragment : Fragment(R.layout.fragment_confirm_dialog) {


    companion object {
        private const val TAG = "ConfirmDialogFragment"
    }

    private var _binding: FragmentConfirmDialogBinding? = null
    private val binding get() = _binding!!

    private val rentalViewModel: SharedViewModel by activityViewModels {
        SharedViewModelFactory((requireActivity().application as MyApplication).repository)
    }

    private lateinit var selectedToolListAdapter: SelectedToolListAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentConfirmDialogBinding.bind(view)

        setupUI()
        observeViewModel()

        binding.printReceipt.setOnClickListener {
//            captureScreenshotExcludingButton(binding.root)
            val action = ConfirmDialogFragmentDirections.actionConfirmDialogFragmentToStockFragment()
            findNavController().navigate(action)


        }
    }

    private fun setupUI() {

        selectedToolListAdapter = SelectedToolListAdapter()
        binding.rVRentedToolsList.adapter = selectedToolListAdapter

    }

    private fun observeViewModel() {
        rentalViewModel.customer.observe(viewLifecycleOwner) { customer ->
            customer?.let {
                updateCustomerUI(customer)
            }
        }

        rentalViewModel.allTools.observe(viewLifecycleOwner) { tools ->
            selectedToolListAdapter.updateToolsList(tools)
        }

        rentalViewModel.selectedTools.observe(viewLifecycleOwner) { selectedTools ->
            selectedToolListAdapter.submitList(selectedTools)
        }

        rentalViewModel.estimatedReturnDate.observe(viewLifecycleOwner){ estimatedReturnDate ->
            val dateToDisplay = if(estimatedReturnDate > 0) Date(estimatedReturnDate) else Date()
            binding.transExpectDate.text = formattedDate(dateToDisplay)

        }
    }


    private fun updateCustomerUI(selectedCustomer: Customer) {


        binding.apply {
            customerName.text = selectedCustomer.customerName
            customerPhone.text = selectedCustomer.customerPhone


        }
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    // Capture screenshot excluding the button
//    private fun captureScreenshotExcludingButton(fragmentView: View) {
//        // Temporarily hide the button
//        binding.printReceipt.visibility = View.GONE
//
//        // Capture the screenshot of the fragment
//        val screenshot = captureScreenshot(fragmentView)
//
//        // Restore the button visibility
//        binding.printReceipt.visibility = View.VISIBLE
//
//        // Save the screenshot and share it
//        val screenshotFile = saveScreenshotToFile(screenshot)
//        shareScreenshot(screenshotFile)
//    }
//
//    private fun captureScreenshot(view: View): Bitmap {
//        // Create a Bitmap to hold the screenshot
//        val bitmap = Bitmap.createBitmap(view.width, view.height, Bitmap.Config.ARGB_8888)
//
//        // Create a Canvas to draw the Bitmap
//        val canvas = Canvas(bitmap)
//
//        // Draw the view onto the canvas
//        view.draw(canvas)
//
//        return bitmap
//    }
//
//    private fun saveScreenshotToFile(bitmap: Bitmap): File {
//        // Optional: Reduce bitmap resolution if it's too large
//        val maxWidth = 1024 // Max width
//        val maxHeight = 1024 // Max height
//
//        val scaledBitmap = if (bitmap.width > maxWidth || bitmap.height > maxHeight) {
//            val scaleFactor = Math.min(maxWidth.toFloat() / bitmap.width, maxHeight.toFloat() / bitmap.height)
//            val scaledWidth = (bitmap.width * scaleFactor).toInt()
//            val scaledHeight = (bitmap.height * scaleFactor).toInt()
//            Bitmap.createScaledBitmap(bitmap, scaledWidth, scaledHeight, false)
//        } else {
//            bitmap
//        }
//
//        // Define the output file
//        val file = File(requireContext().getExternalFilesDir(Environment.DIRECTORY_PICTURES), "fragment_screenshot.webp")
//
//        try {
//            // Use WebP compression instead of PNG or JPEG
//            val outputStream = FileOutputStream(file)
//            scaledBitmap.compress(Bitmap.CompressFormat.WEBP, 100, outputStream)
//            outputStream.flush()
//            outputStream.close()
//        } catch (e: IOException) {
//            e.printStackTrace()
//        }
//
//        // Recycle the bitmap if not needed anymore
//        if (scaledBitmap != bitmap) {
//            scaledBitmap.recycle()
//        }
//
//        return file
//    }
//
//
//
//
//    private fun shareScreenshot(file: File) {
//        val uri = FileProvider.getUriForFile(
//            requireContext(),
//            "${requireContext().packageName}.provider",
//            file
//        )
//
//        val shareIntent = Intent(Intent.ACTION_SEND).apply {
//            type = "image/*"
//            putExtra(Intent.EXTRA_STREAM, uri)
//            addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
//        }
//
//
//        startActivity(Intent.createChooser(shareIntent, "Share Screenshot"))
//    }
}
