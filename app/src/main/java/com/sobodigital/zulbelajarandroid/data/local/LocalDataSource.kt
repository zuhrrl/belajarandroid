package com.sobodigital.zulbelajarandroid.data.local

import android.content.Context
import com.sobodigital.zulbelajarandroid.data.local.db.AppDatabase
import com.sobodigital.zulbelajarandroid.data.local.db.StoryDao
import com.sobodigital.zulbelajarandroid.data.local.db.entity.FavoriteEntity
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class LocalDataSource(context: Context) {
    private val storyDao: StoryDao
    private val executorService: ExecutorService = Executors.newSingleThreadExecutor()

    init {
        val db = AppDatabase.getDatabase(context)
        storyDao = db.storyDao()
    }

    fun getAllEvent(): List<FavoriteEntity> {
        return storyDao.getAll()
    }

    fun insert(event: FavoriteEntity) {
        executorService.execute { storyDao.insert(event) }
    }

    fun deleteById(id: Int) {
        executorService.execute { storyDao.deleteById(id) }
    }

    fun update(event: FavoriteEntity) {
        executorService.execute { storyDao.update(event) }
    }

    fun getEventById(id: Int) : FavoriteEntity {
        return storyDao.getEventById(id)
    }

    companion object {
        @Volatile
        private var instance: LocalDataSource? = null
        fun getInstance(
            context: Context,
        ): LocalDataSource =
            instance ?: synchronized(this) {
                instance ?: LocalDataSource(context)
            }.also { instance = it }
    }
}