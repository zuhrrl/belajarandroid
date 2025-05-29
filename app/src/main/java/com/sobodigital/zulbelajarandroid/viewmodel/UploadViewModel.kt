package com.sobodigital.zulbelajarandroid.viewmodel

import android.app.Application
import android.content.Context
import android.net.Uri
import android.util.Log
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.sobodigital.zulbelajarandroid.data.model.Story
import com.sobodigital.zulbelajarandroid.utils.reduceFileImage
import com.sobodigital.zulbelajarandroid.utils.uriToFile
import java.io.File

class UploadViewModel: ViewModel() {

    private val _fileFromGallery = MutableLiveData<File?>()
    val fileFromGallery = _fileFromGallery

    fun onGalleryImagePicked(uri: Uri?, context: Context) {
        val file = uri?.let { uriToFile(uri, context).reduceFileImage()}
        Log.d(TAG, "Uri file $uri")
        _fileFromGallery.value = file
    }

    fun onCameraImagePicked(uri: Uri?, context: Context) {
        val file = uri?.let { uriToFile(uri, context).reduceFileImage()}
        Log.d(TAG, "Uri file $uri")
        _fileFromGallery.value = file
    }

    companion object {
        private var TAG = UploadViewModel::class.java.simpleName
    }

}