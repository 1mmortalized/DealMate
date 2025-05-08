package com.bizsolutions.dealmate.db

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Ignore
import androidx.room.Index
import androidx.room.PrimaryKey
import java.time.LocalDate

@Entity(
    tableName = "tasks",
    foreignKeys = [ForeignKey(
        entity = ClientEntity::class, parentColumns = ["id"],
        childColumns = ["clientId"], onDelete = ForeignKey.CASCADE
    )],
    indices = [Index(value = ["clientId"])]
)
data class TaskEntity(
    @PrimaryKey(autoGenerate = true) var id: Int = 0,
    var title: String,
    var date: LocalDate,
    var priority: Int,
    @ColumnInfo(defaultValue = "") var description: String,
    @ColumnInfo(defaultValue = "0") var progress: Int,
    var clientId: Int,
    @ColumnInfo(defaultValue = "0") var postponed: Boolean
) {
    @Ignore
    constructor(): this(0, "", LocalDate.now(), 3, "", 0, 0, false)

    @Ignore
    fun extractKeywords(): List<String> {
        val stopWords = setOf("і", "в", "на", "з", "та", "що", "це", "по", "до", "за")
        return title.lowercase()
            .replace(Regex("[^a-zа-яіїєґ0-9 ]"), "")
            .split(" ")
            .filter { it.isNotBlank() && it !in stopWords }
    }
}