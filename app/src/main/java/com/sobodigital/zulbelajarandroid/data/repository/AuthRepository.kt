package com.sobodigital.zulbelajarandroid.data.repository

import android.annotation.SuppressLint
import android.util.Log
import com.sobodigital.zulbelajarandroid.data.Result
import com.sobodigital.zulbelajarandroid.data.local.LocalDataSource
import com.sobodigital.zulbelajarandroid.data.model.AuthParameter
import com.sobodigital.zulbelajarandroid.data.model.LoginResponse
import com.sobodigital.zulbelajarandroid.data.remote.ApiService
import com.sobodigital.zulbelajarandroid.data.remote.AuthRemoteDataSource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class AuthRepository(private val authRemoteDataSource: AuthRemoteDataSource) {

    suspend fun authWithEmail(param: AuthParameter): Result<LoginResponse>? {
        return withContext(Dispatchers.IO) {
            try {
                val response = authRemoteDataSource.authWithEmail(param)
                if(!response.isSuccessful) {
                    return@withContext Result.Error(response.errorBody().toString())
                }
                Log.d(TAG, response.body().toString())
                return@withContext response.body()?.let { data -> Result.Success(data) }
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
        ): AuthRepository =
            instance ?: synchronized(this) {
                instance ?: AuthRepository(dataSource)
            }.also { instance = it }
    }
}