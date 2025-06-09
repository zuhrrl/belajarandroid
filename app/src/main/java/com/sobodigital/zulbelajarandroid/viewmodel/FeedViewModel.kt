package com.sobodigital.zulbelajarandroid.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import androidx.paging.liveData
import com.sobodigital.zulbelajarandroid.data.Result
import com.sobodigital.zulbelajarandroid.data.model.Story
import com.sobodigital.zulbelajarandroid.data.repository.LocalRepository
import com.sobodigital.zulbelajarandroid.data.repository.StoryRepository
import kotlinx.coroutines.launch

class FeedViewModel(private val repository: StoryRepository, private val localRepository: LocalRepository) : ViewModel() {
    private val _listEvent = MutableLiveData<List<Story>>()
    val listEvent = _listEvent

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _pagerData = MutableLiveData<PagingData<Story>>()
    val pagerData: LiveData<PagingData<Story>> = _pagerData

    private val _pagingData = MutableLiveData<LiveData<PagingData<Story>>>()
    val pagingData: LiveData<LiveData<PagingData<Story>>> = _pagingData

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

    fun setErrorData(result: Result.Error) {
        _errorData.value = result
    }

    fun fetchStoryWithPaging() {
        Log.d(TAG, "fetch story with paging")
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
                    val dataSourceResponse = response.data.liveData
                        .cachedIn(viewModelScope)
                    _pagingData.value = dataSourceResponse

                }

            }
        }

    }

//    fun getStories() : LiveData<PagingData<Story>> {
//        return _pagingData.value?.cachedIn(viewModelScope) ?: MutableLiveData()
//    }

    companion object{
        private var TAG = FeedViewModel::class.java.simpleName
    }
}