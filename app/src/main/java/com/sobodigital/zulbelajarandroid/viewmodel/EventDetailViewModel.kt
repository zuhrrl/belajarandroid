package com.sobodigital.zulbelajarandroid.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.sobodigital.zulbelajarandroid.data.model.EventDetailResponse
import com.sobodigital.zulbelajarandroid.data.model.EventItem
import com.sobodigital.zulbelajarandroid.data.remote.ApiConfig
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class EventDetailViewModel : ViewModel() {
    private val _event = MutableLiveData<EventItem>()
    val event = _event

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    companion object{
        private const val TAG = "DetailEventViewModel"
    }

    fun fetchEventById(id: Int) {
        _isLoading.value = true
        val client = ApiConfig.getApiService().fetchEventById(id)
        client.enqueue(object : Callback<EventDetailResponse> {
            override fun onResponse(
                call: Call<EventDetailResponse>,
                response: Response<EventDetailResponse>
            ) {
                _isLoading.value = false
                if (response.isSuccessful) {
                    event.value = response.body()?.event
                } else {
                    Log.e(TAG, "onFailure: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<EventDetailResponse>, t: Throwable) {
                _isLoading.value = false
                Log.e(TAG, "onFailure: ${t.message.toString()}")
            }
        })
    }
}