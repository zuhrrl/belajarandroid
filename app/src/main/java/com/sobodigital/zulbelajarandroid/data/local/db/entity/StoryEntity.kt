package com.sobodigital.zulbelajarandroid.data.local.db.entity

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Entity(
    tableName = "favorites",
    indices = [Index(value = ["id", "story_id"], unique = true)])
@Parcelize
data class StoryEntity(

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    var id: Int = 0,

    @ColumnInfo("story_id")
    val storyId: String? = null,

    @ColumnInfo("name")
    val name: String? = null,

    @ColumnInfo("photo_url")
    val photoUrl: String? = null,

    @ColumnInfo("description")
    val description: String? = null,


    @ColumnInfo("lat")
    val lat: Double? = null,

    @ColumnInfo("lng")
    val lng: Double? = null,

): Parcelable
