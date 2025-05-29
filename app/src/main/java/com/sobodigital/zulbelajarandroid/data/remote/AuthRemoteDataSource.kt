package com.sobodigital.zulbelajarandroid.data.remote

import com.sobodigital.zulbelajarandroid.data.model.EventDetailResponse
import com.sobodigital.zulbelajarandroid.data.model.AuthParameter
import com.sobodigital.zulbelajarandroid.data.model.LoginResponse
import com.sobodigital.zulbelajarandroid.data.model.RegisterParameter
import com.sobodigital.zulbelajarandroid.data.model.RegisterResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path


interface AuthRemoteDataSource {

    @POST("v1/login")
    suspend fun authWithEmail(
        @Body authParameter: AuthParameter
    ): Response<LoginResponse>

    @POST("v1/register")
    suspend fun register(
        @Body registerParameter: RegisterParameter
    ): Response<RegisterResponse>

}