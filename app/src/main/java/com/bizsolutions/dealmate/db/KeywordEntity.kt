package com.bizsolutions.dealmate.db

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.LocalDate

@Entity(tableName = "keywords")
data class KeywordEntity(
    @PrimaryKey val word: String,
    val completedCount: Int = 0,
    val postponedCount: Int = 0,
    val lastSeen: LocalDate? = null
)