package com.example.medicarealert.data.auth

import android.content.Context
import com.example.medicarealert.data.db.AppDatabase
import java.security.MessageDigest

class AuthRepository private constructor(ctx: Context) {
    private val dao = AppDatabase.getDatabase(ctx).userDao()

    suspend fun register(displayName: String, username: String, password: String): Long {
        if (dao.existsUsername(username) > 0) {
            throw IllegalStateException("มีผู้ใช้งานชื่อนี้อยู่แล้ว")
        }
        val id = dao.insert(
            UserAccount(
                username = username.trim(),
                passwordHash = hash(password),
                displayName = displayName.trim()
            )
        )
        return id
    }

    suspend fun login(username: String, password: String): UserAccount? {
        return dao.login(username.trim(), hash(password))
    }

    private fun hash(s: String): String {
        val md = MessageDigest.getInstance("SHA-256")
        return md.digest(s.toByteArray()).joinToString("") { "%02x".format(it) }
    }

    companion object {
        @Volatile private var INSTANCE: AuthRepository? = null
        fun getInstance(ctx: Context): AuthRepository =
            INSTANCE ?: synchronized(this) {
                AuthRepository(ctx.applicationContext).also { INSTANCE = it }
            }
    }
}
