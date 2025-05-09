package com.sobodigital.zulbelajarandroid.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.sobodigital.zulbelajarandroid.data.model.EventItem
import com.sobodigital.zulbelajarandroid.data.model.EventResponse
import com.sobodigital.zulbelajarandroid.data.remote.ApiConfig
import retrofit2.Callback
import retrofit2.Call
import retrofit2.Response

class EventMainViewModel : ViewModel() {
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
        _errorMessage.value = ""
        _isLoading.value = true
        val client = ApiConfig.getApiService().fetchEvents(active)
        client.enqueue(object : Callback<EventResponse> {
            override fun onResponse(
                call: Call<EventResponse>,
                response: Response<EventResponse>
            ) {
                _isLoading.value = false
                if (response.isSuccessful) {
                    _listEvent.value = response.body()?.listEvents
                } else {
                    Log.e(TAG, "onFailure: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<EventResponse>, t: Throwable) {
                _isLoading.value = false
                _errorMessage.value = "Error: ${t.message.toString()}"
                Log.e(TAG, "onFailure: ${t.message.toString()}")
            }
        })
    }
}