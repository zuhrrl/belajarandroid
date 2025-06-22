package com.sobodigital.zulbelajarandroid.presentation.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.sobodigital.zulbelajarandroid.data.repository.LocalRepositoryImpl
import com.sobodigital.zulbelajarandroid.data.repository.StoryRepositoryImpl
import com.sobodigital.zulbelajarandroid.di.Injection
import com.sobodigital.zulbelajarandroid.domain.usecase.MediaUseCase
import com.sobodigital.zulbelajarandroid.domain.usecase.StoryUseCase

class UploadViewModelFactory private constructor(
    private val storyUseCase: StoryUseCase,
    private val mediaUseCase: MediaUseCase) :
    ViewModelProvider.NewInstanceFactory() {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(UploadViewModel::class.java)) {
            return UploadViewModel(storyUseCase, mediaUseCase) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class: " + modelClass.name)
    }

    companion object {
        @Volatile
        private var instance: UploadViewModelFactory? = null
        fun getInstance(context: Context): UploadViewModelFactory =
            instance ?: synchronized(this) {
                instance ?: UploadViewModelFactory(
                    Injection.provideStoryUsecase(context),
                    Injection.provideMediaUseCase(context)
                )
            }.also { instance = it }
    }
}