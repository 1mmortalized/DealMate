package com.bizsolutions.dealmate.db

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import java.time.LocalDate
import java.time.LocalTime
import java.util.Date

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
    val title: String,
    val time: LocalTime,
    val date: LocalDate,
    val clientId: Int,
    val completed: Boolean
)