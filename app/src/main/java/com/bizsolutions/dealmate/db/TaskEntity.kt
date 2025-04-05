package com.bizsolutions.dealmate.db

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import java.time.LocalDate
import java.util.Date

@Entity(
    tableName = "tasks",
    foreignKeys = [ForeignKey(
        entity = ClientEntity::class, parentColumns = ["id"],
        childColumns = ["clientId"], onDelete = ForeignKey.CASCADE
    )],
    indices = [Index(value = ["clientId"])]
)
data class TaskEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val title: String,
    val date: LocalDate,
    val priority: Int,
    val clientId: Int,
    val completed: Boolean
)