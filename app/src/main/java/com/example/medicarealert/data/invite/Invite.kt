package com.example.medicarealert.data.invite

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "invite")
data class Invite(
    @PrimaryKey(autoGenerate = true) val inviteId: Long = 0,
    val inviterUserId: Long,
    val code: String,               // MF-XXXXXXXX
    val role: String,               // "caregiver" | "friend"
    val createdAt: Long,
    val expiresAt: Long,
    val status: String = "active"   // active | used | expired
)
