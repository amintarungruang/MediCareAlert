package com.example.medicarealert.data.manage

import android.content.Context
import com.example.medicarealert.data.db.AppDatabase

class CareLinkRepository private constructor(ctx: Context) {
    private val dao = AppDatabase.getDatabase(ctx).careLinkDao()
    suspend fun insertLink(
        caregiverId: Long,
        patientId: Long,
        relation: String,
        status: String = "ใช้งาน",
        startDate: Long = System.currentTimeMillis(),
        endDate: Long? = null
    ): Long {
        // กันซ้ำ
        val exist = dao.findLink(caregiverId, patientId)
        if (exist != null) {

            if (exist.status != status || exist.endDate != null) {
                dao.setStatus(caregiverId, patientId, status, null)
            }
            return exist.carelinkId
        }
        val link = CareLink(
            caregiverId = caregiverId,
            patientId = patientId,
            relation = relation,
            status = status,
            startDate = startDate,
            endDate = endDate
        )
        val rowId = dao.insert(link)
        return if (rowId != 0L) rowId else dao.findLink(caregiverId, patientId)!!.carelinkId
    }

    suspend fun disconnect(caregiverId: Long, patientId: Long) {
        dao.setStatus(caregiverId, patientId, status = "ยกเลิก", endDate = System.currentTimeMillis())
    }

    suspend fun caregiversOf(patientId: Long) = dao.caregiversOf(patientId)
    suspend fun patientsOf(caregiverId: Long) = dao.patientsOf(caregiverId)


    suspend fun setManageMeds(linkId: Long, enabled: Boolean) {
        dao.setManageMeds(linkId, enabled)
    }
    suspend fun canManageMeds(caregiverId: Long, patientId: Long): Boolean {
        // โปรเจกต์รอบนี้ “ตัดปัญหา” — ไม่ให้สิทธิ์คนอื่น
        return false
    }

    companion object {
        @Volatile private var I: CareLinkRepository? = null
        fun getInstance(ctx: Context) =
            I ?: synchronized(this) { I ?: CareLinkRepository(ctx.applicationContext).also { I = it } }
    }
}
