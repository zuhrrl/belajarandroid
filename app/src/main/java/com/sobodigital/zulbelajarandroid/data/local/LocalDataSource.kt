package com.sobodigital.zulbelajarandroid.data.local

import com.sobodigital.zulbelajarandroid.data.local.db.dao.BaseDao
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class LocalDataSource<T, D : BaseDao<T>>(
    private val dao: D
) {
    private val executorService: ExecutorService = Executors.newSingleThreadExecutor()

    fun insert(entity: T) {
        executorService.execute {
            dao.insert(entity)
        }
    }

    fun update(entity: T) {
        executorService.execute {
            dao.update(entity)
        }
    }

    fun getDao(): D = dao
}