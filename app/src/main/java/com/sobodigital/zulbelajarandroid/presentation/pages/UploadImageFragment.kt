package com.sobodigital.zulbelajarandroid.presentation.pages

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
import com.sobodigital.zulbelajarandroid.domain.model.UploadStoryData
import com.sobodigital.zulbelajarandroid.presentation.viewmodel.UploadViewModel
import com.sobodigital.zulbelajarandroid.presentation.viewmodel.UploadViewModelFactory
import com.sobodigital.zulbelajarandroid.utils.getImageUri
import java.io.File

class UploadImageFragment : Fragment() {
    private lateinit var viewModel: UploadViewModel
    private var uriImageFromCamera: Uri? = null
    private var selectedImage: File? = null

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
            viewModel.onGalleryImagePicked(uri)
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
            viewModel.onCameraImagePicked(uriImageFromCamera)
            return@registerForActivityResult
        }
        Log.d(TAG, "No media selected")
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val factory: UploadViewModelFactory = UploadViewModelFactory.getInstance(requireContext())
        val uploadViewModel: UploadViewModel by viewModels { factory }
        viewModel = uploadViewModel
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = UploadImageFragmentBinding.inflate(layoutInflater)

        viewModel.fileFromGallery.observe(viewLifecycleOwner) {file ->
            if(file != null) {
                selectedImage = file
                Glide.with(requireContext()).load(file).into(binding.imagePreview)
            }
        }

        viewModel.fileFromCamera.observe(viewLifecycleOwner) {file ->
            if(file != null) {
                selectedImage = file
                Glide.with(requireContext()).load(file).into(binding.imagePreview)
            }
        }

        viewModel.isLoading.observe(viewLifecycleOwner) {isLoading ->
            if(isLoading) {
                binding.loading.visibility = View.VISIBLE
                return@observe
            }
            binding.loading.visibility = View.GONE
            return@observe
        }

        viewModel.errorMessage.observe(viewLifecycleOwner) {message ->
            Log.d(TAG, message)
            if(message.isNotEmpty()) {
                binding.errorMessage.visibility = View.VISIBLE
                binding.errorMessage.text = message
                return@observe
            }
            binding.errorMessage.visibility = View.GONE
            return@observe
        }

        viewModel.uploadStoryResponse.observe(viewLifecycleOwner) {data ->
            if(data.error!!) {
                return@observe
            }
            Toast.makeText(requireContext(), data.message, Toast.LENGTH_SHORT).show()
            requireActivity().onBackPressed()
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

        binding.btnUploadStory.setOnClickListener {
            val description = binding.edDescription.text.toString().trim()
            selectedImage.let { file ->
                val param = UploadStoryData(file = file, description = description)
                viewModel.uploadStory(param)
            }
        }

        return binding.root
    }

    companion object {
        private var TAG = UploadImageFragment::class.java.simpleName
        private const val PERMISSION_CAMERA = Manifest.permission.CAMERA
    }
}