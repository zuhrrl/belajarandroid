package com.sobodigital.zulbelajarandroid.di

import android.content.Context
import android.util.Log
import androidx.lifecycle.asLiveData
import com.sobodigital.zulbelajarandroid.data.local.SettingPreferences
import com.sobodigital.zulbelajarandroid.data.local.dataStore
import com.sobodigital.zulbelajarandroid.data.remote.ApiConfig
import com.sobodigital.zulbelajarandroid.data.local.LocalDataSource
import com.sobodigital.zulbelajarandroid.data.remote.ApiService
import com.sobodigital.zulbelajarandroid.data.remote.AuthRemoteDataSource
import com.sobodigital.zulbelajarandroid.data.remote.StoryRemoteDatasource
import com.sobodigital.zulbelajarandroid.data.repository.AuthRepository
import com.sobodigital.zulbelajarandroid.data.repository.EventRepository
import com.sobodigital.zulbelajarandroid.data.repository.StoryRepository
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.runBlocking

object Injection {

    private fun provideToken(context: Context) : String {
        val pref = SettingPreferences.getInstance(context.dataStore)
        val token = pref.getPreferenceSetting(SettingPreferences.AUTH_TOKEN_KEY)
        return runBlocking { token.firstOrNull() ?: "" } as String
    }
    fun provideEventRepository(context: Context): EventRepository {
        val token = provideToken(context)
        val dataSource = ApiConfig.getDataSource(ApiService::class.java, token)
        val localDataSource = LocalDataSource.getInstance(context)
        return EventRepository.getInstance(dataSource, localDataSource)
    }

    fun provideStoryRepository(context: Context): StoryRepository {
        val token = provideToken(context)
        val dataSource = ApiConfig.getDataSource(StoryRemoteDatasource::class.java, token)
        return StoryRepository.getInstance(dataSource)
    }


    fun provideAuthRepository(context: Context): AuthRepository {
        val pref = SettingPreferences.getInstance(context.dataStore)
        val authDataSource = ApiConfig.getDataSource(AuthRemoteDataSource::class.java)
        return AuthRepository.getInstance(authDataSource, pref)
    }

    fun providePrefs(context: Context): SettingPreferences {
        return SettingPreferences.getInstance(context.dataStore)
    }


}