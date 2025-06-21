package com.sobodigital.zulbelajarandroid.data.local.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.sobodigital.zulbelajarandroid.data.local.db.entity.FavoriteEntity

@Dao
interface StoryDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insert(note: FavoriteEntity)

    @Update
    fun update(note: FavoriteEntity)

    @Query("DELETE from favorites WHERE event_id =:id")
    fun deleteById(id: Int)

    @Query("SELECT * from favorites ORDER BY id ASC")
    fun getAll(): List<FavoriteEntity>

    @Query("SELECT * from favorites WHERE event_id =:id")
    fun getEventById(id: Int): FavoriteEntity
}