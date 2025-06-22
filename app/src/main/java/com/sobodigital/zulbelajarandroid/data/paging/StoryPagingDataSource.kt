package com.sobodigital.zulbelajarandroid.data.paging

import android.util.Log
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.google.gson.Gson
import com.sobodigital.zulbelajarandroid.data.model.ErrorResponse
import com.sobodigital.zulbelajarandroid.data.model.StoryItemResponse
import com.sobodigital.zulbelajarandroid.data.remote.StoryRemoteDataSource
import com.sobodigital.zulbelajarandroid.domain.model.Story
import com.sobodigital.zulbelajarandroid.utils.DataMapper

class StoryPagingDataSource(private val storyRemoteDataSource: StoryRemoteDataSource) : PagingSource<Int, Story>() {
    override fun getRefreshKey(state: PagingState<Int, Story>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            val anchorPage = state.closestPageToPosition(anchorPosition)
            anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Story> {
        Log.d(TAG, "load in story paging data source")

        return try {
            val page = params.key ?: INITIAL_PAGE_INDEX
            val response = storyRemoteDataSource.fetchStories(0)

            if(!response.isSuccessful) {
                val errorJsonString = response.errorBody()?.string()
                val error = Gson().fromJson(errorJsonString, ErrorResponse::class.java)
                Log.e(TAG, "Error fetch: ${error}")
                val unauthorizedCodes = listOf(401, 403, 419, 415)

                if(response.code() in unauthorizedCodes) {
                    return LoadResult.Error(Exception("${error.message} code: ${response.code()}"))
                }
            }
            Log.d(TAG, response.body().toString())
            return response.body()?.let { data ->
                val domain = data.listStory?.map { item ->
                    DataMapper.mapEntityToDomain(item, {
                        Story(
                            id = it.id,
                            name = it.name,
                            photoUrl = it.photoUrl
                        )
                    })
                }
                val list = domain ?: listOf()
                LoadResult.Page(
                    data = list,
                    prevKey = if (page == 1) null else page - 1,
                    nextKey = if (list.isEmpty()) null else page + 1
                )
            }!!


        } catch (error: Exception) {
            LoadResult.Error(error)
        }

    }

    private companion object {
        const val INITIAL_PAGE_INDEX = 1
        private  val TAG = StoryPagingDataSource::class.simpleName

    }



}