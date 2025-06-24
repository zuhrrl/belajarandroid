package com.sobodigital.zulbelajarandroid.domain.usecase

import androidx.paging.Pager
import com.sobodigital.zulbelajarandroid.data.Result
import com.sobodigital.zulbelajarandroid.domain.model.Story
import com.sobodigital.zulbelajarandroid.domain.model.UploadStorySession
import com.sobodigital.zulbelajarandroid.domain.model.UploadStoryData
import com.sobodigital.zulbelajarandroid.domain.repository.LocalRepository
import com.sobodigital.zulbelajarandroid.domain.repository.StoryRepository

class StoryInteractor(private val storyRepository: StoryRepository) : StoryUseCase {
    override suspend fun fetchStories(locationType: Int): Result<List<Story>?>? {
        return storyRepository.fetchStories(locationType)
    }

    override suspend fun fetchStoryWithPaging(): Result<Pager<Int, Story>> {
        return storyRepository.fetchStoryWithPaging()
    }

    override suspend fun fetchStoryById(id: String): Result<Story?>? {
        return storyRepository.fetchStoryById(id)
    }

    override suspend fun uploadStory(param: UploadStoryData): Result<UploadStorySession?>? {
        return storyRepository.uploadStory(param)
    }

    override suspend fun getStoryFromDbById(id: String): Result<Story> {
        return storyRepository.getStoryFromDbById(id)
    }

    override fun bookmarkStory(data: Story) {
        return storyRepository.bookmarkStory(data)
    }

    override fun removeBookmarkById(id: String) {
        return storyRepository.removeBookmarkById(id)
    }


}