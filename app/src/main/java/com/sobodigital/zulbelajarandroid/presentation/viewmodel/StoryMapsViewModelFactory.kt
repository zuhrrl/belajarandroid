package com.sobodigital.zulbelajarandroid.presentation.viewmodel
import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.sobodigital.zulbelajarandroid.data.repository.MapsRepository
import com.sobodigital.zulbelajarandroid.data.repository.StoryRepository
import com.sobodigital.zulbelajarandroid.di.Injection

class StoryMapsViewModelFactory private constructor(
    private val mapsRepository: MapsRepository,
    private val storyRepository: StoryRepository,
) :
    ViewModelProvider.NewInstanceFactory() {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(StoryMapsViewModel::class.java)) {
            return StoryMapsViewModel(mapsRepository, storyRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class: " + modelClass.name)
    }

    companion object {
        @Volatile
        private var instance: StoryMapsViewModelFactory? = null
        fun getInstance(context: Context): StoryMapsViewModelFactory =
            instance ?: synchronized(this) {
                instance ?: StoryMapsViewModelFactory(
                    Injection.provideMapsRepository(context),
                    Injection.provideStoryRepository(context))
            }.also { instance = it }
    }
}