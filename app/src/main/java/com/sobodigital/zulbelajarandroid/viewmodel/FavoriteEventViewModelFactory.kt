package com.sobodigital.zulbelajarandroid.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.sobodigital.zulbelajarandroid.data.repository.EventRepository
import com.sobodigital.zulbelajarandroid.di.Injection

class FavoriteEventViewModelFactory(private val repository: EventRepository) :
    ViewModelProvider.NewInstanceFactory() {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(FavoriteEventViewModel::class.java)) {
            return FavoriteEventViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class: " + modelClass.name)
    }

    companion object {
        @Volatile
        private var instance: FavoriteEventViewModelFactory? = null
        fun getInstance(context: Context): FavoriteEventViewModelFactory =
            instance ?: synchronized(this) {
                instance ?: FavoriteEventViewModelFactory(Injection.provideEventRepository(context))
            }.also { instance = it }
    }
}