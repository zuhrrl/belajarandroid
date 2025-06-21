package com.sobodigital.zulbelajarandroid.domain.repository

import com.sobodigital.zulbelajarandroid.data.Result
import com.sobodigital.zulbelajarandroid.domain.model.AuthCredential
import com.sobodigital.zulbelajarandroid.domain.model.AuthSession
import com.sobodigital.zulbelajarandroid.domain.model.RegisterData
import com.sobodigital.zulbelajarandroid.domain.model.RegisterSession

interface AuthRepository {

    suspend fun authWithEmail(authCredential: AuthCredential): Result<AuthSession>?

    suspend fun register(registerData: RegisterData): Result<RegisterSession>?
}