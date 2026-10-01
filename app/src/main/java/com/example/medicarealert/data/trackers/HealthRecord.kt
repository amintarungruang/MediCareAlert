package com.example.medicarealert.data.trackers

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "health_record")
data class HealthRecord(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val userId: Long,
    val recordedAt: Long,
    val type: String,
    val value: String,
    val unit: String?,
    val note: String?
)
