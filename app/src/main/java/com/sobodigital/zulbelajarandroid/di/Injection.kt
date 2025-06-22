package com.sobodigital.zulbelajarandroid.di

import android.content.Context
import android.util.Log
import com.sobodigital.zulbelajarandroid.data.local.SettingPreferences
import com.sobodigital.zulbelajarandroid.data.local.dataStore
import com.sobodigital.zulbelajarandroid.data.remote.ApiConfig
import com.sobodigital.zulbelajarandroid.data.remote.AuthRemoteDataSource
import com.sobodigital.zulbelajarandroid.data.remote.StoryRemoteDataSource
import com.sobodigital.zulbelajarandroid.data.repository.AuthRepositoryImpl
import com.sobodigital.zulbelajarandroid.data.repository.LocalRepositoryImpl
import com.sobodigital.zulbelajarandroid.data.repository.MapsRepositoryImpl
import com.sobodigital.zulbelajarandroid.data.repository.StoryRepositoryImpl
import com.sobodigital.zulbelajarandroid.domain.usecase.AuthInteractor
import com.sobodigital.zulbelajarandroid.domain.usecase.AuthUseCase
import com.sobodigital.zulbelajarandroid.domain.usecase.MapsInteractor
import com.sobodigital.zulbelajarandroid.domain.usecase.MapsUseCase
import com.sobodigital.zulbelajarandroid.domain.usecase.MediaInteractor
import com.sobodigital.zulbelajarandroid.domain.usecase.MediaUseCase
import com.sobodigital.zulbelajarandroid.domain.usecase.SettingInteractor
import com.sobodigital.zulbelajarandroid.domain.usecase.SettingUseCase
import com.sobodigital.zulbelajarandroid.domain.usecase.StoryInteractor
import com.sobodigital.zulbelajarandroid.domain.usecase.StoryUseCase
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

    fun provideLocalRepository(context: Context) : LocalRepositoryImpl {
        val pref = SettingPreferences.getInstance(context.dataStore)
        return LocalRepositoryImpl.getInstance(context, pref)
    }


    fun provideStoryRepository(context: Context): StoryRepositoryImpl {
        val token = provideToken(context)
        Log.d("Injection", "Token ${token}")
        val dataSource = ApiConfig.getDataSource(StoryRemoteDataSource::class.java, token)
        return StoryRepositoryImpl.getInstance(dataSource)
    }

    fun provideAuthRepository(context: Context): AuthRepositoryImpl {
        val pref = SettingPreferences.getInstance(context.dataStore)
        val authDataSource = ApiConfig.getDataSource(AuthRemoteDataSource::class.java)
        return AuthRepositoryImpl.getInstance(authDataSource, pref)
    }

    fun providePrefs(context: Context): SettingPreferences {
        return SettingPreferences.getInstance(context.dataStore)
    }

    fun provideMapsRepository(context: Context): MapsRepositoryImpl {
        return MapsRepositoryImpl.getInstance(context)
    }

    fun provideAuthUsecase(context: Context): AuthUseCase {
        val repository = provideAuthRepository(context)
        val localRepository = provideLocalRepository(context)
        return AuthInteractor(repository, localRepository)
    }

    fun provideStoryUsecase(context: Context): StoryUseCase {
        val repository = provideStoryRepository(context)
        return StoryInteractor(repository)
    }

    fun provideMapsUseCase(context: Context): MapsUseCase {
        val mapsRepository = provideMapsRepository(context)
        return MapsInteractor(mapsRepository)
    }

    fun provideMediaUseCase(context: Context): MediaUseCase {
        val localRepository = provideLocalRepository(context)
        return MediaInteractor(localRepository)
    }

    fun provideSettingUseCase(context: Context): SettingUseCase {
        val localRepository = provideLocalRepository(context)
        return SettingInteractor(localRepository)
    }

}