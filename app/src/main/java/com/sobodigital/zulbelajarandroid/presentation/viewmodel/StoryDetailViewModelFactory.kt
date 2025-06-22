package com.sobodigital.zulbelajarandroid.presentation.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.sobodigital.zulbelajarandroid.data.repository.StoryRepositoryImpl
import com.sobodigital.zulbelajarandroid.di.Injection
import com.sobodigital.zulbelajarandroid.domain.usecase.StoryUseCase

class StoryDetailViewModelFactory private constructor(private val storyUseCase: StoryUseCase) :
    ViewModelProvider.NewInstanceFactory() {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(StoryDetailViewModel::class.java)) {
            return StoryDetailViewModel(storyUseCase) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class: " + modelClass.name)
    }

    companion object {
        @Volatile
        private var instance: StoryDetailViewModelFactory? = null
        fun getInstance(context: Context): StoryDetailViewModelFactory =
            instance ?: synchronized(this) {
                instance ?: StoryDetailViewModelFactory(Injection.provideStoryUsecase(context))
            }.also { instance = it }
    }
}