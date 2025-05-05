package com.bizsolutions.dealmate.db

import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
import java.time.LocalDate
import java.time.LocalTime

@Entity(tableName = "events")
data class EventEntity(
    @PrimaryKey(autoGenerate = true) var id: Int = 0,
    var title: String,
    var timeStart: LocalTime,
    var timeEnd: LocalTime,
    var date: LocalDate,
    var completed: Boolean
) {
    @Ignore
    constructor(): this(0, "", LocalTime.now(), LocalTime.now(), LocalDate.now(), false)
}