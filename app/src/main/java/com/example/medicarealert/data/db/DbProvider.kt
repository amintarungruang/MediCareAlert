package com.example.medicarealert.data.db

import android.content.Context
import androidx.room.Room

object DbProvider {
    @Volatile private var INSTANCE: AppDatabase? = null

    fun get(context: Context): AppDatabase {
        return INSTANCE ?: synchronized(this) {
            INSTANCE ?: Room.databaseBuilder(
                context.applicationContext,
                AppDatabase::class.java,
                "medicare.db"
            ).fallbackToDestructiveMigration().build().also { INSTANCE = it }
        }
    }
}
