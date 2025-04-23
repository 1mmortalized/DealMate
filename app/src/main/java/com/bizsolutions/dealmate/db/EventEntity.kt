package com.bizsolutions.dealmate.db

import androidx.room.Entity
import androidx.room.Ignore
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
) {
    @Ignore
    constructor(): this(0, "", LocalTime.now(), LocalTime.now(), LocalDate.now(), false)
}