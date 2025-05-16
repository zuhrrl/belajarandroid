package com.sobodigital.zulbelajarandroid.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.sobodigital.zulbelajarandroid.data.repository.EventRepository
import com.sobodigital.zulbelajarandroid.di.Injection

class EventDetailViewModelFactory private constructor(private val repository: EventRepository) :
    ViewModelProvider.NewInstanceFactory() {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(EventDetailViewModel::class.java)) {
            return EventDetailViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class: " + modelClass.name)
    }

    companion object {
        @Volatile
        private var instance: EventDetailViewModelFactory? = null
        fun getInstance(context: Context): EventDetailViewModelFactory =
            instance ?: synchronized(this) {
                instance ?: EventDetailViewModelFactory(Injection.provideEventRepository(context))
            }.also { instance = it }
    }
}