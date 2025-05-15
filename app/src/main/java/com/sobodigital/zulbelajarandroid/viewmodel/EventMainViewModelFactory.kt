package com.sobodigital.zulbelajarandroid.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import android.content.Context
import com.sobodigital.zulbelajarandroid.data.repository.EventRepository
import com.sobodigital.zulbelajarandroid.di.Injection

class EventMainViewModelFactory private constructor(private val repository: EventRepository) :
    ViewModelProvider.NewInstanceFactory() {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(EventMainViewModel::class.java)) {
                return EventMainViewModel(repository) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class: " + modelClass.name)
        }

        companion object {
            @Volatile
            private var instance: EventMainViewModelFactory? = null
            fun getInstance(context: Context): EventMainViewModelFactory =
                instance ?: synchronized(this) {
                    instance ?: EventMainViewModelFactory(Injection.provideEventRepository(context))
                }.also { instance = it }
        }
}