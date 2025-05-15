package com.sobodigital.zulbelajarandroid.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sobodigital.zulbelajarandroid.data.Result
import com.sobodigital.zulbelajarandroid.data.model.EventItem
import com.sobodigital.zulbelajarandroid.data.repository.EventRepository
import kotlinx.coroutines.launch

class EventMainViewModel constructor(private val repository: EventRepository) : ViewModel() {
    private val _listEvent = MutableLiveData<List<EventItem>>()
    val listEvent = _listEvent

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _errorMessage = MutableLiveData<String>()
    val errorMessage: LiveData<String> = _errorMessage

    companion object{
        private const val TAG = "MainViewModel"
    }

    fun fetchListEvent(active: Int) {
        viewModelScope.launch {
            _errorMessage.value = ""
            _isLoading.value = true
            when(val response = repository.fetchListEvent(active)) {
                is Result.Error -> {
                    _isLoading.value = false
                    _errorMessage.value = "Error: $response"
                }
                Result.Loading -> {
                    _isLoading.value = true
                }
                is Result.Success -> {
                    _isLoading.value = false
                    _listEvent.value = response.data
                }
            }
        }

    }
}