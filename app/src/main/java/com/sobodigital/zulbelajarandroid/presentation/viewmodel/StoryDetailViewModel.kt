package com.sobodigital.zulbelajarandroid.presentation.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sobodigital.zulbelajarandroid.data.Result
import com.sobodigital.zulbelajarandroid.domain.model.Story
import com.sobodigital.zulbelajarandroid.domain.usecase.StoryUseCase
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class StoryDetailViewModel(private val storyUseCase: StoryUseCase) : ViewModel() {
    private val _story = MutableLiveData<Story>()
    val story = _story

    private val _isFavoriteLoading = MutableLiveData<Boolean>()
    val isFavoriteLoading: LiveData<Boolean> = _isFavoriteLoading

    private val _isFavorite = MutableLiveData<Boolean>()
    val isFavorite = _isFavorite

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _errorMessage = MutableLiveData<String>()
    val errorMessage: LiveData<String> = _errorMessage

    fun fetchStoryById(id: String) {
         viewModelScope.launch {
             _errorMessage.value = ""
             _isLoading.value = true
             when(val response = storyUseCase.fetchStoryById(id)) {
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

    fun checkIsFavoriteEvent(id: String) {
        viewModelScope.launch {
            when(storyUseCase.getStoryFromDbById(id)) {
                is Result.Error -> {
                    _isFavorite.value = false
                }
                is Result.Success -> {
                    _isFavorite.value = true
                }
            }
        }
    }

    fun bookmarkStory(story: Story) {
        _isFavoriteLoading.value = true
        viewModelScope.launch {
            if(isFavorite.value!!) {
                story.id?.let { storyUseCase.removeBookmarkById(it) }
                delay(300)
                story.id?.let { checkIsFavoriteEvent(it) }
                _isFavoriteLoading.value = false
                return@launch
            }
            storyUseCase.bookmarkStory(story)
            delay(300)
            story.id?.let { checkIsFavoriteEvent(it) }
            _isFavoriteLoading.value = false

        }
    }

    companion object{
        private var TAG = StoryDetailViewModel::class.java.simpleName
    }
}