package com.sobodigital.zulbelajarandroid.domain.repository

import androidx.paging.Pager
import com.sobodigital.zulbelajarandroid.data.Result
import com.sobodigital.zulbelajarandroid.data.model.UploadResponse
import com.sobodigital.zulbelajarandroid.data.model.UploadStoryParameter
import com.sobodigital.zulbelajarandroid.domain.model.Story

interface StoryRepository {

    suspend fun fetchStories(locationType: Int = 0): Result<List<Story>?>?

    suspend fun fetchStoryWithPaging(): Result<Pager<Int, Story>>

    suspend fun fetchStoryById(id: String): Result<Story?>?

    suspend fun uploadStory(param: UploadStoryParameter): Result<UploadResponse?>?

}