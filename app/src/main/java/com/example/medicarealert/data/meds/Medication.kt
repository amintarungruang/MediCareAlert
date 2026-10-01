package com.example.medicarealert.data.meds

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "medications")
data class Medication(
    @PrimaryKey(autoGenerate = true) val id: Long = 0L,
    @ColumnInfo(index = true) val ownerUserId: Long,
    @ColumnInfo(name = "name")   val name: String,
    @ColumnInfo(name = "dose")   val dose: String? = null,
    @ColumnInfo(name = "hour")   val hour: Int = 8,
    @ColumnInfo(name = "minute") val minute: Int = 0,
    @ColumnInfo(name = "note")   val note: String? = null,

    val mon: Boolean = false,
    val tue: Boolean = false,
    val wed: Boolean = false,
    val thu: Boolean = false,
    val fri: Boolean = false,
    val sat: Boolean = false,
    val sun: Boolean = false
)

