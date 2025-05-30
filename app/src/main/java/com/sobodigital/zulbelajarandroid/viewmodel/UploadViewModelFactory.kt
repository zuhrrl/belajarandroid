package com.sobodigital.zulbelajarandroid.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.sobodigital.zulbelajarandroid.data.repository.LocalRepository
import com.sobodigital.zulbelajarandroid.data.repository.StoryRepository
import com.sobodigital.zulbelajarandroid.di.Injection

class UploadViewModelFactory private constructor(private val localRepository: LocalRepository, private val storyRepository: StoryRepository) :
    ViewModelProvider.NewInstanceFactory() {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(UploadViewModel::class.java)) {
            return UploadViewModel(localRepository, storyRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class: " + modelClass.name)
    }

    companion object {
        @Volatile
        private var instance: UploadViewModelFactory? = null
        fun getInstance(context: Context): UploadViewModelFactory =
            instance ?: synchronized(this) {
                instance ?: UploadViewModelFactory(
                    Injection.provideLocalRepository(context),
                    Injection.provideStoryRepository(context))
            }.also { instance = it }
    }
}