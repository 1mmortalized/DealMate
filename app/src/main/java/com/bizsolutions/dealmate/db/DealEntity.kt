package com.bizsolutions.dealmate.db

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Ignore
import androidx.room.Index
import androidx.room.PrimaryKey
import java.time.LocalDate

@Entity(
    tableName = "deals",
    foreignKeys = [ForeignKey(
        entity = ClientEntity::class, parentColumns = ["id"],
        childColumns = ["clientId"], onDelete = ForeignKey.CASCADE
    )],
    indices = [Index(value = ["clientId"])]
)
data class DealEntity(
    @PrimaryKey(autoGenerate = true) var id: Int = 0,
    var title: String,
    var amount: Int,
    var currency: String,
    var clientId: Int,
    @ColumnInfo(defaultValue = "0") var date: LocalDate
) {
    @Ignore
    constructor(): this(0, "", 0, "", 0, LocalDate.now())
}