package com.example.medicarealert.data.trackers

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface HealthRecordDao {
    @Query("SELECT * FROM health_record WHERE userId=:user ORDER BY recordedAt DESC")
    fun flowByUser(user: Long): Flow<List<HealthRecord>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(record: HealthRecord): Long

    @Update
    suspend fun update(record: HealthRecord): Int

    @Delete
    suspend fun delete(record: HealthRecord): Int
}
