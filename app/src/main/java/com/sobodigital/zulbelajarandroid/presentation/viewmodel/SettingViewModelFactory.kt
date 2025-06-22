package com.sobodigital.zulbelajarandroid.presentation.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import com.sobodigital.zulbelajarandroid.data.local.SettingPreferences
import androidx.lifecycle.ViewModelProvider.NewInstanceFactory
import com.sobodigital.zulbelajarandroid.di.Injection
import com.sobodigital.zulbelajarandroid.domain.usecase.AuthUseCase
import com.sobodigital.zulbelajarandroid.domain.usecase.SettingUseCase

class SettingViewModelFactory (private val settingUseCase: SettingUseCase,
                               private  val authUseCase: AuthUseCase) : NewInstanceFactory() {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(SettingViewModel::class.java)) {
            return SettingViewModel(settingUseCase, authUseCase) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class: " + modelClass.name)
    }

    companion object {
        @Volatile
        private var instance: SettingViewModelFactory? = null
        fun getInstance(context: Context): SettingViewModelFactory =
            instance ?: synchronized(this) {
                instance ?: SettingViewModelFactory(
                    Injection.provideSettingUseCase(context),
                Injection.provideAuthUsecase(context))
            }.also { instance = it }
    }
}