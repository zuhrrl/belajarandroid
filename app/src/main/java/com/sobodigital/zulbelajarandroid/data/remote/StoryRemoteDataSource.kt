package com.sobodigital.zulbelajarandroid.data.remote

import com.sobodigital.zulbelajarandroid.data.model.EventDetailResponse
import com.sobodigital.zulbelajarandroid.data.model.StoriesResponse
import com.sobodigital.zulbelajarandroid.data.model.StoryResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface StoryRemoteDataSource {

    @GET("v1/stories")
    suspend fun fetchStories(
    ): Response<StoriesResponse>

    @GET("v1/stories/{id}")
    suspend fun fetchStoryById(
        @Path("id") id: String
    ): Response<StoryResponse>

}