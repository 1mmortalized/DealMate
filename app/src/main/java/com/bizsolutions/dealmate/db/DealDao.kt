package com.bizsolutions.dealmate.db

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import kotlinx.coroutines.flow.Flow

@Dao
interface DealDao {
    @Transaction
    @Query("SELECT * FROM deals ORDER BY id DESC")
    fun getAllDeals(): Flow<List<DealWithClient>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertDeal(task: DealEntity)

    @Delete
    suspend fun deleteDeal(task: DealEntity)
}