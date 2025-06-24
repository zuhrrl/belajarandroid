package com.sobodigital.zulbelajarandroid.data.repository

import android.annotation.SuppressLint
import android.util.Log
import androidx.paging.Pager
import androidx.paging.PagingConfig
import com.google.gson.Gson
import com.sobodigital.zulbelajarandroid.data.Result
import com.sobodigital.zulbelajarandroid.data.local.LocalDataSource
import com.sobodigital.zulbelajarandroid.data.local.db.dao.StoryDao
import com.sobodigital.zulbelajarandroid.data.local.db.entity.StoryEntity
import com.sobodigital.zulbelajarandroid.data.model.ErrorResponse
import com.sobodigital.zulbelajarandroid.data.paging.StoryPagingDataSource
import com.sobodigital.zulbelajarandroid.data.remote.StoryRemoteDataSource
import com.sobodigital.zulbelajarandroid.domain.model.Story
import com.sobodigital.zulbelajarandroid.domain.model.UploadStoryData
import com.sobodigital.zulbelajarandroid.domain.model.UploadStorySession
import com.sobodigital.zulbelajarandroid.domain.repository.StoryRepository
import com.sobodigital.zulbelajarandroid.utils.DataMapper
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody

class StoryRepositoryImpl(
    private val storyRemoteDataSource: StoryRemoteDataSource,
    private val localDataSource: LocalDataSource<StoryEntity, StoryDao>
) : StoryRepository {

    override suspend fun fetchStories(locationType: Int): Result<List<Story>?>? {
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

                    val domain = data.listStory?.map { item ->
                        DataMapper.mapEntityToDomain(item, {
                            Story(
                                id = it.id,
                                name = it.name,
                                photoUrl = it.photoUrl,
                                lat = it.lat as Double,
                                lon = it.lon as Double
                            )
                        })
                    }
                    Result.Success(domain) }
            } catch (e: Exception) {
                val message = e.localizedMessage!!
                Result.Error(message)
            }
        }

    }

    override suspend fun fetchStoryWithPaging(): Result<Pager<Int, Story>> {
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

    override suspend fun fetchStoryById(id: String): Result<Story?>? {
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
                    val domain = DataMapper.mapEntityToDomain(data.story, {
                        Story(
                            id = it?.id,
                            name = it?.name,
                            photoUrl = it?.photoUrl
                        )
                    })
                    Result.Success(domain) }
            } catch (e: Exception) {
                val message = e.localizedMessage!!
                Result.Error(message)
            }
        }

    }

    override suspend fun uploadStory(param: UploadStoryData): Result<UploadStorySession?>? {
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
                    val domain = DataMapper.mapEntityToDomain(data, {
                        UploadStorySession(
                            error = it.error,
                            message = it.message
                        )
                    })
                    Result.Success(domain)
                }
            } catch (e: Exception) {
                val message = e.localizedMessage!!
                Result.Error(message)
            }
        }

    }

    override suspend fun getStoryFromDbById(id: String): Result<Story> {
        return withContext(Dispatchers.IO) {
            try {
                val storyEntity = localDataSource.getDao().getStoryById(id)
                Log.d(TAG, "Succces get data from DB $storyEntity")
                val domain = DataMapper.mapEntityToDomain(storyEntity, {
                    Story(id = it.storyId,
                        name = it.name,
                        description = it.description,
                        photoUrl = it.photoUrl
                    )
                })
                return@withContext Result.Success(domain)
            } catch (e: Exception) {
                val message = e.localizedMessage!!
                Result.Error(message)
            }
        }
    }

    override suspend fun bookmarkStory(data: Story) {
        Log.d(TAG, "Trying to bookmark story ${data.id}")
        return withContext(Dispatchers.IO) {
            val entity = DataMapper.mapEntityToDomain(data, {
                StoryEntity(
                    storyId = it.id,
                    name = it.name,
                    description = it.description,
                    photoUrl = it.photoUrl
                )
            })
            localDataSource.insert(entity)
        }

    }

    override suspend fun removeBookmarkById(id: String) {
        return withContext(Dispatchers.IO) {
            localDataSource.getDao().deleteById(id)
        }
    }

    companion object {
        private  val TAG = StoryRepositoryImpl::class.simpleName

        @SuppressLint("StaticFieldLeak")
        @Volatile
        private var instance: StoryRepositoryImpl? = null
        fun getInstance(
            storyRemoteDatasource: StoryRemoteDataSource,
            localDataSource: LocalDataSource<StoryEntity, StoryDao>
        ): StoryRepositoryImpl =
            instance ?: synchronized(this) {
                instance ?: StoryRepositoryImpl(
                    storyRemoteDatasource,
                    localDataSource)
            }.also { instance = it }
    }
}