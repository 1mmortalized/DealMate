package com.bizsolutions.dealmate.db

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.LocalDate
import java.time.LocalTime

@Entity(tableName = "events")
data class EventEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val title: String,
    val timeStart: LocalTime,
    val timeEnd: LocalTime,
    val date: LocalDate,
    val completed: Boolean
)