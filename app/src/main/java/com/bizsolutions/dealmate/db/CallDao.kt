package com.bizsolutions.dealmate.db

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import kotlinx.coroutines.flow.Flow
import java.time.LocalDate

@Dao
interface CallDao {
    @Transaction
    @Query("SELECT * FROM calls WHERE date = :date ORDER BY time ASC, id ASC")
    fun getAllCallsByDate(date: LocalDate): Flow<List<CallWithClient>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCall(task: CallEntity)

    @Delete
    suspend fun deleteCall(task: CallEntity)

    @Query("UPDATE calls SET completed = :completed WHERE id = :id")
    suspend fun completeCall(id: Int, completed: Boolean)
}