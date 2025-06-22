package com.sobodigital.zulbelajarandroid.domain.usecase

import com.sobodigital.zulbelajarandroid.data.Result
import com.sobodigital.zulbelajarandroid.domain.model.AuthCredential
import com.sobodigital.zulbelajarandroid.domain.model.AuthSession
import com.sobodigital.zulbelajarandroid.domain.model.RegisterData
import com.sobodigital.zulbelajarandroid.domain.model.RegisterSession


interface AuthUseCase {
    suspend fun authWithEmail(authCredential: AuthCredential): Result<AuthSession>?

    suspend fun register(registerData: RegisterData): Result<RegisterSession>?

    suspend fun checkIsLoggedIn() : Boolean

}