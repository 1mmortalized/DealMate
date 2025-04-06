package com.bizsolutions.dealmate.db

import androidx.room.Embedded
import androidx.room.Relation

data class DealWithClient(
    @Embedded val deal: DealEntity,

    @Relation(
        parentColumn = "clientId",
        entityColumn = "id"
    )
    val client: ClientEntity
)