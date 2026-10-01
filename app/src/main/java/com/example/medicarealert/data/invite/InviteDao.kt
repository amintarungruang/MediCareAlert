package com.example.medicarealert.data.invite

import androidx.room.*

@Dao
interface InviteDao {

    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun insert(invite: Invite): Long

    @Query("SELECT * FROM invite WHERE code = :code LIMIT 1")
    suspend fun findByCode(code: String): Invite?

    @Query("UPDATE invite SET status='used' WHERE inviteId = :id")
    suspend fun markUsed(id: Long)

    @Query("UPDATE invite SET status='expired' WHERE expiresAt < :now AND status='active'")
    suspend fun expireOld(now: Long)

    @Delete
    suspend fun delete(invite: Invite)
}
