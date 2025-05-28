package com.sobodigital.zulbelajarandroid.di

import android.content.Context
import com.sobodigital.zulbelajarandroid.data.local.SettingPreferences
import com.sobodigital.zulbelajarandroid.data.local.dataStore
import com.sobodigital.zulbelajarandroid.data.remote.ApiConfig
import com.sobodigital.zulbelajarandroid.data.local.LocalDataSource
import com.sobodigital.zulbelajarandroid.data.remote.ApiService
import com.sobodigital.zulbelajarandroid.data.remote.AuthRemoteDataSource
import com.sobodigital.zulbelajarandroid.data.repository.AuthRepository
import com.sobodigital.zulbelajarandroid.data.repository.EventRepository

object Injection {
    fun provideEventRepository(context: Context): EventRepository {
        val dataSource = ApiConfig.getDataSource(ApiService::class.java)
        val localDataSource = LocalDataSource.getInstance(context)
        return EventRepository.getInstance(dataSource, localDataSource)
    }

    fun provideAuthRepository(context: Context): AuthRepository {
        val authDataSource = ApiConfig.getDataSource(AuthRemoteDataSource::class.java)
        return AuthRepository.getInstance(authDataSource)
    }

    fun providePrefs(context: Context): SettingPreferences {
        return SettingPreferences.getInstance(context.dataStore)
    }


}