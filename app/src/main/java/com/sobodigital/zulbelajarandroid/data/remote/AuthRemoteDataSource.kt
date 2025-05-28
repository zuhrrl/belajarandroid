package com.sobodigital.zulbelajarandroid.data.remote

import com.sobodigital.zulbelajarandroid.data.model.EventDetailResponse
import com.sobodigital.zulbelajarandroid.data.model.AuthParameter
import com.sobodigital.zulbelajarandroid.data.model.LoginResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path


interface AuthRemoteDataSource {

    @POST("login")
    suspend fun authWithEmail(
        @Body authParameter: AuthParameter
    ): Response<LoginResponse>

    @GET("events/{id}")
    suspend fun fetchEventById(
        @Path("id") id: Int
    ): Response<EventDetailResponse>

}