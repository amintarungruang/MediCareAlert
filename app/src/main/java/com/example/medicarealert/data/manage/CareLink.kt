package com.example.medicarealert.data.manage

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "carelink",
    indices = [
        Index(value = ["caregiverId"]),
        Index(value = ["patientId"]),
        Index(value = ["caregiverId", "patientId"], unique = true)
    ]
)
data class CareLink(
    @PrimaryKey(autoGenerate = true) val carelinkId: Long = 0,
    val caregiverId: Long,
    val patientId: Long,
    val relation: String,
    val status: String,
    val startDate: Long,
    val endDate: Long? = null,
    val canManageMeds: Boolean = true
)
