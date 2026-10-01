package com.example.medicarealert.data.trackers

import android.content.Context
import com.example.medicarealert.data.db.AppDatabase

class HealthRecordRepository private constructor(ctx: Context) {
    private val dao = AppDatabase.getDatabase(ctx).healthRecordDao()

    fun flowByUser(userId: Long) = dao.flowByUser(userId)
    suspend fun insert(r: HealthRecord) = dao.insert(r)
    suspend fun update(r: HealthRecord) = dao.update(r)
    suspend fun delete(r: HealthRecord) = dao.delete(r)

    companion object {
        @Volatile private var I: HealthRecordRepository? = null
        fun getInstance(ctx: Context) = I ?: synchronized(this) {
            I ?: HealthRecordRepository(ctx.applicationContext).also { I = it }
        }
    }
}
