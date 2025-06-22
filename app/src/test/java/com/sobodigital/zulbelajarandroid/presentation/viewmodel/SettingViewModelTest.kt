package com.sobodigital.zulbelajarandroid.presentation.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.sobodigital.zulbelajarandroid.DataDummy
import com.sobodigital.zulbelajarandroid.MainDispatcherRule
import com.sobodigital.zulbelajarandroid.data.Result
import com.sobodigital.zulbelajarandroid.domain.model.AuthSession
import com.sobodigital.zulbelajarandroid.domain.usecase.AuthUseCase
import com.sobodigital.zulbelajarandroid.domain.usecase.MapsUseCase
import com.sobodigital.zulbelajarandroid.domain.usecase.SettingUseCase
import com.sobodigital.zulbelajarandroid.domain.usecase.StoryUseCase
import com.sobodigital.zulbelajarandroid.getOrAwaitValue
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.junit.MockitoJUnitRunner


@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
class SettingViewModelTest {
    private lateinit var viewModel: SettingViewModel

    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    val mainDispatcherRules = MainDispatcherRule()

    @Mock
    private lateinit var settingUseCase: SettingUseCase

    @Mock
    private lateinit var authUseCase: AuthUseCase

    @Before
    fun setUp() {
        viewModel = SettingViewModel(settingUseCase, authUseCase)
    }

    @After
    fun tearDown() {
    }

    @Test
    fun `When check isloggedIn should return true`() = runTest {
        // arrange
        val isLoggedIn = true

        // act
        Mockito.`when`(
            authUseCase.checkIsLoggedIn()
        ).thenReturn(isLoggedIn)

        viewModel.checkIsLoggedIn()
        val status = viewModel.isLoggedIn.getOrAwaitValue()

        // assert
        assertTrue(status)

        // verify
        Mockito.verify(authUseCase).checkIsLoggedIn()
    }
}