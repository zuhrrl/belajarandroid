package com.sobodigital.zulbelajarandroid.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sobodigital.zulbelajarandroid.data.Result
import com.sobodigital.zulbelajarandroid.data.model.EventItem
import com.sobodigital.zulbelajarandroid.data.repository.EventRepository
import kotlinx.coroutines.launch

class FavoriteEventViewModel(private val repository: EventRepository) : ViewModel() {
    private val _listEvent = MutableLiveData<List<EventItem>>()
    val listEvent = _listEvent

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _errorMessage = MutableLiveData<String>()
    val errorMessage: LiveData<String> = _errorMessage

    companion object{
        private val TAG = FavoriteEventViewModel::class.simpleName
    }

    fun fetchFavoriteEvent() {
        viewModelScope.launch {
            _errorMessage.value = ""
            _isLoading.value = true
            when(val response = repository.getFavoriteEventFromDb()) {
                is Result.Error -> {
                    _isLoading.value = false
                    _errorMessage.value = "Error: ${response.error}"
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