package com.bizsolutions.dealmate.db

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import java.time.LocalDate
import java.util.Date

@Entity(
    tableName = "deals",
    foreignKeys = [ForeignKey(
        entity = ClientEntity::class, parentColumns = ["id"],
        childColumns = ["clientId"], onDelete = ForeignKey.CASCADE
    )],
    indices = [Index(value = ["clientId"])]
)
data class DealEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val title: String,
    val amount: Int,
    val currency: String,
    val clientId: Int,
    @ColumnInfo(defaultValue = "0") val date: LocalDate
)