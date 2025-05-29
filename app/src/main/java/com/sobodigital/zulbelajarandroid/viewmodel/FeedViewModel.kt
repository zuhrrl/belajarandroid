package com.sobodigital.zulbelajarandroid.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sobodigital.zulbelajarandroid.data.Result
import com.sobodigital.zulbelajarandroid.data.model.EventItem
import com.sobodigital.zulbelajarandroid.data.model.Story
import com.sobodigital.zulbelajarandroid.data.repository.EventRepository
import com.sobodigital.zulbelajarandroid.data.repository.StoryRepository
import kotlinx.coroutines.launch

class FeedViewModel(private val repository: StoryRepository) : ViewModel() {
    private val _listEvent = MutableLiveData<List<Story>>()
    val listEvent = _listEvent

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _errorMessage = MutableLiveData<String>()
    val errorMessage: LiveData<String> = _errorMessage

    fun fetchStory() {
        viewModelScope.launch {
            _errorMessage.value = ""
            _isLoading.value = true
            when(val response = repository.fetchStories()) {
                is Result.Error -> {
                    _isLoading.value = false
                    _errorMessage.value = "Error: ${response.error}"
                }
                is Result.Success -> {
                    _isLoading.value = false
                    _listEvent.value = response.data
                }
                null -> {
                    _isLoading.value = false
                    _errorMessage.value = "Error: Unimplemented!"
                }
            }
        }

    }

    companion object{
        private var TAG = FeedViewModel::class.java.simpleName
    }
}