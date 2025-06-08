package com.sobodigital.zulbelajarandroid.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
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
import com.sobodigital.zulbelajarandroid.data.model.Story
import com.sobodigital.zulbelajarandroid.data.repository.LocalRepository
import com.sobodigital.zulbelajarandroid.data.repository.StoryRepository
import com.sobodigital.zulbelajarandroid.getOrAwaitValue
import com.sobodigital.zulbelajarandroid.ui.adapter.StoryAdapterWithPaging
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.TimeoutCancellationException
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.withTimeout
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.junit.MockitoJUnitRunner

@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
class FeedViewModelTest {
    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    val mainDispatcherRules = MainDispatcherRule()

    @Mock
    private lateinit var storyRepository: StoryRepository

    @Mock
    private lateinit var localRepository: LocalRepository

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

        Mockito.`when`(storyRepository.fetchStoryWithPaging()).thenReturn(Result.Success(pager))
        val mainViewModel = FeedViewModel(storyRepository, localRepository)

        // act
        mainViewModel.fetchStoryWithPaging()
        advanceUntilIdle()

        val actualStories: PagingData<Story> = mainViewModel.getStories().getOrAwaitValue()

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
    fun `when Get Empty Sries Should Return No Data`() = runTest {

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

        Mockito.`when`(storyRepository.fetchStoryWithPaging()).thenReturn(Result.Success(pager))
        val mainViewModel = FeedViewModel(storyRepository, localRepository)

        // act
        mainViewModel.fetchStoryWithPaging()
        advanceUntilIdle()

        val actualStories: PagingData<Story> = mainViewModel.getStories().getOrAwaitValue()

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