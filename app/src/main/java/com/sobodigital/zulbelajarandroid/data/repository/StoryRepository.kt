package com.sobodigital.zulbelajarandroid.data.repository

import android.annotation.SuppressLint
import android.util.Log
import com.google.gson.Gson
import com.sobodigital.zulbelajarandroid.data.Result
import com.sobodigital.zulbelajarandroid.data.model.ErrorResponse
import com.sobodigital.zulbelajarandroid.data.model.Story
import com.sobodigital.zulbelajarandroid.data.remote.StoryRemoteDatasource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.Retrofit

class StoryRepository(private val storyRemoteDatasource: StoryRemoteDatasource) {

    suspend fun fetchStories(): Result<List<Story>?>? {
        return withContext(Dispatchers.IO) {
            try {
                val response = storyRemoteDatasource.fetchStories()
                if(!response.isSuccessful) {
                    val errorJsonString = response.errorBody()?.string()
                    val error = Gson().fromJson(errorJsonString, ErrorResponse::class.java)
                    Log.e(TAG, "Error fetch: ${response}")
                    val unauthorizedCodes = listOf(401, 403, 419, 415)

                    if(response.code() in unauthorizedCodes) {
                        return@withContext Result.Error(error.message ?: "Unknown error!")
                    }
                    return@withContext Result.Error(response.errorBody().toString())
                }
                Log.d(TAG, response.body().toString())
                return@withContext response.body()?.let { data ->
                    Result.Success(data.listStory) }
            } catch (e: Exception) {
                val message = e.localizedMessage!!
                Result.Error(message)
            }
        }

    }

    companion object {
        private  val TAG = StoryRepository::class.simpleName

        @SuppressLint("StaticFieldLeak")
        @Volatile
        private var instance: StoryRepository? = null
        fun getInstance(
            storyRemoteDatasource: StoryRemoteDatasource,
        ): StoryRepository =
            instance ?: synchronized(this) {
                instance ?: StoryRepository(storyRemoteDatasource)
            }.also { instance = it }
    }
}