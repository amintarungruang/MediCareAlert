package com.example.medicarealert.data.logs

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "dose_logs")
data class DoseLog(
    @PrimaryKey(autoGenerate = true) val logId: Long = 0,
    @ColumnInfo(index = true) val ownerUserId: Long,
    val medId: Long,
    val medName: String,
    val timestamp: Long,
    val status: String
)

