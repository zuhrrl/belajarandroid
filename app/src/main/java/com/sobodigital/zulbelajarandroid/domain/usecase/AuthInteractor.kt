package com.sobodigital.zulbelajarandroid.domain.usecase

import com.sobodigital.zulbelajarandroid.data.Result
import com.sobodigital.zulbelajarandroid.domain.model.AuthCredential
import com.sobodigital.zulbelajarandroid.domain.model.AuthSession
import com.sobodigital.zulbelajarandroid.domain.model.RegisterData
import com.sobodigital.zulbelajarandroid.domain.model.RegisterSession
import com.sobodigital.zulbelajarandroid.domain.repository.AuthRepository

class AuthInteractor(private val authRepository: AuthRepository) : AuthUsecase {
    override suspend fun authWithEmail(authCredential: AuthCredential): Result<AuthSession>? {
        return authRepository.authWithEmail(authCredential)
    }

    override suspend fun register(registerData: RegisterData): Result<RegisterSession>? {
        return authRepository.register(registerData)
    }


}