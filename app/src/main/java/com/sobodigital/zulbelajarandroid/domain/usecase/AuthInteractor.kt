package com.sobodigital.zulbelajarandroid.domain.usecase

import com.sobodigital.zulbelajarandroid.data.Result
import com.sobodigital.zulbelajarandroid.data.local.SettingPreferences
import com.sobodigital.zulbelajarandroid.domain.model.AuthCredential
import com.sobodigital.zulbelajarandroid.domain.model.AuthSession
import com.sobodigital.zulbelajarandroid.domain.model.RegisterData
import com.sobodigital.zulbelajarandroid.domain.model.RegisterSession
import com.sobodigital.zulbelajarandroid.domain.repository.AuthRepository
import com.sobodigital.zulbelajarandroid.domain.repository.LocalRepository
import kotlinx.coroutines.flow.first

class AuthInteractor(private val authRepository: AuthRepository,
                     private val localRepository: LocalRepository) : AuthUseCase {
    override suspend fun authWithEmail(authCredential: AuthCredential): Result<AuthSession>? {
        return authRepository.authWithEmail(authCredential)
    }

    override suspend fun register(registerData: RegisterData): Result<RegisterSession>? {
        return authRepository.register(registerData)
    }

    override suspend fun checkIsLoggedIn(): Boolean {
        val token = localRepository.getPreferenceSetting(SettingPreferences.AUTH_TOKEN_KEY).first()
        val isLoggedIn = token is String && token.isNotEmpty()
        return isLoggedIn
    }


}