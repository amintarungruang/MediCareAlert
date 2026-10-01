package com.example.medicarealert.data.diary

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface DiaryDao {
    @Query("SELECT * FROM diary_entry WHERE userId=:user ORDER BY day DESC")
    fun flowByUser(user: Long): Flow<List<DiaryEntry>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(entry: DiaryEntry): Long

    @Update
    suspend fun update(entry: DiaryEntry): Int

    @Delete
    suspend fun delete(entry: DiaryEntry): Int
}
