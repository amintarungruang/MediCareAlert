package com.example.medicarealert.data.meds

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface MedicationDao {

    @Query("SELECT * FROM medications WHERE ownerUserId = :ownerId ORDER BY name ASC")
    fun observeAllOf(ownerId: Long): Flow<List<Medication>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(med: Medication): Long

    @Update suspend fun update(med: Medication)
    @Delete suspend fun delete(med: Medication)
}

