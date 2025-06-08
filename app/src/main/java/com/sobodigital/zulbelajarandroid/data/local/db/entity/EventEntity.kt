package com.sobodigital.zulbelajarandroid.data.local.db.entity

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Entity(
    tableName = "favorites",
    indices = [Index(value = ["id", "event_id"], unique = true)])
@Parcelize
data class EventEntity(

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    var id: Int = 0,

    @ColumnInfo("event_id")
    val eventId: Int? = null,

    @ColumnInfo("name")
    val name: String? = null,

    @ColumnInfo("ownerName")
    val ownerName: String? = null,

    @ColumnInfo("beginTime")
    val beginTime: String? = null,

    @ColumnInfo("registrants")
    val registrants: Int? = null,

    @ColumnInfo("imageLogo")
    val imageLogo: String? = null,

    @ColumnInfo("link")
    val link: String? = null,

    @ColumnInfo("description")
    val description: String? = null,

    @ColumnInfo("quota")
    val quota: Int? = null,

): Parcelable
