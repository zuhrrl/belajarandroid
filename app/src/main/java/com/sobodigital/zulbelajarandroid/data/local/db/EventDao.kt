package com.sobodigital.zulbelajarandroid.data.local.db

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.sobodigital.zulbelajarandroid.data.local.db.entity.EventEntity

@Dao
interface EventDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insert(note: EventEntity)
    @Update
    fun update(note: EventEntity)

    @Query("DELETE from favorites WHERE event_id =:id")
    fun deleteById(id: Int)

    @Query("SELECT * from favorites ORDER BY id ASC")
    fun getAll(): List<EventEntity>

    @Query("SELECT * from favorites WHERE event_id =:id")
    fun getEventById(id: Int): EventEntity
}