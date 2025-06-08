package com.sobodigital.zulbelajarandroid.data.remote

import com.sobodigital.zulbelajarandroid.data.model.StoriesResponse
import com.sobodigital.zulbelajarandroid.data.model.StoryResponse
import com.sobodigital.zulbelajarandroid.data.model.UploadResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Path
import retrofit2.http.Query

interface StoryRemoteDataSource {

    @GET("v1/stories")
    suspend fun fetchStories(
        @Query("location") location : Int = 0,
        ): Response<StoriesResponse>


    @GET("v1/stories/{id}")
    suspend fun fetchStoryById(
        @Path("id") id: String
    ): Response<StoryResponse>

    @Multipart
    @POST("v1/stories")
    suspend fun uploadStory(
        @Part file: MultipartBody.Part,
        @Part("description") description: RequestBody,
    ): Response<UploadResponse>

}