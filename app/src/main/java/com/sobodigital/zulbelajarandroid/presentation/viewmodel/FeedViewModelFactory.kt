package com.sobodigital.zulbelajarandroid.presentation.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.sobodigital.zulbelajarandroid.di.Injection
import com.sobodigital.zulbelajarandroid.domain.usecase.SettingUseCase
import com.sobodigital.zulbelajarandroid.domain.usecase.StoryUseCase

class FeedViewModelFactory private constructor(
    private val storyUseCase: StoryUseCase,
    private val settingUseCase: SettingUseCase) :
    ViewModelProvider.NewInstanceFactory() {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(FeedViewModel::class.java)) {
                return FeedViewModel(storyUseCase, settingUseCase) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class: " + modelClass.name)
        }

        companion object {
            @Volatile
            private var instance: FeedViewModelFactory? = null
            fun getInstance(context: Context): FeedViewModelFactory =
                instance ?: synchronized(this) {
                    instance ?: FeedViewModelFactory(
                        Injection.provideStoryUsecase(context),
                        Injection.provideSettingUseCase(context))
                }.also { instance = it }
        }
}