package com.sobodigital.zulbelajarandroid.ui.pages

import android.Manifest
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.bumptech.glide.Glide
import com.sobodigital.zulbelajarandroid.databinding.UploadImageFragmentBinding
import com.sobodigital.zulbelajarandroid.utils.getImageUri
import com.sobodigital.zulbelajarandroid.viewmodel.FeedViewModel
import com.sobodigital.zulbelajarandroid.viewmodel.FeedViewModelFactory
import com.sobodigital.zulbelajarandroid.viewmodel.UploadViewModel

class UploadImageFragment : Fragment() {
    private lateinit var viewModel: UploadViewModel
    private var uriImageFromCamera: Uri? = null

    private val requestPermissionLauncher =
        registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { isGranted: Boolean ->
            if (isGranted) {
                Toast.makeText(requireContext(), "Permission request granted", Toast.LENGTH_LONG).show()
            } else {
                Toast.makeText(requireContext(), "Permission request denied", Toast.LENGTH_LONG).show()
            }
        }

    private fun isPermissionCameraGranted() =
        ContextCompat.checkSelfPermission(
            requireContext(),
            PERMISSION_CAMERA
        ) == PackageManager.PERMISSION_GRANTED

    private fun startGallery(launcher: ActivityResultLauncher<PickVisualMediaRequest>) {
        launcher.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
    }

    private val launcherGallery = registerForActivityResult(
            ActivityResultContracts.PickVisualMedia()
            ) { uri: Uri? ->
        if (uri != null) {
            viewModel.onGalleryImagePicked(uri, requireContext())
        } else {
            Log.d(TAG, "No media selected")
        }
    }

    private fun startCamera() {
        uriImageFromCamera = getImageUri(requireContext())
        launcherIntentCamera.launch(uriImageFromCamera!!)
    }

    private val launcherIntentCamera = registerForActivityResult(
        ActivityResultContracts.TakePicture()
    ) { isSuccess ->
        if (isSuccess && uriImageFromCamera != null) {
            viewModel.onGalleryImagePicked(uriImageFromCamera, requireContext())
            return@registerForActivityResult
        }
        Log.d(TAG, "No media selected")
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = UploadImageFragmentBinding.inflate(layoutInflater)

        val uploadViewModel: UploadViewModel by viewModels()
        viewModel = uploadViewModel

        viewModel.fileFromGallery.observe(viewLifecycleOwner) {file ->
            if(file != null) {
                Glide.with(this).load(file).into(binding.imagePreview)
            }
        }

        binding.btnFromGallery.setOnClickListener {
            startGallery(launcherGallery)
        }

        binding.btnFromCamera.setOnClickListener {
            if (!isPermissionCameraGranted()) {
                requestPermissionLauncher.launch(PERMISSION_CAMERA)
                return@setOnClickListener
            }
            startCamera()
        }

        return binding.root
    }

    companion object {
        private var TAG = UploadImageFragment::class.java.simpleName
        private const val PERMISSION_CAMERA = Manifest.permission.CAMERA
    }
}