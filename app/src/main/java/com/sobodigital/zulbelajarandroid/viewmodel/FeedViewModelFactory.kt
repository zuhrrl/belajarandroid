package com.sobodigital.zulbelajarandroid.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import android.content.Context
import com.sobodigital.zulbelajarandroid.data.local.SettingPreferences
import com.sobodigital.zulbelajarandroid.data.repository.EventRepository
import com.sobodigital.zulbelajarandroid.data.repository.LocalRepository
import com.sobodigital.zulbelajarandroid.data.repository.StoryRepository
import com.sobodigital.zulbelajarandroid.di.Injection

class FeedViewModelFactory private constructor(
    private val repository: StoryRepository,
    private val localRepository: LocalRepository) :
    ViewModelProvider.NewInstanceFactory() {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(FeedViewModel::class.java)) {
                return FeedViewModel(repository, localRepository) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class: " + modelClass.name)
        }

        companion object {
            @Volatile
            private var instance: FeedViewModelFactory? = null
            fun getInstance(context: Context): FeedViewModelFactory =
                instance ?: synchronized(this) {
                    instance ?: FeedViewModelFactory(Injection.provideStoryRepository(context),
                        Injection.provideLocalRepository(context))
                }.also { instance = it }
        }
}