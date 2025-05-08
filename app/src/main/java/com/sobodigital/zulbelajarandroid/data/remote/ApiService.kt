package com.sobodigital.zulbelajarandroid.data.remote

import com.sobodigital.zulbelajarandroid.data.model.EventDetailResponse
import com.sobodigital.zulbelajarandroid.data.model.EventResponse
import retrofit2.Call
import retrofit2.http.*

interface ApiService {

    @GET("events")
    fun fetchEvents(
        @Query("active") active: Int
    ): Call<EventResponse>

    @GET("events/{id}")
    fun fetchEventById(
        @Path("id") id: Int
    ): Call<EventDetailResponse>


}