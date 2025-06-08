package com.sobodigital.zulbelajarandroid.di

import android.content.Context
import com.sobodigital.zulbelajarandroid.data.local.LocalDataSource
import com.sobodigital.zulbelajarandroid.data.local.SettingPreferences
import com.sobodigital.zulbelajarandroid.data.local.dataStore
import com.sobodigital.zulbelajarandroid.data.remote.ApiConfig
import com.sobodigital.zulbelajarandroid.data.remote.ApiService
import com.sobodigital.zulbelajarandroid.data.remote.AuthRemoteDataSource
import com.sobodigital.zulbelajarandroid.data.remote.StoryRemoteDataSource
import com.sobodigital.zulbelajarandroid.data.repository.AuthRepository
import com.sobodigital.zulbelajarandroid.data.repository.EventRepository
import com.sobodigital.zulbelajarandroid.data.repository.LocalRepository
import com.sobodigital.zulbelajarandroid.data.repository.MapsRepository
import com.sobodigital.zulbelajarandroid.data.repository.StoryRepository
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

    fun provideEventRepository(context: Context): EventRepository {
        val token = provideToken(context)
        val dataSource = ApiConfig.getDataSource(ApiService::class.java, token)
        val localDataSource = LocalDataSource.getInstance(context)
        return EventRepository.getInstance(dataSource, localDataSource)
    }

    fun provideStoryRepository(context: Context): StoryRepository {
        val token = provideToken(context)
        val dataSource = ApiConfig.getDataSource(StoryRemoteDataSource::class.java, token)
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

    fun provideMapsRepository(context: Context): MapsRepository {
        return MapsRepository.getInstance(context)
    }

}