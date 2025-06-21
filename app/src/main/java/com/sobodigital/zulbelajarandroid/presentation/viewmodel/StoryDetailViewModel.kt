package com.sobodigital.zulbelajarandroid.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sobodigital.zulbelajarandroid.data.Result
import com.sobodigital.zulbelajarandroid.data.model.Story
import com.sobodigital.zulbelajarandroid.data.repository.StoryRepository
import kotlinx.coroutines.launch

class StoryDetailViewModel(private val repository: StoryRepository) : ViewModel() {
    private val _story = MutableLiveData<Story>()
    val story = _story

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _errorMessage = MutableLiveData<String>()
    val errorMessage: LiveData<String> = _errorMessage

     fun fetchEventById(id: String) {
         viewModelScope.launch {
             _errorMessage.value = ""
             _isLoading.value = true
             when(val response = repository.fetchStoryById(id)) {
                 is Result.Error -> {
                     _isLoading.value = false
                     _errorMessage.value = "Error: ${response.error}"
                 }

                 is Result.Success -> {
                     _isLoading.value = false
                     _story.value = response.data
                 }

                 null -> {
                     _isLoading.value = false
                     _errorMessage.value = "Error: Unimplemented!"
                 }
             }
         }
    }

    companion object{
        private var TAG = StoryDetailViewModel::class.java.simpleName
    }
}