package com.sobodigital.zulbelajarandroid.data.local.db.dao

import androidx.room.Dao
import androidx.room.Query
import com.sobodigital.zulbelajarandroid.data.local.db.entity.StoryEntity

@Dao
interface StoryDao : BaseDao<StoryEntity> {

    @Query("SELECT * FROM favorites ORDER BY id ASC")
    override fun getAll(): List<StoryEntity>

    @Query("SELECT * FROM favorites WHERE story_id = :id")
    override fun getStoryById(id: String): StoryEntity
}
