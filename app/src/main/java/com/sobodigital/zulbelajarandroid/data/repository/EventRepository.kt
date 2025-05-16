package com.sobodigital.zulbelajarandroid.data.repository

import android.util.Log
import com.sobodigital.zulbelajarandroid.data.Result
import com.sobodigital.zulbelajarandroid.data.model.EventItem
import com.sobodigital.zulbelajarandroid.data.remote.ApiService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class EventRepository(private val apiService: ApiService) {
   suspend fun fetchListEvent(active: Int): Result<List<EventItem>> {
         return withContext(Dispatchers.IO) {
            try {
                val response = apiService.fetchEvents(active)
                if(!response.isSuccessful) {
                    return@withContext Result.Error(response.errorBody().toString())
                }
                Log.d("EventRepository", response.body().toString())
                return@withContext response.body()?.let { list -> Result.Success(list.listEvents) }
            } catch (e: Exception) {
                val message = e.localizedMessage!!
                Result.Error(message)
            }
        } ?: Result.Error("Unknown error!")

   }

    suspend fun fetchEventById(eventId: Int): Result<EventItem?> {
        return withContext(Dispatchers.IO) {
            try {
                val response = apiService.fetchEventById(eventId)
                if(!response.isSuccessful) {
                    return@withContext Result.Error(response.errorBody().toString())
                }
                Log.d("EventRepository", response.body().toString())
                return@withContext response.body()?.let { data -> Result.Success(data.event) }
            } catch (e: Exception) {
                val message = e.localizedMessage!!
                Result.Error(message)
            }
        } ?: Result.Error("Unknown error!")

    }

    companion object {
        @Volatile
        private var instance: EventRepository? = null
        fun getInstance(
            apiService: ApiService,
        ): EventRepository =
            instance ?: synchronized(this) {
                instance ?: EventRepository(apiService)
            }.also { instance = it }
    }
}