package com.sobodigital.zulbelajarandroid.data.paging

import android.util.Log
import androidx.paging.LoadState
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.google.gson.Gson
import com.sobodigital.zulbelajarandroid.data.model.ErrorResponse
import com.sobodigital.zulbelajarandroid.data.model.Story
import com.sobodigital.zulbelajarandroid.data.remote.StoryRemoteDataSource

class StoryPagingDataSource(private val storyRemoteDataSource: StoryRemoteDataSource) : PagingSource<Int, Story>() {
    override fun getRefreshKey(state: PagingState<Int, Story>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            val anchorPage = state.closestPageToPosition(anchorPosition)
            anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Story> {

        return try {
            val page = params.key ?: INITIAL_PAGE_INDEX
            val response = storyRemoteDataSource.fetchStories(0)

            if(!response.isSuccessful) {
                val errorJsonString = response.errorBody()?.string()
                val error = Gson().fromJson(errorJsonString, ErrorResponse::class.java)
                Log.e(TAG, "Error fetch: ${error}")
                val unauthorizedCodes = listOf(401, 403, 419, 415)

                if(response.code() in unauthorizedCodes) {

                }
            }
            Log.d(TAG, response.body().toString())
            return response.body()?.let { data ->
                val list = data.listStory ?: listOf()
                LoadResult.Page(
                    data = list,
                    prevKey = if (page == 1) null else page - 1,
                    nextKey = if (list.isEmpty()) null else page + 1
                )
            }!!


        } catch (error: Exception) {
            LoadResult.Error(error)
        }

//        return try {
//            val page = params.key ?: INITIAL_PAGE_INDEX
//            val responseData = storyRemoteDataSource.fetchStories(page, params.loadSize)
//
//            LoadResult.Page(
//                data = responseData,
//                prevKey = if (page == 1) null else page - 1,
//                nextKey = if (responseData.isNullOrEmpty()) null else page + 1
//            )
//        } catch (exception: Exception) {
//            return LoadResult.Error(exception)
//        }
    }

    private companion object {
        const val INITIAL_PAGE_INDEX = 1
        private  val TAG = StoryPagingDataSource::class.simpleName

    }

}