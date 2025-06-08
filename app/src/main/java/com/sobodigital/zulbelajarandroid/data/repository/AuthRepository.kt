package com.sobodigital.zulbelajarandroid.data.repository

import android.annotation.SuppressLint
import android.util.Log
import com.google.gson.Gson
import com.sobodigital.zulbelajarandroid.data.Result
import com.sobodigital.zulbelajarandroid.data.local.SettingPreferences
import com.sobodigital.zulbelajarandroid.data.model.AuthParameter
import com.sobodigital.zulbelajarandroid.data.model.ErrorResponse
import com.sobodigital.zulbelajarandroid.data.model.LoginResponse
import com.sobodigital.zulbelajarandroid.data.model.RegisterParameter
import com.sobodigital.zulbelajarandroid.data.model.RegisterResponse
import com.sobodigital.zulbelajarandroid.data.remote.AuthRemoteDataSource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class AuthRepository(private val authRemoteDataSource: AuthRemoteDataSource, private val pref: SettingPreferences) {

    suspend fun authWithEmail(param: AuthParameter): Result<LoginResponse>? {
        return withContext(Dispatchers.IO) {
            try {
                val response = authRemoteDataSource.authWithEmail(param)
                if(!response.isSuccessful) {
                    val errorJsonString = response.errorBody()?.string()
                    val error = Gson().fromJson(errorJsonString, ErrorResponse::class.java)
                    Log.e(TAG, "Error post: $response")
                    val unauthorizedCodes = listOf(401, 403, 419, 415, 400)

                    if(response.code() in unauthorizedCodes) {
                        return@withContext Result.Error(error.message ?: "Unknown error!")
                    }
                    return@withContext Result.Error(response.errorBody().toString())
                }
                Log.d(TAG, response.body().toString())
                return@withContext response.body()?.let { data ->
                    val token = data.loginResult?.token
                    if(token != null) {
                        Log.d(TAG, "Trying to save token: $token")
                        pref.saveSetting(SettingPreferences.AUTH_TOKEN_KEY, token)
                    }
                    Result.Success(data) }
            } catch (e: Exception) {
                val message = e.localizedMessage!!
                Result.Error(message)
            }
        }

    }

    suspend fun register(param: RegisterParameter): Result<RegisterResponse>? {
        return withContext(Dispatchers.IO) {
            try {
                val response = authRemoteDataSource.register(param)
                if(!response.isSuccessful) {
                    val errorJsonString = response.errorBody()?.string()
                    val error = Gson().fromJson(errorJsonString, ErrorResponse::class.java)
                    Log.e(TAG, "Error post: $response")
                    val unauthorizedCodes = listOf(401, 403, 419, 415, 400)

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
        private  val TAG = AuthRepository::class.simpleName

        @SuppressLint("StaticFieldLeak")
        @Volatile
        private var instance: AuthRepository? = null
        fun getInstance(
            dataSource: AuthRemoteDataSource,
            pref: SettingPreferences
        ): AuthRepository =
            instance ?: synchronized(this) {
                instance ?: AuthRepository(dataSource, pref)
            }.also { instance = it }
    }
}