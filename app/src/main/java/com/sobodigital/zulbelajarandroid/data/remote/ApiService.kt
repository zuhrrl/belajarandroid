package com.sobodigital.zulbelajarandroid.data.remote

import com.sobodigital.zulbelajarandroid.data.model.AuthResponse
import com.sobodigital.zulbelajarandroid.data.model.EventDetailResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {

    @GET("events")
    suspend fun fetchEvents(
        @Query("active") active: Int
    ): Response<AuthResponse>

    @GET("events/{id}")
    suspend fun fetchEventById(
        @Path("id") id: Int
    ): Response<EventDetailResponse>

}