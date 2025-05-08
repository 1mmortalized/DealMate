package com.bizsolutions.dealmate.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface DealDao {
    @Transaction
    @Query("SELECT * FROM deals ORDER BY id DESC")
    fun getAllDeals(): Flow<List<DealWithClient>>

    @Transaction
    @Query("SELECT * FROM deals WHERE id = :id")
    fun getDealById(id: Int): Flow<DealWithClient>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertDeal(deal: DealEntity)

    @Update
    suspend fun updateDeal(deal: DealEntity)

    @Query("DELETE FROM deals WHERE id = :id")
    suspend fun deleteDeal(id: Int)
}