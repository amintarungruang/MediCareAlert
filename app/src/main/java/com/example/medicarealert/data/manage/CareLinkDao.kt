package com.example.medicarealert.data.manage

import androidx.room.*

@Dao
interface CareLinkDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(link: CareLink): Long

    @Update
    suspend fun update(link: CareLink)

    @Delete
    suspend fun delete(link: CareLink)

    @Query("""
        UPDATE carelink 
        SET status = :status, endDate = :endDate 
        WHERE caregiverId = :caregiverId AND patientId = :patientId
    """)
    suspend fun setStatus(
        caregiverId: Long,
        patientId: Long,
        status: String,
        endDate: Long? = null
    ): Int


    @Query("SELECT * FROM carelink WHERE patientId = :patientId AND status = 'ใช้งาน'")
    suspend fun caregiversOf(patientId: Long): List<CareLink>

    @Query("SELECT * FROM carelink WHERE caregiverId = :caregiverId AND status = 'ใช้งาน'")
    suspend fun patientsOf(caregiverId: Long): List<CareLink>


    @Query("""
        SELECT * FROM carelink 
        WHERE caregiverId = :caregiverId AND patientId = :patientId 
        LIMIT 1
    """)
    suspend fun findLink(caregiverId: Long, patientId: Long): CareLink?

    @Query("UPDATE carelink SET canManageMeds = :enabled WHERE carelinkId = :id")
    suspend fun setManageMeds(id: Long, enabled: Boolean): Int

    @Query("""
        SELECT canManageMeds FROM carelink
        WHERE caregiverId = :caregiverId AND patientId = :patientId
          AND status = 'ใช้งาน'
        LIMIT 1
    """)
    suspend fun canManage(caregiverId: Long, patientId: Long): Boolean?
}
