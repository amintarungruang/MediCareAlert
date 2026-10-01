package com.example.medicarealert.data.invite

import android.content.Context
import com.example.medicarealert.data.db.AppDatabase

class InviteRepository private constructor(ctx: Context) {
    private val dao = AppDatabase.getDatabase(ctx).inviteDao()

    suspend fun insert(invite: Invite): Long = dao.insert(invite)
    suspend fun findByCode(code: String) = dao.findByCode(code)
    suspend fun markUsed(id: Long) = dao.markUsed(id)
    suspend fun expireOld(now: Long) = dao.expireOld(now)

    companion object {
        @Volatile private var I: InviteRepository? = null
        fun getInstance(ctx: Context): InviteRepository =
            I ?: synchronized(this) { I ?: InviteRepository(ctx.applicationContext).also { I = it } }
    }
}
