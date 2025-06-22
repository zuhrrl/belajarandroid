package com.sobodigital.zulbelajarandroid.presentation.viewmodel

import android.net.Uri
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sobodigital.zulbelajarandroid.data.Result
import com.sobodigital.zulbelajarandroid.data.model.UploadResponse
import com.sobodigital.zulbelajarandroid.data.model.UploadStoryParameter
import com.sobodigital.zulbelajarandroid.data.repository.LocalRepositoryImpl
import com.sobodigital.zulbelajarandroid.data.repository.StoryRepositoryImpl
import com.sobodigital.zulbelajarandroid.domain.usecase.MediaUseCase
import com.sobodigital.zulbelajarandroid.domain.usecase.StoryUseCase
import kotlinx.coroutines.launch
import java.io.File

class UploadViewModel(
    private val storyUseCase: StoryUseCase,
    private val mediaUseCase: MediaUseCase): ViewModel() {

    private val _fileFromGallery = MutableLiveData<File?>()
    val fileFromGallery = _fileFromGallery

    private val _fileFromCamera = MutableLiveData<File?>()
    val fileFromCamera = _fileFromCamera

    private val _uploadStoryResponse = MutableLiveData<UploadResponse>()
    val uploadStoryResponse = _uploadStoryResponse

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _errorMessage = MutableLiveData<String>()
    val errorMessage: LiveData<String> = _errorMessage

    fun onGalleryImagePicked(uri: Uri?) {
        _isLoading.value = true
        viewModelScope.launch {
            when(val result = mediaUseCase.reduceImageSize(uri)) {
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
            when(val result = mediaUseCase.reduceImageSize(uri)) {
                is Result.Error -> {
                    _isLoading.value = false
                    _errorMessage.value = "Error: ${result.error}"
                }
                is Result.Success -> {
                    _isLoading.value = false
                    _fileFromCamera.value = result.data
                }
            }
        }
    }

    fun uploadStory(param: UploadStoryParameter) {
        viewModelScope.launch {
            _errorMessage.value = ""
            _isLoading.value = true
            when(val response = storyUseCase.uploadStory(param)) {
                is Result.Error -> {
                    _isLoading.value = false
                    _errorMessage.value = "Error: ${response.error}"
                }

                is Result.Success -> {
                    _isLoading.value = false
                    _uploadStoryResponse.value = response.data
                }

                null -> {
                    _isLoading.value = false
                    _errorMessage.value = "Error: Unimplemented!"

                }
            }
        }
    }

    companion object {
        private var TAG = UploadViewModel::class.java.simpleName
    }

}