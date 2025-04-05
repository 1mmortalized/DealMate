package com.bizsolutions.dealmate.db

import androidx.room.Embedded
import androidx.room.Relation

data class TaskWithClient(
    @Embedded val task: TaskEntity,

    @Relation(
        parentColumn = "clientId",
        entityColumn = "id"
    )
    val client: ClientEntity
)