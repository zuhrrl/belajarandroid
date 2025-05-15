package com.sobodigital.zulbelajarandroid.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sobodigital.zulbelajarandroid.data.model.EventDetailResponse
import com.sobodigital.zulbelajarandroid.data.model.EventItem
import com.sobodigital.zulbelajarandroid.data.remote.ApiConfig
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class EventDetailViewModel : ViewModel() {
    private val _event = MutableLiveData<EventItem>()
    val event = _event

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _errorMessage = MutableLiveData<String>()
    val errorMessage: LiveData<String> = _errorMessage

    companion object{
        private const val TAG = "DetailEventViewModel"
    }

     fun fetchEventById(id: Int) {
         viewModelScope.launch {
             _errorMessage.value = ""
             _isLoading.value = true
             try {
                 val response = ApiConfig.getApiService().fetchEventById(id)
                 _isLoading.value = false
                 if (response.isSuccessful) {
                     event.value = response.body()?.event
                     return@launch
                 }
                 Log.e(TAG, "onFailure: ${response.message()}")
             }
             catch (err: Exception) {
                 _isLoading.value = false
                 _errorMessage.value = "Error: ${err.message.toString()}"
                 Log.e(TAG, "onFailure: ${err.message.toString()}")
             }

         }
    }
}