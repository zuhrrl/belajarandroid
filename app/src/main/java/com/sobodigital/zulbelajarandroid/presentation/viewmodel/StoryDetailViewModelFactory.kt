package com.sobodigital.zulbelajarandroid.presentation.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.sobodigital.zulbelajarandroid.data.repository.StoryRepository
import com.sobodigital.zulbelajarandroid.di.Injection

class StoryDetailViewModelFactory private constructor(private val repository: StoryRepository) :
    ViewModelProvider.NewInstanceFactory() {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(StoryDetailViewModel::class.java)) {
            return StoryDetailViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class: " + modelClass.name)
    }

    companion object {
        @Volatile
        private var instance: StoryDetailViewModelFactory? = null
        fun getInstance(context: Context): StoryDetailViewModelFactory =
            instance ?: synchronized(this) {
                instance ?: StoryDetailViewModelFactory(Injection.provideStoryRepository(context))
            }.also { instance = it }
    }
}