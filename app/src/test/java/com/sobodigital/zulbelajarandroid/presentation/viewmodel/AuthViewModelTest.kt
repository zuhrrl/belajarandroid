package com.sobodigital.zulbelajarandroid.presentation.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.sobodigital.zulbelajarandroid.MainDispatcherRule
import com.sobodigital.zulbelajarandroid.data.Result
import com.sobodigital.zulbelajarandroid.domain.model.AuthCredential
import com.sobodigital.zulbelajarandroid.domain.model.AuthSession
import com.sobodigital.zulbelajarandroid.domain.model.LoginResult
import com.sobodigital.zulbelajarandroid.domain.model.RegisterData
import com.sobodigital.zulbelajarandroid.domain.model.RegisterSession
import com.sobodigital.zulbelajarandroid.domain.usecase.AuthUseCase
import com.sobodigital.zulbelajarandroid.getOrAwaitValue
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Assert.*

import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.junit.MockitoJUnitRunner

@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
class AuthViewModelTest {

    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    val mainDispatcherRules = MainDispatcherRule()

    @Mock
    private lateinit var authUsecase: AuthUseCase

    @Test
    fun `When auth with user email and password should success`() = runTest {
        // arrange
        val authViewModel = AuthViewModel(authUsecase)
        val authCredential = AuthCredential(email = "test@gmail.com", password = "password")
        val dummyAuthSession = AuthSession(loginResult = LoginResult(name = "test"),
            error = false,
            message = "Success")

        // act
        Mockito.`when`(
            authUsecase.authWithEmail(authCredential)
        ).thenReturn(Result.Success(dummyAuthSession))

        authViewModel.authWithEmail(authCredential)
        val authSession = authViewModel.authSession.getOrAwaitValue()

        // assert
        assertEquals(false, authSession.error)

        // verify
        Mockito.verify(authUsecase).authWithEmail(authCredential)
    }

    @Test
    fun `When register with RegisterData should success`() = runTest {
        // arrange
        val authViewModel = AuthViewModel(authUsecase)
        val registerData = RegisterData(name = "test", email = "test@gmail.com", password = "password")
        val dummyRegisterSession = RegisterSession(
            error = false,
            message = "Success")

        // act
        Mockito.`when`(
            authUsecase.register(registerData)
        ).thenReturn(Result.Success(dummyRegisterSession))

        authViewModel.register(registerData)
        val registerSession = authViewModel.registerSession.getOrAwaitValue()

        // assert
        assertEquals(false, registerSession.error)

        // verify
        Mockito.verify(authUsecase).register(registerData)
    }
}