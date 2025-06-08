package com.sobodigital.zulbelajarandroid.data.local

import android.content.Context
import com.sobodigital.zulbelajarandroid.data.local.db.AppDatabase
import com.sobodigital.zulbelajarandroid.data.local.db.EventDao
import com.sobodigital.zulbelajarandroid.data.local.db.entity.EventEntity
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class LocalDataSource(context: Context) {
    private val eventDao: EventDao
    private val executorService: ExecutorService = Executors.newSingleThreadExecutor()

    init {
        val db = AppDatabase.getDatabase(context)
        eventDao = db.eventDao()
    }

    fun getAllEvent(): List<EventEntity> {
        return eventDao.getAll()
    }

    fun insert(event: EventEntity) {
        executorService.execute { eventDao.insert(event) }
    }

    fun deleteById(id: Int) {
        executorService.execute { eventDao.deleteById(id) }
    }

    fun update(event: EventEntity) {
        executorService.execute { eventDao.update(event) }
    }

    fun getEventById(id: Int) : EventEntity {
        return eventDao.getEventById(id)
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