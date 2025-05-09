package com.bizsolutions.dealmate.repository

import com.bizsolutions.dealmate.db.ClientEntity
import com.bizsolutions.dealmate.db.KeywordDao
import com.bizsolutions.dealmate.db.KeywordEntity
import kotlinx.coroutines.flow.Flow
import java.time.LocalDate

class KeywordRepository(private val dao: KeywordDao) {
    val allKeywords: Flow<List<KeywordEntity>> = dao.getAllKeywords()

    suspend fun increasePostponedCount(word: String) {
        val existing = dao.getKeyword(word)
        if (existing != null) {
            val updated = existing.copy(
                postponedCount = existing.postponedCount + 1,
                lastSeen = LocalDate.now()
            )
            dao.updateKeyword(updated)
        } else {
            dao.insertKeyword(
                KeywordEntity(
                    word = word,
                    postponedCount = 1,
                    lastSeen = LocalDate.now()
                )
            )
        }
    }

    suspend fun increaseCompletedCount(word: String) {
        val existing = dao.getKeyword(word)
        if (existing != null) {
            val updated = existing.copy(
                completedCount = existing.completedCount + 1,
                lastSeen = LocalDate.now()
            )
            dao.updateKeyword(updated)
        } else {
            dao.insertKeyword(
                KeywordEntity(
                    word = word,
                    completedCount = 1,
                    lastSeen = LocalDate.now()
                )
            )
        }
    }
}