package com.sobodigital.zulbelajarandroid.di

import android.content.Context
import android.util.Log
import com.sobodigital.zulbelajarandroid.data.local.LocalDataSource
import com.sobodigital.zulbelajarandroid.data.local.SettingPreferences
import com.sobodigital.zulbelajarandroid.data.local.dataStore
import com.sobodigital.zulbelajarandroid.data.remote.ApiConfig
import com.sobodigital.zulbelajarandroid.data.remote.ApiService
import com.sobodigital.zulbelajarandroid.data.remote.AuthRemoteDataSource
import com.sobodigital.zulbelajarandroid.data.remote.StoryRemoteDataSource
import com.sobodigital.zulbelajarandroid.data.repository.AuthRepositoryImpl
import com.sobodigital.zulbelajarandroid.data.repository.LocalRepository
import com.sobodigital.zulbelajarandroid.data.repository.MapsRepository
import com.sobodigital.zulbelajarandroid.data.repository.StoryRepository
import com.sobodigital.zulbelajarandroid.domain.usecase.AuthInteractor
import com.sobodigital.zulbelajarandroid.domain.usecase.AuthUsecase
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.runBlocking

object Injection {

    private fun provideToken(context: Context) : String {
        val pref = SettingPreferences.getInstance(context.dataStore)
        val data = runBlocking {
            pref.getPreferenceSetting(SettingPreferences.AUTH_TOKEN_KEY)
                .firstOrNull()
        }
        return data.toString()
    }

    fun provideLocalRepository(context: Context) : LocalRepository {
        val pref = SettingPreferences.getInstance(context.dataStore)
        return LocalRepository.getInstance(context, pref)
    }


    fun provideStoryRepository(context: Context): StoryRepository {
        val token = provideToken(context)
        Log.d("Injection", "Token ${token}")
        val dataSource = ApiConfig.getDataSource(StoryRemoteDataSource::class.java, token)
        return StoryRepository.getInstance(dataSource)
    }

    fun provideAuthRepository(context: Context): AuthRepositoryImpl {
        val pref = SettingPreferences.getInstance(context.dataStore)
        val authDataSource = ApiConfig.getDataSource(AuthRemoteDataSource::class.java)
        return AuthRepositoryImpl.getInstance(authDataSource, pref)
    }

    fun providePrefs(context: Context): SettingPreferences {
        return SettingPreferences.getInstance(context.dataStore)
    }

    fun provideMapsRepository(context: Context): MapsRepository {
        return MapsRepository.getInstance(context)
    }

    fun provideAuthUsecase(context: Context): AuthUsecase {
        val repository = provideAuthRepository(context)
        return AuthInteractor(repository)
    }

}