package com.sobodigital.zulbelajarandroid.domain.usecase

import androidx.paging.Pager
import com.sobodigital.zulbelajarandroid.data.Result
import com.sobodigital.zulbelajarandroid.domain.model.Story
import com.sobodigital.zulbelajarandroid.domain.model.UploadStorySession
import com.sobodigital.zulbelajarandroid.domain.model.UploadStoryData

interface StoryUseCase {

    suspend fun fetchStories(locationType: Int = 0): Result<List<Story>?>?

    suspend fun fetchStoryWithPaging(): Result<Pager<Int, Story>>

    suspend fun fetchStoryById(id: String): Result<Story?>?

    suspend fun uploadStory(param: UploadStoryData): Result<UploadStorySession?>?

    suspend fun getStoryFromDbById(id: String): Result<Story>

    suspend fun bookmarkStory(data: Story)

    suspend fun removeBookmarkById(id: String)

}