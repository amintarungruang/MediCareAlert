package com.example.medicarealert.data.logs

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface DoseLogDao {

    @Insert suspend fun insert(log: DoseLog)

    @Query("SELECT * FROM dose_logs WHERE ownerUserId = :ownerId ORDER BY timestamp DESC")
    fun observeAllOf(ownerId: Long): Flow<List<DoseLog>>

    @Query("""
    SELECT * FROM dose_logs
    WHERE ownerUserId = :ownerId
      AND timestamp BETWEEN :startMillis AND :endMillis
    ORDER BY timestamp DESC
""")
    fun observeByDay(
        ownerId: Long,
        startMillis: Long,
        endMillis: Long
    ): kotlinx.coroutines.flow.Flow<List<DoseLog>>
}

