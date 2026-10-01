package com.example.medicarealert.data.auth

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface UserDao {
    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun insert(user: UserAccount): Long

    @Query("SELECT COUNT(*) FROM user_accounts WHERE username = :username")
    suspend fun existsUsername(username: String): Int

    @Query("SELECT * FROM user_accounts WHERE username = :username AND passwordHash = :hash LIMIT 1")
    suspend fun login(username: String, hash: String): UserAccount?

    @Query("SELECT * FROM user_accounts WHERE userId = :id LIMIT 1")
    suspend fun getById(id: Long): UserAccount?
}
