package com.sobodigital.zulbelajarandroid.presentation.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.switchMap
import androidx.paging.AsyncPagingDataDiffer
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.PagingSource
import androidx.paging.PagingState
import androidx.recyclerview.widget.ListUpdateCallback
import com.sobodigital.zulbelajarandroid.DataDummy
import com.sobodigital.zulbelajarandroid.MainDispatcherRule
import com.sobodigital.zulbelajarandroid.TLog
import com.sobodigital.zulbelajarandroid.data.Result
import com.sobodigital.zulbelajarandroid.domain.model.Story
import com.sobodigital.zulbelajarandroid.domain.usecase.SettingUseCase
import com.sobodigital.zulbelajarandroid.domain.usecase.StoryUseCase
import com.sobodigital.zulbelajarandroid.getOrAwaitValue
import com.sobodigital.zulbelajarandroid.presentation.adapter.StoryAdapterWithPaging
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.TimeoutCancellationException
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.withTimeout
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.junit.MockitoJUnitRunner

@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
class FeedViewModelTest {
    private lateinit var viewModel: FeedViewModel

    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    val mainDispatcherRules = MainDispatcherRule()

    @Mock
    private lateinit var storyUseCase: StoryUseCase

    @Mock
    private lateinit var settingUseCase: SettingUseCase

    @Before
    fun setup() {
        viewModel = FeedViewModel(storyUseCase, settingUseCase)
    }

    @Test
    fun `when Get Stories Should Not Null and Return Data`() = runTest {

        //arrange
        val testDispatcher = mainDispatcherRules.testDispatcher
        val dummyStories = DataDummy.generateDummyStories()
        val pager = Pager(
            config = PagingConfig(
                pageSize = 5
            ),
            pagingSourceFactory = {
                StoryPagingSource(dummyStories)
            }
        )

        Mockito.`when`(storyUseCase.fetchStoryWithPaging()).thenReturn(Result.Success(pager))

        // act
        viewModel.fetchStoryWithPaging()
        advanceUntilIdle()

        val pagingData = viewModel.pagingData.switchMap { it }
        val actualStories: PagingData<Story> = pagingData.getOrAwaitValue()

        val differ = AsyncPagingDataDiffer(
            diffCallback = StoryAdapterWithPaging.DIFF_CALLBACK,
            updateCallback = noopListUpdateCallback,
            workerDispatcher = testDispatcher,
            mainDispatcher = testDispatcher

        )

        try {
            withTimeout(5) {
                differ.submitData(actualStories)
            }
        } catch (_: TimeoutCancellationException) {

        }

        // assert
        assertNotNull(differ.snapshot())
        assertEquals(dummyStories.size, differ.snapshot().size)
        assertEquals(dummyStories[0], differ.snapshot()[0])

    }

    @Test
    fun `when Get Empty stories Should Return No Data`() = runTest {

        //arrange
        val testDispatcher = mainDispatcherRules.testDispatcher
        val pager = Pager(
            config = PagingConfig(
                pageSize = 5
            ),
            pagingSourceFactory = {
                StoryPagingSource(emptyList())
            }
        )

        Mockito.`when`(storyUseCase.fetchStoryWithPaging()).thenReturn(Result.Success(pager))

        // act
        viewModel.fetchStoryWithPaging()
        advanceUntilIdle()

        val pagingData = viewModel.pagingData.switchMap { it }
        val actualStories: PagingData<Story> = pagingData.getOrAwaitValue()

        val differ = AsyncPagingDataDiffer(
            diffCallback = StoryAdapterWithPaging.DIFF_CALLBACK,
            updateCallback = noopListUpdateCallback,
            workerDispatcher = testDispatcher,
            mainDispatcher = testDispatcher

        )

        try {
            withTimeout(5) {
                differ.submitData(actualStories)
            }
        } catch (_: TimeoutCancellationException) {

        }

        // assert
        assertEquals(0, differ.snapshot().size)

        // verify
        Mockito.verify(storyUseCase).fetchStoryWithPaging()
    }

    @Test
    fun `when clear all preference should called`() = runTest {

        //arrange

        // act
        viewModel.clearAllSetting()
        advanceUntilIdle()

        // verify
        Mockito.verify(settingUseCase).clearAllPreference()
    }

    @Test
    fun `Set loading should return correct status`() = runTest {

        //arrange
        val newStatus = false;

        // act
        viewModel.setLoading(newStatus)
        val status = viewModel.isLoading.getOrAwaitValue()

        // assert
        assertEquals(false, status)

    }

    @Test
    fun `Set Error data should return correct Error Data`() = runTest {

        //arrange
        val newErrorData = Result.Error("Error");

        // act
        viewModel.setErrorData(newErrorData)
        val errorData = viewModel.errorData.getOrAwaitValue()

        // assert
        assertEquals(newErrorData, errorData)

    }
}



class StoryPagingSource(private val stories: List<Story>) : PagingSource<Int, Story>() {

    override fun getRefreshKey(state: PagingState<Int, Story>): Int? {
        return 0
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Story> {
        TLog.d("suspend", stories.toString())
        val page = params.key ?: 1

        return LoadResult.Page(stories, prevKey = if (page == 1) null else page - 1,
            nextKey = if (stories.isEmpty()) null else page + 1)
    }

}

val noopListUpdateCallback = object : ListUpdateCallback {
    override fun onInserted(position: Int, count: Int) {}
    override fun onRemoved(position: Int, count: Int) {}
    override fun onMoved(fromPosition: Int, toPosition: Int) {}
    override fun onChanged(position: Int, count: Int, payload: Any?) {}
}