package com.example.medicarealert.data.diary

import android.content.Context
import com.example.medicarealert.data.db.AppDatabase

class DiaryRepository private constructor(ctx: Context) {
    private val dao = AppDatabase.getDatabase(ctx).diaryDao()

    fun flowByUser(user: Long) = dao.flowByUser(user)
    suspend fun insert(e: DiaryEntry) = dao.insert(e)
    suspend fun update(e: DiaryEntry) = dao.update(e)
    suspend fun delete(e: DiaryEntry) = dao.delete(e)

    companion object {
        @Volatile private var I: DiaryRepository? = null
        fun getInstance(ctx: Context) = I ?: synchronized(this) {
            I ?: DiaryRepository(ctx.applicationContext).also { I = it }
        }
    }
}
