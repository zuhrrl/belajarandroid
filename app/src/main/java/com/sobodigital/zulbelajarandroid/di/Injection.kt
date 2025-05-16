package com.sobodigital.zulbelajarandroid.di

import android.content.Context
import com.sobodigital.zulbelajarandroid.data.local.SettingPreferences
import com.sobodigital.zulbelajarandroid.data.local.dataStore
import com.sobodigital.zulbelajarandroid.data.remote.ApiConfig
import com.sobodigital.zulbelajarandroid.data.repository.EventRepository

object Injection {
    fun provideEventRepository(context: Context): EventRepository {
        val apiService = ApiConfig.getApiService()
        return EventRepository.getInstance(apiService)
    }

    fun providePrefs(context: Context): SettingPreferences {
        return SettingPreferences.getInstance(context.dataStore)
    }
}