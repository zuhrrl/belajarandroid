package com.sobodigital.zulbelajarandroid.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sobodigital.zulbelajarandroid.data.Result
import com.sobodigital.zulbelajarandroid.data.model.EventItem
import com.sobodigital.zulbelajarandroid.data.repository.EventRepository
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class EventDetailViewModel(private val repository: EventRepository) : ViewModel() {
    private val _event = MutableLiveData<EventItem>()
    val event = _event

    private val _isFavorite = MutableLiveData<Boolean>()
    val isFavorite = _isFavorite

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _isFavoriteLoading = MutableLiveData<Boolean>()
    val isFavoriteLoading: LiveData<Boolean> = _isFavoriteLoading

    private val _errorMessage = MutableLiveData<String>()
    val errorMessage: LiveData<String> = _errorMessage

    companion object{
        private const val TAG = "DetailEventViewModel"
    }

     fun fetchEventById(id: Int) {
         viewModelScope.launch {
             _errorMessage.value = ""
             _isLoading.value = true
             when(val response = repository.fetchEventById(id)) {
                 is Result.Error -> {
                     _isLoading.value = false
                     _errorMessage.value = "Error: ${response.error}"
                 }
                 Result.Loading -> {
                     _isLoading.value = true
                 }
                 is Result.Success -> {
                     _isLoading.value = false
                     _event.value = response.data
                 }
             }
         }
    }

    fun checkIsFavoriteEvent(id: Int) {
        viewModelScope.launch {
            when(repository.getEventFromDbById(id)) {
                is Result.Error -> {
                    _isFavorite.value = false
                }
                Result.Loading -> {
                    _isFavorite.value = false
                }
                is Result.Success -> {
                    _isFavorite.value = true
                }
            }
        }
    }

     fun bookmarkEvent(event: EventItem) {
         _isFavoriteLoading.value = true
         viewModelScope.launch {
             if(isFavorite.value!!) {
                 Log.d("EventDetailViewModel", "Trying to remove bookmark this event id: ${event.id}")
                 repository.removeBookmarkById(event.id!!)
                 delay(300)
                 checkIsFavoriteEvent(event.id)
                 _isFavoriteLoading.value = false
                 return@launch
             }
             Log.d("EventDetailViewModel", "Trying to bookmark this event id: ${event.id}")
             repository.bookmarkEvent(event)
             delay(300)
             checkIsFavoriteEvent(event.id!!)
             _isFavoriteLoading.value = false

         }


    }

}