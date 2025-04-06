package com.bizsolutions.dealmate.db

import androidx.room.Embedded
import androidx.room.Relation

data class CallWithClient(
    @Embedded val call: CallEntity,

    @Relation(
        parentColumn = "clientId",
        entityColumn = "id"
    )
    val client: ClientEntity
)