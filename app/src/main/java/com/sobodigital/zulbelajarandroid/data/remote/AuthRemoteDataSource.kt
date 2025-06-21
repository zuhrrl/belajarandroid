package com.sobodigital.zulbelajarandroid.data.remote

import com.sobodigital.zulbelajarandroid.data.model.AuthParameter
import com.sobodigital.zulbelajarandroid.data.model.AuthResponse
import com.sobodigital.zulbelajarandroid.domain.model.AuthCredential
import com.sobodigital.zulbelajarandroid.data.model.RegisterParameter
import com.sobodigital.zulbelajarandroid.data.model.RegisterResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST


interface AuthRemoteDataSource {

    @POST("v1/login")
    suspend fun authWithEmail(
        @Body authParameter: AuthParameter
    ): Response<AuthResponse>

    @POST("v1/register")
    suspend fun register(
        @Body registerParameter: RegisterParameter
    ): Response<RegisterResponse>

}