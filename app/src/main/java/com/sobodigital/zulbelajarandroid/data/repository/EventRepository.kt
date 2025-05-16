package com.sobodigital.zulbelajarandroid.data.repository

import android.annotation.SuppressLint
import android.util.Log
import com.sobodigital.zulbelajarandroid.data.Result
import com.sobodigital.zulbelajarandroid.data.local.LocalDataSource
import com.sobodigital.zulbelajarandroid.data.local.db.entity.EventEntity
import com.sobodigital.zulbelajarandroid.data.model.EventItem
import com.sobodigital.zulbelajarandroid.data.remote.ApiService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class EventRepository(private val apiService: ApiService, private val localDataSource: LocalDataSource) {

    suspend fun getEventFromDbById(id: Int): Result<EventItem> {
        return withContext(Dispatchers.IO) {
            try {
                val event = localDataSource.getEventById(id)
                Log.d("EventRepository", "Trying to get event from db: $event")
                return@withContext Result.Success(EventItem(id = event.eventId))
            } catch (e: Exception) {
                val message = e.localizedMessage!!
                Result.Error(message)
            }
        }

    }

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

    suspend fun getFavoriteEventFromDb(): Result<List<EventItem>> {
        val tempList: MutableList<EventItem> = mutableListOf()
        return withContext(Dispatchers.IO) {
            try {
                val events = localDataSource.getAllEvent()
                Log.d(TAG, "Favorite data from db ${events}")

                if(events.isEmpty()) {
                    return@withContext Result.Success(listOf())
                }

                for (item: EventEntity in events) {

                    val eventItem = EventItem(id = item.eventId, name = item.name,
                        ownerName = item.ownerName,
                        beginTime = item.beginTime,
                        registrants = item.registrants,
                        quota = item.quota,
                        imageLogo = item.imageLogo,
                        link = item.link,
                        description = item.description
                    )
                    tempList.add(eventItem)
                }
                Log.d(TAG, "Favorite data ${tempList}")

                return@withContext  Result.Success(tempList)
            } catch (e: Exception) {
                val message = e.localizedMessage!!
                Result.Error(message)
            }
        }

    }

    fun bookmarkEvent(event: EventItem) {
        val entity = EventEntity(eventId = event.id, name = event.name,
            ownerName = event.ownerName, beginTime = event.beginTime,
            registrants = event.registrants, imageLogo = event.imageLogo, link = event.link,
            description = event.description, quota = event.quota)
        localDataSource.insert(entity)
    }

    fun removeBookmarkById(id: Int) {
        localDataSource.deleteById(id)
    }


    companion object {
        private  val TAG = EventRepository::class.simpleName

        @SuppressLint("StaticFieldLeak")
        @Volatile
        private var instance: EventRepository? = null
        fun getInstance(
            apiService: ApiService,
            localDataSource: LocalDataSource
        ): EventRepository =
            instance ?: synchronized(this) {
                instance ?: EventRepository(apiService, localDataSource)
            }.also { instance = it }
    }
}