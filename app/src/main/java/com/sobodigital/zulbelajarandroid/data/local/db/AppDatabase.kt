package com.sobodigital.zulbelajarandroid.data.local.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.sobodigital.zulbelajarandroid.data.local.db.dao.BaseDao
import com.sobodigital.zulbelajarandroid.data.local.db.dao.StoryDao
import com.sobodigital.zulbelajarandroid.data.local.db.entity.StoryEntity

@Database(entities = [StoryEntity::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun storyDao(): StoryDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null
        private val dbName = "story_app_db"

        @JvmStatic
        fun getDatabase(context: Context): AppDatabase {
            if (INSTANCE == null) {
                synchronized(AppDatabase::class.java) {
                    INSTANCE = Room.databaseBuilder(context.applicationContext,
                        AppDatabase::class.java, dbName)
                        .build()
                }
            }
            return INSTANCE as AppDatabase
        }
    }
}
