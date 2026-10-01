package com.example.medicarealert.data.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.medicarealert.data.auth.UserAccount
import com.example.medicarealert.data.auth.UserDao
import com.example.medicarealert.data.diary.DiaryDao
import com.example.medicarealert.data.logs.DoseLog
import com.example.medicarealert.data.logs.DoseLogDao
import com.example.medicarealert.data.meds.Medication
import com.example.medicarealert.data.meds.MedicationDao
import com.example.medicarealert.data.invite.Invite
import com.example.medicarealert.data.invite.InviteDao
import com.example.medicarealert.data.manage.CareLink
import com.example.medicarealert.data.manage.CareLinkDao
import com.example.medicarealert.data.trackers.HealthRecord
import com.example.medicarealert.data.diary.DiaryEntry
import com.example.medicarealert.data.trackers.HealthRecordDao

@Database(
    entities = [
        Medication::class,
        DoseLog::class,
        UserAccount::class,
        Invite::class,
        CareLink::class,
        HealthRecord::class,
        DiaryEntry::class

    ],
    version = 8,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {

    abstract fun medicationDao(): MedicationDao
    abstract fun doseLogDao(): DoseLogDao
    abstract fun userDao(): UserDao
    abstract fun inviteDao(): InviteDao
    abstract fun careLinkDao(): CareLinkDao
    abstract fun healthRecordDao(): HealthRecordDao
    abstract fun diaryDao(): DiaryDao

    companion object {
        @Volatile private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase =
            INSTANCE ?: synchronized(this) {
                Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "medicare_alert_db"
                )
                    // NOTE: ช่วงพัฒนาให้ล้าง DB เมื่อ schema เปลี่ยน
                    // ถ้าจะเก็บข้อมูลจริง ให้ทำ Migration แทน
                    .fallbackToDestructiveMigration()
                    .build()
                    .also { INSTANCE = it }
            }
    }
}
