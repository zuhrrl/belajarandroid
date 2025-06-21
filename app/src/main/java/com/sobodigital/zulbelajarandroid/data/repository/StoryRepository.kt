package com.sobodigital.zulbelajarandroid.data.repository

import android.annotation.SuppressLint
import android.util.Log
import androidx.paging.Pager
import androidx.paging.PagingConfig
import com.google.gson.Gson
import com.sobodigital.zulbelajarandroid.data.Result
import com.sobodigital.zulbelajarandroid.data.model.ErrorResponse
import com.sobodigital.zulbelajarandroid.data.model.Story
import com.sobodigital.zulbelajarandroid.data.model.UploadResponse
import com.sobodigital.zulbelajarandroid.data.model.UploadStoryParameter
import com.sobodigital.zulbelajarandroid.data.paging.StoryPagingDataSource
import com.sobodigital.zulbelajarandroid.data.remote.StoryRemoteDataSource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody

class StoryRepository(
    private val storyRemoteDataSource: StoryRemoteDataSource) {


    suspend fun fetchStories(locationType: Int = 0): Result<List<Story>?>? {
        return withContext(Dispatchers.IO) {
            try {
                val response = storyRemoteDataSource.fetchStories(locationType)
                if(!response.isSuccessful) {
                    val errorJsonString = response.errorBody()?.string()
                    val error = Gson().fromJson(errorJsonString, ErrorResponse::class.java)
                    Log.e(TAG, "Error fetch: ${error}")
                    val unauthorizedCodes = listOf(401, 403, 419, 415)

                    if(response.code() in unauthorizedCodes) {
                        return@withContext Result.Error(error.message ?: "Unknown error!", response.code())
                    }
                    return@withContext Result.Error(response.errorBody().toString(), response.code())
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

    suspend fun fetchStoryWithPaging(): Result<Pager<Int, Story>> {
        Log.d(TAG, "fetch story with paging from repository")

        return withContext(Dispatchers.IO) {
            try {
               val pager = Pager(
                    config = PagingConfig(
                        pageSize = 5
                    ),
                    pagingSourceFactory = {
                        StoryPagingDataSource(storyRemoteDataSource)
                    }
                )

                Result.Success(pager)
            } catch (e: Exception) {
                val message = e.localizedMessage!!
                Log.e(TAG, "HARUSNYA ERROR DISINI $e")
                Result.Error(message)
            }
        }
    }

    suspend fun fetchStoryById(id: String): Result<Story?>? {
        return withContext(Dispatchers.IO) {
            try {
                val response = storyRemoteDataSource.fetchStoryById(id)
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
                    Result.Success(data.story) }
            } catch (e: Exception) {
                val message = e.localizedMessage!!
                Result.Error(message)
            }
        }

    }

    suspend fun uploadStory(param: UploadStoryParameter): Result<UploadResponse?>? {
        val description = param.description?.toRequestBody("text/plain".toMediaType())
        val requestImageFile = param.file?.asRequestBody("image/jpeg".toMediaType())
        val photoMultipartBody = requestImageFile?.let {
            MultipartBody.Part.createFormData(
                "photo",
                param.file.name,
                it
            )
        }
        return withContext(Dispatchers.IO) {
            try {
                val response = storyRemoteDataSource.uploadStory(photoMultipartBody!!, description!!)
                if(!response.isSuccessful) {
                    val errorJsonString = response.errorBody()?.string()
                    val error = Gson().fromJson(errorJsonString, ErrorResponse::class.java)
                    Log.e(TAG, "Error fetch: ${response}")
                    val unauthorizedCodes = listOf(401,404, 403, 419, 415)

                    if(response.code() in unauthorizedCodes) {
                        return@withContext Result.Error(error.message ?: "Unknown error!")
                    }
                    return@withContext Result.Error(response.errorBody().toString())
                }
                Log.d(TAG, response.body().toString())
                return@withContext response.body()?.let { data ->
                    Result.Success(data) }
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
            storyRemoteDatasource: StoryRemoteDataSource,
        ): StoryRepository =
            instance ?: synchronized(this) {
                instance ?: StoryRepository(
                    storyRemoteDatasource)
            }.also { instance = it }
    }
}