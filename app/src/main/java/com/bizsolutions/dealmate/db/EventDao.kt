package com.bizsolutions.dealmate.db

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow
import java.time.LocalDate

@Dao
interface EventDao {
    @Query("SELECT * FROM events WHERE date = :date ORDER BY timeStart ASC")
    fun getAllEventsByDate(date: LocalDate): Flow<List<EventEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertEvent(client: EventEntity)

    @Delete
    suspend fun deleteEvent(client: EventEntity)
}