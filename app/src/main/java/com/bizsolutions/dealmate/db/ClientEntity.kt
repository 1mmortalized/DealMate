package com.bizsolutions.dealmate.db

import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey

@Entity(tableName = "clients")
data class ClientEntity(
    @PrimaryKey(autoGenerate = true) var id: Int = 0,
    var name: String,
    var email: String,
    var phone: String
) {
    @Ignore
    constructor(): this(0, "", "", "")
}