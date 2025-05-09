package com.bizsolutions.dealmate.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface KeywordDao {
    @Query("SELECT * FROM keywords ORDER BY completedCount DESC, postponedCount DESC")
    fun getAllKeywords(): Flow<List<KeywordEntity>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertKeyword(keyword: KeywordEntity): Long

    @Update
    suspend fun updateKeyword(keyword: KeywordEntity)

    @Query("SELECT * FROM keywords WHERE word = :word")
    suspend fun getKeyword(word: String): KeywordEntity?
}