package com.sobodigital.zulbelajarandroid.data.local.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.sobodigital.zulbelajarandroid.data.local.db.entity.StoryEntity

@Dao
interface BaseDao<T> {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insert(entity: T)

    @Update
    fun update(entity: T)

    @Query("DELETE from favorites WHERE story_id =:id")
    fun deleteById(id: String)

    @Query("SELECT * from favorites ORDER BY id ASC")
    fun getAll(): List<StoryEntity>

    @Query("SELECT * from favorites WHERE story_id =:id")
    fun getStoryById(id: String): StoryEntity
}