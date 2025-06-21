package com.sobodigital.zulbelajarandroid.data.local.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.sobodigital.zulbelajarandroid.data.local.db.entity.FavoriteEntity

@Database(entities = [FavoriteEntity::class], version = 1)
abstract class AppDatabase : RoomDatabase() {

    abstract fun storyDao(): StoryDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        @JvmStatic
        fun getDatabase(context: Context): AppDatabase {
            if (INSTANCE == null) {
                synchronized(AppDatabase::class.java) {
                    INSTANCE = Room.databaseBuilder(context.applicationContext,
                        AppDatabase::class.java, "event_db")
                        .build()
                }
            }
            return INSTANCE as AppDatabase
        }
    }
}
