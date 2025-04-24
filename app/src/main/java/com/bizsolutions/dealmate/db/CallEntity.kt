package com.bizsolutions.dealmate.db

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Ignore
import androidx.room.Index
import androidx.room.PrimaryKey
import java.time.LocalDate
import java.time.LocalTime

@Entity(
    tableName = "calls",
    foreignKeys = [ForeignKey(
        entity = ClientEntity::class, parentColumns = ["id"],
        childColumns = ["clientId"], onDelete = ForeignKey.CASCADE
    )],
    indices = [Index(value = ["clientId"])]
)
data class CallEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    var title: String,
    var time: LocalTime,
    var date: LocalDate,
    var clientId: Int,
    val completed: Boolean
) {
    @Ignore
    constructor(): this(0, "", LocalTime.now(), LocalDate.now(), 0, false)
}