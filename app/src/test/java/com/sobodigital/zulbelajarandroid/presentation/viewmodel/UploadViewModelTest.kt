package com.sobodigital.zulbelajarandroid.presentation.viewmodel

import android.net.Uri
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.sobodigital.zulbelajarandroid.DataDummy
import com.sobodigital.zulbelajarandroid.MainDispatcherRule
import com.sobodigital.zulbelajarandroid.data.Result
import com.sobodigital.zulbelajarandroid.domain.repository.LocalRepository
import com.sobodigital.zulbelajarandroid.domain.usecase.MapsUseCase
import com.sobodigital.zulbelajarandroid.domain.usecase.MediaUseCase
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
class UploadViewModelTest {
    private lateinit var viewModel: UploadViewModel

    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    val mainDispatcherRules = MainDispatcherRule()

    @Mock
    private lateinit var storyUseCase: StoryUseCase

    @Mock
    private lateinit var mediaUseCase: MediaUseCase

    @Before
    fun setUp() {
        viewModel = UploadViewModel(storyUseCase, mediaUseCase)
    }

    @After
    fun tearDown() {
    }

    @Test
    fun `When pick image from gallery should return image size less than 1MB`() = runTest {
        // arrange
        val dummyUri = Uri.parse("test")
        val dummyFile = DataDummy.generateSampleImage()

        // act
        Mockito.`when`(
            mediaUseCase.reduceImageSize(dummyUri)
        ).thenReturn(Result.Success(dummyFile))

        viewModel.onGalleryImagePicked(dummyUri)
        val file = viewModel.fileFromGallery.getOrAwaitValue()

        // assert
        assertTrue(file!!.exists())
        assertTrue(file.length() <= 1000)

        // verify
        Mockito.verify(mediaUseCase).reduceImageSize(dummyUri)

        // clean up
        dummyFile.delete()
    }

    @Test
    fun `When pick image from camera should return image size less than 1MB`() = runTest {
        // arrange
        val dummyUri = Uri.parse("test")
        val dummyFile = DataDummy.generateSampleImage()

        // act
        Mockito.`when`(
            mediaUseCase.reduceImageSize(dummyUri)
        ).thenReturn(Result.Success(dummyFile))

        viewModel.onCameraImagePicked(dummyUri)
        val file = viewModel.fileFromCamera.getOrAwaitValue()

        // assert
        assertTrue(file!!.exists())
        assertTrue(file.length() <= 1000)

        // verify
        Mockito.verify(mediaUseCase).reduceImageSize(dummyUri)

        // clean up
        dummyFile.delete()
    }
}