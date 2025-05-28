package com.sobodigital.zulbelajarandroid.data.remote

import com.sobodigital.zulbelajarandroid.data.model.EventDetailResponse
import com.sobodigital.zulbelajarandroid.data.model.StoriesResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface StoryRemoteDatasource {

    @GET("v1/stories")
    suspend fun fetchStories(
    ): Response<StoriesResponse>

    @GET("events/{id}")
    suspend fun fetchEventById(
        @Path("id") id: Int
    ): Response<EventDetailResponse>

}