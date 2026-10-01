package com.example.medicarealert.data.meds

import android.content.Context
import com.example.medicarealert.data.db.AppDatabase
import com.example.medicarealert.data.logs.DoseLog
import kotlinx.coroutines.flow.Flow

class MedicationRepository private constructor(context: Context) {

    private val db = AppDatabase.getDatabase(context)
    private val medDao = db.medicationDao()
    private val logDao = db.doseLogDao()

    fun getAllOf(ownerId: Long): Flow<List<Medication>> = medDao.observeAllOf(ownerId)

    suspend fun upsert(med: Medication): Long {

        return if (med.id == 0L) medDao.insert(med).also {  }
        else { medDao.update(med); med.id }
    }

    suspend fun delete(med: Medication) = medDao.delete(med)

    // กด “กินแล้ว”
    suspend fun markTaken(ownerId: Long, med: Medication) {
        val now = System.currentTimeMillis()
        logDao.insert(
            DoseLog(
                ownerUserId = ownerId,
                medId = med.id,
                medName = med.name,
                timestamp = now,
                status = "TAKEN"
            )
        )
    }

    fun getAllLogsOf(ownerId: Long): Flow<List<DoseLog>> = logDao.observeAllOf(ownerId)
    fun observeLogsByDay(ownerId: Long, startMillis: Long, endMillis: Long)
            = logDao.observeByDay(ownerId, startMillis, endMillis)
    companion object {
        @Volatile private var INSTANCE: MedicationRepository? = null

        fun getInstance(context: Context): MedicationRepository =
            INSTANCE ?: synchronized(this) {
                MedicationRepository(context.applicationContext).also { INSTANCE = it }
            }
    }
}
