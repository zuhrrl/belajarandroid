package com.sobodigital.zulbelajarandroid.presentation.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.sobodigital.zulbelajarandroid.DataDummy
import com.sobodigital.zulbelajarandroid.MainDispatcherRule
import com.sobodigital.zulbelajarandroid.data.Result
import com.sobodigital.zulbelajarandroid.domain.usecase.MapsUseCase
import com.sobodigital.zulbelajarandroid.domain.usecase.StoryUseCase
import com.sobodigital.zulbelajarandroid.getOrAwaitValue
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.junit.MockitoJUnitRunner

@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
class StoryMapsViewModelTest {
    private lateinit var viewModel: StoryMapsViewModel

    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    val mainDispatcherRules = MainDispatcherRule()

    @Mock
    private lateinit var mapsUseCase: MapsUseCase

    @Mock
    private lateinit var storyUseCase: StoryUseCase

    @Before
    fun setUp() {
        viewModel = StoryMapsViewModel(mapsUseCase, storyUseCase)
    }

    @After
    fun tearDown() {
    }

    @Test
    fun `When fetch stories with location should return story with lat lng`() = runTest {
        // arrange
        val dummyStories = DataDummy.generateDummyStories()

        // act
        Mockito.`when`(
            storyUseCase.fetchStories(1)
        ).thenReturn(Result.Success(dummyStories))

        viewModel.fetchStoryWithLocation()
        val stories = viewModel.listStory.getOrAwaitValue()

        // assert
        assertTrue(stories.isNotEmpty())
        assertTrue(stories.all { it.lat != null && it.lon != null })

        // verify
        Mockito.verify(storyUseCase).fetchStories(1)
    }
}