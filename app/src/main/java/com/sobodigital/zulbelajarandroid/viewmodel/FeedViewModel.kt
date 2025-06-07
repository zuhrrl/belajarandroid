package com.sobodigital.zulbelajarandroid.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asFlow
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import androidx.paging.liveData
import com.sobodigital.zulbelajarandroid.data.Result
import com.sobodigital.zulbelajarandroid.data.model.Story
import com.sobodigital.zulbelajarandroid.data.repository.LocalRepository
import com.sobodigital.zulbelajarandroid.data.repository.StoryRepository
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class FeedViewModel(private val repository: StoryRepository, private val localRepository: LocalRepository) : ViewModel() {
    private val _listEvent = MutableLiveData<List<Story>>()
    val listEvent = _listEvent

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _pagerData = MutableLiveData<PagingData<Story>>()
    val pagerData: LiveData<PagingData<Story>> = _pagerData

    private val _errorData = MutableLiveData<Result.Error?>()
    val errorData = _errorData

    fun clearAllSetting() {
        viewModelScope.launch {
            localRepository.clearAllPreference()
        }
    }

    fun setLoading(status: Boolean) {
        Log.d(TAG, "Change status $status")
        _isLoading.value = status
    }

    fun fetchStory() {
        viewModelScope.launch {
            _errorData.value = Result.Error("")
            _isLoading.value = true
            when(val response = repository.fetchStories()) {
                is Result.Error -> {
                    _isLoading.value = false
                    _errorData.value = response
                }
                is Result.Success -> {
                    _isLoading.value = false
                    _listEvent.value = response.data
                }
                null -> {
                    _isLoading.value = false
                    _errorData.value = Result.Error("Error: Unimplemented!")
                }
            }
        }

    }

    fun fetchStoryWithPaging() {
        viewModelScope.launch {
            _errorData.value = Result.Error("")
            _isLoading.value = true
            when(val response = repository.fetchStoryWithPaging()) {
                is Result.Error -> {
                    _isLoading.value = false
                    _errorData.value = response
                }
                is Result.Success -> {
                    _isLoading.value = false
                    response.data.liveData
                        .asFlow()
                        .cachedIn(viewModelScope)
                        .collectLatest { data ->
                            _pagerData.value = data
                        }

                }
                null -> {
                    _isLoading.value = false
                    _errorData.value = Result.Error("Error: Unimplemented!")
                }
            }
        }

    }

    companion object{
        private var TAG = FeedViewModel::class.java.simpleName
    }
}