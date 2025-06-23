package com.sobodigital.zulbelajarandroid.data.repository

import android.annotation.SuppressLint
import android.provider.ContactsContract.Data
import android.util.Log
import com.google.gson.Gson
import com.sobodigital.zulbelajarandroid.data.Result
import com.sobodigital.zulbelajarandroid.data.local.SettingPreferences
import com.sobodigital.zulbelajarandroid.data.model.AuthParameter
import com.sobodigital.zulbelajarandroid.data.model.ErrorResponse
import com.sobodigital.zulbelajarandroid.data.model.RegisterParameter
import com.sobodigital.zulbelajarandroid.data.model.RegisterResponse
import com.sobodigital.zulbelajarandroid.data.remote.AuthRemoteDataSource
import com.sobodigital.zulbelajarandroid.domain.model.AuthCredential
import com.sobodigital.zulbelajarandroid.domain.model.AuthSession
import com.sobodigital.zulbelajarandroid.domain.model.LoginResult
import com.sobodigital.zulbelajarandroid.domain.model.RegisterData
import com.sobodigital.zulbelajarandroid.domain.model.RegisterSession
import com.sobodigital.zulbelajarandroid.domain.repository.AuthRepository
import com.sobodigital.zulbelajarandroid.utils.DataMapper
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class AuthRepositoryImpl(
    private val authRemoteDataSource: AuthRemoteDataSource,
    private val pref: SettingPreferences) : AuthRepository {

    override suspend fun authWithEmail(authCredential: AuthCredential): Result<AuthSession>? {
        return withContext(Dispatchers.IO) {
            try {
                val parameter = DataMapper.mapDomainToEntity(authCredential, {
                    AuthParameter(
                        email = it.email,
                        password = it.password
                    )
                })
                val response = authRemoteDataSource.authWithEmail(parameter)
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
                return@withContext response.body()?.let { data ->
                    val token = data.loginResult?.token
                    if(token != null) {
                        Log.d(TAG, "Trying to save token: $token")
                        pref.saveSetting(SettingPreferences.AUTH_TOKEN_KEY, token)
                    }
                    val domain = DataMapper.mapEntityToDomain(data, { AuthSession(
                        loginResult = DataMapper.mapEntityToDomain(it.loginResult, { loginResult ->
                            LoginResult(
                                userId = loginResult?.userId,
                                name = loginResult?.name,
                                token = loginResult?.token
                            )
                        }),
                        message = it.message,
                        error = it.error

                    )
                    })
                    Result.Success(domain) }
            } catch (e: Exception) {
                val message = e.localizedMessage!!
                Result.Error(message)
            }
        }
    }

    override suspend fun register(registerData: RegisterData): Result<RegisterSession>? {
        return withContext(Dispatchers.IO) {
            try {
                val parameter = DataMapper.mapDomainToEntity(registerData, {
                    RegisterParameter(
                        email = it.email,
                        name = it.name,
                        password = it.password
                    )
                })
                val response = authRemoteDataSource.register(parameter)
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
                    val domain = DataMapper.mapEntityToDomain(data, {
                        RegisterSession(
                            error = it.error,
                            message = it.message
                        )
                    })
                    Result.Success(domain)
                }
            } catch (e: Exception) {
                val message = e.localizedMessage!!
                Result.Error(message)
            }
        }
    }

    companion object {
        private  val TAG = AuthRepositoryImpl::class.simpleName

        @SuppressLint("StaticFieldLeak")
        @Volatile
        private var instance: AuthRepositoryImpl? = null
        fun getInstance(
            dataSource: AuthRemoteDataSource,
            pref: SettingPreferences
        ): AuthRepositoryImpl =
            instance ?: synchronized(this) {
                instance ?: AuthRepositoryImpl(dataSource, pref)
            }.also { instance = it }
    }

}