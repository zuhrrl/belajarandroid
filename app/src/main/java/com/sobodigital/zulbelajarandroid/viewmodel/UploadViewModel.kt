package com.sobodigital.zulbelajarandroid.viewmodel

import android.content.Context
import android.net.Uri
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sobodigital.zulbelajarandroid.data.Result
import com.sobodigital.zulbelajarandroid.data.repository.LocalRepository
import com.sobodigital.zulbelajarandroid.utils.reduceFileImage
import com.sobodigital.zulbelajarandroid.utils.uriToFile
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File

class UploadViewModel(private val localRepository: LocalRepository): ViewModel() {

    private val _fileFromGallery = MutableLiveData<File?>()
    val fileFromGallery = _fileFromGallery

    private val _fileFromCamera = MutableLiveData<File?>()
    val fileFromCamera = _fileFromCamera

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _errorMessage = MutableLiveData<String>()
    val errorMessage: LiveData<String> = _errorMessage

    fun setLoading(status: Boolean) {
        Log.d(TAG, "Change status $status")
        _isLoading.value = status
    }

    fun onGalleryImagePicked(uri: Uri?) {
        _isLoading.value = true
        viewModelScope.launch {
            when(val result = localRepository.reduceImageSize(uri)) {
                is Result.Error -> {
                    _isLoading.value = false
                    _errorMessage.value = "Error: ${result.error}"
                }
                is Result.Success -> {
                    _isLoading.value = false
                    _fileFromGallery.value = result.data
                }
            }
        }
    }

    fun onCameraImagePicked(uri: Uri?) {
        _isLoading.value = true
        viewModelScope.launch {
            when(val result = localRepository.reduceImageSize(uri)) {
                is Result.Error -> {
                    _isLoading.value = false
                    _errorMessage.value = "Error: ${result.error}"
                }
                is Result.Success -> {
                    _isLoading.value = false
                    _fileFromGallery.value = result.data
                }
            }
        }
    }

    companion object {
        private var TAG = UploadViewModel::class.java.simpleName
    }

}