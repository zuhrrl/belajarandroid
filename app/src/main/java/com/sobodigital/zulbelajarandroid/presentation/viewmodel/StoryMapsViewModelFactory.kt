package com.sobodigital.zulbelajarandroid.presentation.viewmodel
import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.sobodigital.zulbelajarandroid.data.repository.MapsRepositoryImpl
import com.sobodigital.zulbelajarandroid.data.repository.StoryRepositoryImpl
import com.sobodigital.zulbelajarandroid.di.Injection
import com.sobodigital.zulbelajarandroid.domain.usecase.MapsUseCase
import com.sobodigital.zulbelajarandroid.domain.usecase.StoryUseCase

class StoryMapsViewModelFactory private constructor(
    private val mapsUseCase: MapsUseCase,
    private val storyUseCase: StoryUseCase,
) :
    ViewModelProvider.NewInstanceFactory() {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(StoryMapsViewModel::class.java)) {
            return StoryMapsViewModel(mapsUseCase, storyUseCase) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class: " + modelClass.name)
    }

    companion object {
        @Volatile
        private var instance: StoryMapsViewModelFactory? = null
        fun getInstance(context: Context): StoryMapsViewModelFactory =
            instance ?: synchronized(this) {
                instance ?: StoryMapsViewModelFactory(
                    Injection.provideMapsUseCase(context),
                    Injection.provideStoryUsecase(context))
            }.also { instance = it }
    }
}